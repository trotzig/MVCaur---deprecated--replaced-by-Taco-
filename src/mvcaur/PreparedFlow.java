package mvcaur;

public class PreparedFlow {

	private RoutingFlow flow;
	private Controller<?> preparedController;
	
	public RoutingFlow getFlow() {
		return flow;
	}
	public void setFlow(RoutingFlow flow) {
		this.flow = flow;
	}
	public Controller<?> getPreparedController() {
		return preparedController;
	}
	public void setPreparedController(Controller<?> preparedController) {
		this.preparedController = preparedController;
	}
	
	
	
}
