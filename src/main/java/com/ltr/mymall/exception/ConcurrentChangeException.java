package com.ltr.mymall.exception;

public class ConcurrentChangeException extends RuntimeException{
	public ConcurrentChangeException(String message, Throwable cause) {
		super(message,cause);
	}
	
	public ConcurrentChangeException(String message) {
		super(message);
	}
}
