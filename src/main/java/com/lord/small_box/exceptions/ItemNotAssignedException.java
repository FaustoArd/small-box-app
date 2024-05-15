package com.lord.small_box.exceptions;

public class ItemNotAssignedException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ItemNotAssignedException(String message) {
		super(message);
		
		
	}

	public ItemNotAssignedException(String message,Throwable cause) {
		super(message,cause);
	}
}
