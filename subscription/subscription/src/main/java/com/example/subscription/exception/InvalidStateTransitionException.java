package com.example.subscription.exception;

public class InvalidStateTransitionException extends RuntimeException {

	public InvalidStateTransitionException(String message){
		super(message);
	}
}
