package com.aarete.pi.exception;

public class WorkbenchException extends Exception {

	private static final long serialVersionUID = -940932003775850373L;

	private String message;

	public WorkbenchException(String message) {
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
