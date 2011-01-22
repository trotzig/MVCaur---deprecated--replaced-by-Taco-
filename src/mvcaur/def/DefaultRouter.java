package mvcaur.def;

import mvcaur.Router;

public class DefaultRouter extends Router {

	@Override
	protected void init() {
		route("/").through(DefaultController.class).renderedBy("/start.jsp");
		route("/json/{message}/{number:int}").through(DefaultController.class).renderAsJson();
		route("/hello/{message}").through(DefaultController.class).renderedBy("/start.jsp");
		route("/hello/{message}/{number:int}").through(DefaultController.class).renderedBy("/start.jsp");
	}
}
