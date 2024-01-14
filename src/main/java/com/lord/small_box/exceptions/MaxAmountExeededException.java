package com.lord.small_box.exceptions;

public class MaxAmountExeededException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public MaxAmountExeededException(String message) {
		super(message);
	}

}
