package com.example.subscription.exception;

public class DuplicateSubscriptionException extends RuntimeException{

	public DuplicateSubscriptionException(String message){
		super(message);
	}
}
