package mvcaur;

/**
 * A {@link PreparedFlow} is an object instructing the router how to continue
 * its route.
 * 
 * @author henper
 * 
 */
public class PreparedFlow {

	private RoutingFlow flow;
	private Controller<?> preparedController;

	/**
	 * Gets the flow description for this prepared flow.
	 * 
	 * @return
	 */
	public RoutingFlow getFlow() {
		return flow;
	}

	public void setFlow(RoutingFlow flow) {
		this.flow = flow;
	}

	/**
	 * Gets the controller, prepared for execution.
	 * 
	 * @return
	 */
	public Controller<?> getPreparedController() {
		return preparedController;
	}

	public void setPreparedController(Controller<?> preparedController) {
		this.preparedController = preparedController;
	}

}
