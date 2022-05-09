package com.aarete.pi.config.security;

public class AuthException extends RuntimeException {

	private static final long serialVersionUID = 7179227171998368907L;

	public AuthException(String message, Throwable cause) {
		super(message, cause);
	}
}