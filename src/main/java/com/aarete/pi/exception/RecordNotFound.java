package com.aarete.pi.exception;

/**
 * @author mpalla
 */
public class RecordNotFound extends Exception{

	private static final long serialVersionUID = -7523779371259821809L;

	private final String message;

	public RecordNotFound(String message) {
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}
	
}
