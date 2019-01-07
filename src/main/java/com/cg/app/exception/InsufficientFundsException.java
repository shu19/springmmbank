package com.cg.app.exception;

public class InsufficientFundsException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5503959465533294823L;

	public InsufficientFundsException(String message) {
		super(message);
	}

}
