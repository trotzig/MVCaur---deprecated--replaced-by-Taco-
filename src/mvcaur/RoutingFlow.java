package mvcaur;

import java.lang.reflect.Method;
import java.util.Map;

import mvcaur.RegexpMapper.ParamType;
import mvcaur.RegexpMapper.PreparedMapping;
import mvcaur.def.ForwardRenderer;
import mvcaur.def.JsonRenderer;

/**
 * A {@link RoutingFlow} is a representation of a routing flow through MVCaur.
 * For a request, each routing flow is checked to see if it knows how to handle
 * the request. A routing flow knows what request it can handle, which
 * controller to execute and how to render the response.
 * 
 * A response can be rendered by any predefined type represented by {@link Type}
 * , or a custom {@link Renderer}.
 * 
 * @author henper
 * 
 */
public class RoutingFlow {

	private Class<? extends Controller<?>> controller;
	private Renderer renderer;
	private String mapping;
	private RegexpMapper mapper;

	public String getMapping() {
		return mapping;
	}

	protected void setMapping(String mapping) {
		this.mapping = mapping;
		this.mapper = new RegexpMapper(mapping);
	}

	public Class<? extends Controller<?>> getController() {
		return controller;
	}

	protected void setController(Class<? extends Controller<?>> controller) {
		this.controller = controller;
	}

	public Renderer getRenderer() {
		return renderer;
	}

	public void setRenderer(Renderer renderer) {
		this.renderer = renderer;
	}

	/**
	 * Tell the routing flow which controller class to use.
	 * 
	 * @param controllerClass
	 * @return
	 */
	public RoutingFlow through(Class<? extends Controller<?>> controllerClass) {
		setController(controllerClass);
		return this;
	}

	/**
	 * Instruct the routing flow to be forwarded to an internal resource, e.g. a
	 * JSP file.
	 * 
	 * @param forward
	 * @return
	 */
	public RoutingFlow renderedBy(String forward) {
		this.renderer = new ForwardRenderer(forward);
		return this;
	}

	/**
	 * Instruct the routing flow to render through your own custom renderer
	 * 
	 * @param renderer
	 * @return
	 */
	public RoutingFlow renderedBy(Renderer renderer) {
		this.renderer = renderer;
		return this;
	}

	/**
	 * Instruct the routing flow to be rendered as JSON
	 * 
	 * @return
	 */
	public RoutingFlow renderAsJson() {
		this.renderer = new JsonRenderer();
		return this;
	}

	/**
	 * Returns a controller if the routing flow knows how to handle a request
	 * 
	 * @param request
	 *            .getRequestURI()
	 * @return
	 */
	public Controller<?> execute(String requestURI,
			Map<String, String[]> requestParams, ObjectFactory factory) {
		PreparedMapping mapping = mapper.execute(requestURI);
		if (mapping != null) {
			return createController(factory, mapping, requestParams);
		}
		return null;
	}

	private Controller<?> createController(ObjectFactory factory,
			PreparedMapping m, Map<String, String[]> requestParams) {
		Controller<?> ctrl;
		try {
			ctrl = (Controller<?>) factory.create(controller);
		} catch (Exception e) {
			throw new RuntimeException("Failed to create controller", e);
		}
		// first, set params within the url
		Map<String, Object> urlParams = m.getMap();
		for (Map.Entry<String, Object> entry : urlParams.entrySet()) {
			try {
				setParam(ctrl, entry.getValue(), entry.getKey());
			} catch (NoSuchMethodException e) {
				// ignore missing method
			}
		}
		// secondly, set all params from request parameters
		if (requestParams != null) {
			for (Map.Entry<String, String[]> entry : requestParams.entrySet()) {
				handleRequestParam(ctrl, entry);
			}
		}
		return ctrl;
	}

	private void handleRequestParam(Controller<?> ctrl,
			Map.Entry<String, String[]> entry) {
		for (ParamType pt : ParamType.values()) {
			try {
				setParam(ctrl, pt.parse(entry.getValue()[0]), entry.getKey());
				// success, break loop
				return;
			} catch (NoSuchMethodException e) {
				// try next param type
			} catch (NumberFormatException e) {
				// try next param type
			}
		}
	}

	private void setParam(Controller<?> ctrl, Object obj, String name)
			throws NoSuchMethodException {
		String nameUpper = name.substring(0, 1).toUpperCase()
				+ name.substring(1);
		try {
			Method setMethod = controller.getMethod("set" + nameUpper,
					obj.getClass());
			try {
				setMethod.invoke(ctrl, obj);
			} catch (Exception e) {
				// not much to do, ignore
			}
		} catch (SecurityException e) {
			// no proper method found, ignore
		}
	}

	public RoutingFlow route(String route) {
		setMapping(route);
		return this;
	}

}
