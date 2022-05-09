package com.aarete.pi.exception;

public class BadRequestException extends RuntimeException {

	private static final long serialVersionUID = -940932003775850373L;

	private String message;

	public BadRequestException(String message) {
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
