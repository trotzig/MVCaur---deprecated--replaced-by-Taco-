package mvcaur.test;
import static org.junit.Assert.assertEquals;
import mvcaur.RoutingFlow;
import mvcaur.test.controller.ComplexMapping;
import mvcaur.test.controller.NoMapping;

import org.junit.Test;

import com.google.gson.Gson;


public class JsonTest {

	
	@Test
	public void testSimpleJson() {
		RoutingFlow flow = new RoutingFlow().route("/").through(NoMapping.class).renderAsJson();
		Gson gson = new Gson();
		String json = gson.toJson(flow.execute("/", RouterTest.emptyRequestParams).getController().execute());
		assertEquals("\"no-mapping\"", json);
	}
	
	
	@Test
	public void testComplexType() {
		RoutingFlow flow = new RoutingFlow().route("/").through(ComplexMapping.class).renderAsJson();
		Gson gson = new Gson();
		String json = gson.toJson(flow.execute("/", RouterTest.emptyRequestParams).getController().execute());
		assertEquals("[{\"val\":\"foo\"},{\"val\":\"bar\"}]", json);
	}
	
}
