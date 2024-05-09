package com.lord.small_box.exceptions;

public class InvalidFileException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	public InvalidFileException(String message, Throwable cause) {
		super(message,cause);
	}
}
