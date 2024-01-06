package com.lord.small_box.exceptions;

import org.springframework.security.core.AuthenticationException;

public class LoginException extends AuthenticationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public LoginException(String message) {
		super(message);
	}

}
