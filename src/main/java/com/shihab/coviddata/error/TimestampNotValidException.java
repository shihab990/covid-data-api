package com.shihab.coviddata.error;

public class TimestampNotValidException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public TimestampNotValidException() {
        super("Timestamp is not valid");
    }
}
