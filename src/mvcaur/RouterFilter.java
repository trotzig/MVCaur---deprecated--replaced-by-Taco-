package mvcaur;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mvcaur.RoutingFlow.Type;

import com.google.gson.Gson;

public class RouterFilter implements Filter {

	private Router router;
	private static final String correctWebXml = "<filter>\n"
			+ "\t<filter-name>routingFilter</filter-name>\n"
			+ "\t<filter-class>mvcaur.RouterFilter</filter-class>\n"
			+ "\t<init-param>\n" + "\t\t<param-name>router</param-name>\n"
			+ "\t\t<param-value>mvcaur.TestRouter</param-value>\n"
			+ "\t</init-param>\n" + "</filter>\n\n";

	private static final String zeroArgumentConstructorBody = "() {\n"
			+ "\t//default constructor, needed by mvcaur\n" + "}";

	private ObjectFactory objectFactory;

	@Override
	public void destroy() {
		// do nothing
	}

	@SuppressWarnings("unchecked")
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		PreparedFlow flow = router.execute(request.getRequestURI(), request.getParameterMap(), objectFactory);
		if (flow == null) {
			//no url mapping for this request, continue as if nothing happened.
			chain.doFilter(req, resp);
		} else {
			routeThrough(request, response, flow);
		}
	}
	
	private void routeThrough(HttpServletRequest request, HttpServletResponse response, PreparedFlow flow) throws ServletException, IOException {
		Controller<?> ctrl = flow.getPreparedController();
		Object result = ctrl.execute();
		if (flow.getFlow().getType() == Type.FORWARD) {
			request.setAttribute("mvcaur", result);
			request.setAttribute("controller", ctrl);
			request.getRequestDispatcher(flow.getFlow().getForward()).forward(request, response);
		} else if (flow.getFlow().getType() == Type.JSON) {
			renderJson(flow, response, result);
		}
	}

	private void renderJson(PreparedFlow flow, HttpServletResponse response, Object result) throws IOException {
		response.setContentType("application/json");
		Gson gson = router.createGson(flow.getFlow());
		response.getWriter().print(gson.toJson(result));
	}


	@SuppressWarnings("unchecked")
	@Override
	public void init(FilterConfig conf) throws ServletException {
		String routerClass = conf.getInitParameter("router");
		if (routerClass == null) {
			throw new RouterMissingException(
					"No router class configured in web.xml. "
							+ "A correct web.xml configuration should look something like this: "
							+ correctWebXml
							+ "Your web.xml is missing the init-param named router");
		}
		Class<Router> routerClazz;
		try {
			routerClazz = (Class<Router>) Class.forName(routerClass);
			try {
				router = routerClazz.newInstance();
			} catch (Exception e) {
				if (e instanceof ClassCastException) {
					throw new RuntimeException(
							"Failed to create the router. Make sure "
									+ routerClass + " extends "
									+ Router.class.getName(), e);
				}
				throw new RuntimeException(
						"Failed to initialize the router class. Does \""
								+ routerClass
								+ "\" have a zero argument default constructor? "
								+ "If not, add such a constructor:\n"
								+ "public " + routerClazz.getSimpleName()
								+ zeroArgumentConstructorBody, e);
			}
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(
					"The router class can not be found. Is \"" + routerClass
							+ "\" a real class and on the runtime classpath?",
					e);
		}

		router.init();
		initObjectFactory(conf);

	}

	private void initObjectFactory(FilterConfig conf) {
		String ofClassName = conf.getInitParameter("object-factory");
		if (ofClassName != null) {
			try {
				Class<?> clazz = Class.forName(ofClassName);
				Object ofObj;
				try {
					ofObj = clazz.newInstance();
				} catch (Exception e) {
					throw new RuntimeException(
							"Failed to initialize the object-factory class. Does \""
									+ ofClassName
									+ "\" have a zero argument default constructor? "
									+ "If not, add such a constructor:\n"
									+ "public " + clazz.getSimpleName()
									+ zeroArgumentConstructorBody, e);
				}
				try {
					this.objectFactory = (ObjectFactory) ofObj;
				} catch (ClassCastException e) {
					throw new RuntimeException(
							"Failed to create the object factory. Make sure "
									+ ofClassName + " implements the "
									+ ObjectFactory.class.getName()
									+ " interface", e);
				}
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(
						"The object-factory class can not be found. Is \""
								+ ofClassName
								+ "\" a real class and on the runtime classpath?",
						e);
			}
		} else {
			this.objectFactory = new DefaultObjectFactory();
		}
	}

}
