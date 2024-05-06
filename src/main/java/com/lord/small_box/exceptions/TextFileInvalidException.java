package com.lord.small_box.exceptions;

public class TextFileInvalidException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	public TextFileInvalidException(String message,Throwable cause) {
		super(message,cause);
	}
	
	public TextFileInvalidException(String message) {
		super(message);
	}
}
