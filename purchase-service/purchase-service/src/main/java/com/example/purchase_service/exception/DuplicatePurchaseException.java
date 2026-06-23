package com.example.purchase_service.exception;

public class DuplicatePurchaseException extends RuntimeException {

	public DuplicatePurchaseException(String message) {
		super(message);
	}
}