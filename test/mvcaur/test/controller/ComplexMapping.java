package mvcaur.test.controller;

import java.util.Arrays;
import java.util.List;

import mvcaur.Controller;

public class ComplexMapping implements Controller<List<Complex>> {

	@Override
	public List<Complex> execute() {
		return Arrays.asList(new Complex("foo"), new Complex("bar"));
	}

}
