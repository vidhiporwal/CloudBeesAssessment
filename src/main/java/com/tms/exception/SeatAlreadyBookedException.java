package com.tms.exception;

public class SeatAlreadyBookedException extends RuntimeException {
    private static final long serialVersionUID = 1L;

	public SeatAlreadyBookedException(String message) {
        super(message);
    }
}