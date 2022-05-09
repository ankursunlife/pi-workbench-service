package com.aarete.pi.exception;

public class AccessDenied extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2159624502485606398L;
	
	private String message;

	public AccessDenied(String message) {
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
