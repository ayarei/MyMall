package com.ltr.mymall.exception;

public class ConcurrentChange extends RuntimeException{
	public ConcurrentChange(String message, Throwable cause) {
		super(message,cause);
	}
	
	public ConcurrentChange(String message) {
		super(message);
	}
}
