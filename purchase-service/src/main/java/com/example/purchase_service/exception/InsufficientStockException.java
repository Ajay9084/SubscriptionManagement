package com.example.purchase_service.exception;

public class InsufficientStockException extends RuntimeException{
	public InsufficientStockException(String message){
		super(message);
	}
}
