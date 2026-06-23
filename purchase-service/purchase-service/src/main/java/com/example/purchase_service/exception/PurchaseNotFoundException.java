package com.example.purchase_service.exception;

public class PurchaseNotFoundException extends RuntimeException {

	public PurchaseNotFoundException(String message) {
		super(message);
	}
}
