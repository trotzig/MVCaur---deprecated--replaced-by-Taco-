package mvcaur.def;

import mvcaur.Controller;

public class DefaultController implements Controller<String> {

	private String message;
	private Integer number;
	
	@Override
	public String execute() {
		if (message != null) {
			return message + " " + number;
		}
		return "Hello world!";
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public void setNumber(Integer number) {
		this.number = number;
	}
	
	public String getMessage() {
		return message;
	}
	
	public Integer getNumber() {
		return number;
	}

}
