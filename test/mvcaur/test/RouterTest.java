package mvcaur.test;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import mvcaur.Controller;
import mvcaur.RoutingFlow;
import mvcaur.def.DefaultObjectFactory;
import mvcaur.test.controller.AllParamTypes;
import mvcaur.test.controller.NoMapping;

import org.junit.Test;

public class RouterTest {
	
	public static Map<String, String[]> emptyRequestParams = new HashMap<String, String[]>();
	
	@Test
	public void testNoParams() {
		RoutingFlow flow = new RoutingFlow().route("/").through(NoMapping.class);
		assertNull(flow.execute("/55", emptyRequestParams, new DefaultObjectFactory()));
		Controller<?> c = flow.execute("/", emptyRequestParams, new DefaultObjectFactory()); 
		assertNotNull(c);
		assertEquals(NoMapping.class, c.getClass());
	}
	
	
	@Test
	public void testAllParamTypes() {
		RoutingFlow flow = new RoutingFlow().route("/{bool:boolean}/static/{number:int}/{longer:long}/{doubler:double}/{string:string}/{stringDefault}").through(AllParamTypes.class);
		assertNull(flow.execute("/55", emptyRequestParams, new DefaultObjectFactory()));
		assertNull(flow.execute("/", emptyRequestParams, new DefaultObjectFactory()));
		AllParamTypes c = (AllParamTypes) flow.execute("/true/static/1/2/2.5/string/3", emptyRequestParams, new DefaultObjectFactory()); 
		assertNotNull(c);
		assertEquals(Integer.valueOf(1), c.getNumber());
		assertEquals(Long.valueOf(2), c.getLonger());
		assertEquals(Double.valueOf(2.5d), c.getDoubler());
		assertEquals("string", c.getString());
		assertEquals("3", c.getStringDefault());
	}
	
	
	@Test
	public void wrongTypeDoesNotMatch() {
		RoutingFlow flow = new RoutingFlow().route("/{number:int}").through(AllParamTypes.class);
		assertNull(flow.execute("/hh", emptyRequestParams, new DefaultObjectFactory()));
		AllParamTypes c = (AllParamTypes) flow.execute("/123", emptyRequestParams, new DefaultObjectFactory()); 
		assertNotNull(c);
		assertEquals(Integer.valueOf(123), c.getNumber());
	}


	
}
