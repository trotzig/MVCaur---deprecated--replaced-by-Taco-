package mvcaur.test.controller;

import mvcaur.Controller;

public class NoMapping implements Controller<String> {

	@Override
	public String execute() {
		return "no-mapping";
	}
	
}
