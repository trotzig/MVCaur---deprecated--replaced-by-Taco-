package mvcaur.def;

import mvcaur.Controller;
import mvcaur.def.Complex.Inner;

public class ComplexController implements Controller<Complex> {

	@Override
	public Complex execute() {
		Complex c = new Complex();
		c.setName("Test");
		Inner i = new Inner();
		i.setType("type");
		c.setInner(i);
		return c;
	}

}
