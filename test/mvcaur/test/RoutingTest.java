package mvcaur.test;
import static org.junit.Assert.*;

import org.junit.Test;

import mvcaur.PreparedFlow;
import mvcaur.Router;
import mvcaur.test.controller.NoMapping;



public class RoutingTest {

	@Test
	public void testSingleRoute() {
		Router router = new Router() {
			
			@Override
			public void init() {
				route("/").through(NoMapping.class).renderedBy("/start.jsp");
			}
		};
		router.init();
		PreparedFlow flow = router.execute("/");
		NoMapping controller = (NoMapping) flow.getPreparedController();
		assertEquals("no-mapping", controller.execute());
	}
	
	public void testNoMatch() {
		Router router = new Router() {
			
			@Override
			public void init() {
				route("/{}").through(NoMapping.class).renderedBy("/start.jsp");
			}
		};
		PreparedFlow flow = router.execute("/hello/foo/bar");
		assertNull(flow);
	}
}
