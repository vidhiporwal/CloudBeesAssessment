package com.tms.exception;

public class NoSeatsAvailableException extends Exception {
	private static final long serialVersionUID = 1L;
	public NoSeatsAvailableException(String message) {
        super(message);
    }
}
