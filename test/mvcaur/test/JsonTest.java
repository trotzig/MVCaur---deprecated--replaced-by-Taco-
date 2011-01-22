package mvcaur.test;
import static org.junit.Assert.*;
import mvcaur.DefaultObjectFactory;
import mvcaur.RoutingFlow;
import mvcaur.test.controller.ComplexMapping;
import mvcaur.test.controller.NoMapping;

import org.junit.Test;

import com.google.gson.Gson;


public class JsonTest {

	
	@Test
	public void testSimpleJson() {
		RoutingFlow flow = new RoutingFlow().route("/").through(NoMapping.class).renderAsJson();
		assertEquals(RoutingFlow.Type.JSON, flow.getType());
		Gson gson = new Gson();
		String json = gson.toJson(flow.execute("/", RouterTest.emptyRequestParams, new DefaultObjectFactory()).execute());
		assertEquals("\"no-mapping\"", json);
	}
	
	
	@Test
	public void testComplexType() {
		RoutingFlow flow = new RoutingFlow().route("/").through(ComplexMapping.class).renderAsJson();
		assertEquals(RoutingFlow.Type.JSON, flow.getType());
		Gson gson = new Gson();
		String json = gson.toJson(flow.execute("/", RouterTest.emptyRequestParams, new DefaultObjectFactory()).execute());
		assertEquals("[{\"val\":\"foo\"},{\"val\":\"bar\"}]", json);
	}
	
}