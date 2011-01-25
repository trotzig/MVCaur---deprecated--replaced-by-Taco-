package mvcaur.def;

import mvcaur.Router;

public class DefaultRouter extends Router {

	/**
	 * Default, non-arg constructor required by MVCaur
	 */
	public DefaultRouter() {
		/*
		 * Shows how to inject a custom object factory. The object factory is
		 * used when creating new instances of Controllers
		 */
		setObjectFactory(new DefaultObjectFactory());
	}

	@Override
	public void init() {
		route("/").through(DefaultController.class).renderedBy("/start.jsp");
		route("/json/{message}/{number:int}").through(DefaultController.class)
				.renderAsJson();
		route("/hello/{message}").through(DefaultController.class).renderedBy(
				"/start.jsp");
		route("/hello/{message}/{number:int}").through(DefaultController.class)
				.renderedBy("/start.jsp");
	}

}
