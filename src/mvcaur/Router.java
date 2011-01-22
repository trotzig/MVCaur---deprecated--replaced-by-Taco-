package mvcaur;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

/**
 * Base class for the mvcaur routing configuration.
 * 
 * @author henper
 * 
 */
public abstract class Router {

	private List<RoutingFlow> flows = new ArrayList<RoutingFlow>();

	/**
	 * Finds a flow for a specific request URI.
	 * 
	 * @param requestURI
	 * @param requestParams
	 * @param factory
	 * @return
	 */
	public final PreparedFlow execute(String requestURI,
			Map<String, String[]> requestParams, ObjectFactory factory) {
		for (RoutingFlow flow : flows) {
			Controller<?> ctrl = flow.execute(requestURI, requestParams,
					factory);

			if (ctrl != null) {
				PreparedFlow pflow = new PreparedFlow();
				pflow.setFlow(flow);
				pflow.setPreparedController(ctrl);
				return pflow;
			}
		}
		return null;
	}

	/**
	 * This is where all url mappings are configured.
	 */
	protected abstract void init();

	/**
	 * Start configuring routing for a specific url pattern.
	 * 
	 * @param route
	 * @return a routing flow, optimized for method chaining
	 */
	protected RoutingFlow route(String route) {
		RoutingFlow flow = new RoutingFlow();
		flow.setMapping(route);
		flows.add(flow);
		return flow;
	}

	/**
	 * Gets a Gson instance to use when creating json output. Can be overridden
	 * to allow for custom Gson instances.
	 * 
	 * @param flow
	 * @return
	 */
	protected Gson createGson(RoutingFlow flow) {
		return new Gson();
	}

}
