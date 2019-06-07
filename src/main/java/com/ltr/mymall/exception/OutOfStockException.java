package com.ltr.mymall.exception;

public class OutOfStockException extends RuntimeException{
	public OutOfStockException(String message, Throwable cause) {
		super(message,cause);
	}
	
	public OutOfStockException(String message) {
		super(message);
	}
}
