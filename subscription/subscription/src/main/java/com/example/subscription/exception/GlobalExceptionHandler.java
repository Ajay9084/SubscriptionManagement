package com.example.subscription.exception;



import com.example.subscription.dto.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	// Subscription Not Found
	@ExceptionHandler(SubscriptionNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleSubscriptionNotFoundException(
			SubscriptionNotFoundException ex,
			HttpServletRequest request) {

		ErrorResponse response = ErrorResponse.builder()
				.timestamp(LocalDateTime.now())
				.status(HttpStatus.NOT_FOUND.value())
				.error(HttpStatus.NOT_FOUND.getReasonPhrase())
				.errorCode("SUBSCRIPTION_NOT_FOUND")
				.message(ex.getMessage())
				.path(request.getRequestURI())
				.build();

		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(response);
	}

	// Duplicate Subscription
	@ExceptionHandler(DuplicateSubscriptionException.class)
	public ResponseEntity<ErrorResponse> handleDuplicateSubscriptionException(
			DuplicateSubscriptionException ex,
			HttpServletRequest request) {

		ErrorResponse response = ErrorResponse.builder()
				.timestamp(LocalDateTime.now())
				.status(HttpStatus.CONFLICT.value())
				.error(HttpStatus.CONFLICT.getReasonPhrase())
				.errorCode("DUPLICATE_SUBSCRIPTION")
				.message(ex.getMessage())
				.path(request.getRequestURI())
				.build();

		return ResponseEntity.status(HttpStatus.CONFLICT)
				.body(response);
	}

	// Invalid State Transition
	@ExceptionHandler(InvalidStateTransitionException.class)
	public ResponseEntity<ErrorResponse> handleInvalidStateTransitionException(
			InvalidStateTransitionException ex,
			HttpServletRequest request) {

		ErrorResponse response = ErrorResponse.builder()
				.timestamp(LocalDateTime.now())
				.status(HttpStatus.BAD_REQUEST.value())
				.error(HttpStatus.BAD_REQUEST.getReasonPhrase())
				.errorCode("INVALID_STATE_TRANSITION")
				.message(ex.getMessage())
				.path(request.getRequestURI())
				.build();

		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(response);
	}

	// Validation Error
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidationException(
			MethodArgumentNotValidException ex) {

		FieldError fieldError = ex.getBindingResult().getFieldError();

		String message = fieldError != null
				? fieldError.getDefaultMessage()
				: "Validation failed";

		ErrorResponse response = ErrorResponse.builder()
				.status(HttpStatus.BAD_REQUEST.value())
				.error(HttpStatus.BAD_REQUEST.getReasonPhrase())
				.errorCode("VALIDATION_ERROR")
				.message(message)
				.build();

		return ResponseEntity.badRequest().body(response);
	}

	// Invalid Request Body
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(
			HttpMessageNotReadableException ex) {

		ErrorResponse response = ErrorResponse.builder()
				.status(HttpStatus.BAD_REQUEST.value())
				.error(HttpStatus.BAD_REQUEST.getReasonPhrase())
				.errorCode("INVALID_JSON")
				.message("Malformed JSON request")
				.build();

		return ResponseEntity.badRequest().body(response);
	}

	// Invalid Path Variable
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatch(
			MethodArgumentTypeMismatchException ex) {

		ErrorResponse response = ErrorResponse.builder()
				.status(HttpStatus.BAD_REQUEST.value())
				.error(HttpStatus.BAD_REQUEST.getReasonPhrase())
				.errorCode("INVALID_PARAMETER")
				.message(ex.getName() + " must be of type "
						+ ex.getRequiredType().getSimpleName())
				.build();

		return ResponseEntity.badRequest().body(response);
	}

	// Invalid Request
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
			IllegalArgumentException ex) {

		ErrorResponse response = ErrorResponse.builder()
				.status(HttpStatus.BAD_REQUEST.value())
				.error(HttpStatus.BAD_REQUEST.getReasonPhrase())
				.errorCode("INVALID_REQUEST")
				.message(ex.getMessage())
				.build();

		return ResponseEntity.badRequest().body(response);
	}

	// Method Not Allowed
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<ErrorResponse> handleMethodNotSupported(
			HttpRequestMethodNotSupportedException ex) {

		ErrorResponse response = ErrorResponse.builder()
				.status(HttpStatus.METHOD_NOT_ALLOWED.value())
				.error(HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase())
				.errorCode("METHOD_NOT_ALLOWED")
				.message(ex.getMessage())
				.build();

		return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
				.body(response);
	}

	// Endpoint Not Found
	@ExceptionHandler(NoHandlerFoundException.class)
	public ResponseEntity<ErrorResponse> handleNoHandlerFoundException(
			NoHandlerFoundException ex,
			HttpServletRequest request) {

		ErrorResponse response = ErrorResponse.builder()
				.timestamp(LocalDateTime.now())
				.status(HttpStatus.NOT_FOUND.value())
				.error(HttpStatus.NOT_FOUND.getReasonPhrase())
				.errorCode("ENDPOINT_NOT_FOUND")
				.message("Endpoint not found: " + request.getRequestURI())
				.path(request.getRequestURI())
				.build();

		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(response);
	}

	// Generic Exception
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleException(
			Exception ex,
			HttpServletRequest request) {

		log.error("Unexpected exception occurred", ex);

		ErrorResponse response = ErrorResponse.builder()
				.timestamp(LocalDateTime.now())
				.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
				.errorCode("INTERNAL_SERVER_ERROR")
				.message(ex.getMessage())
				.path(request.getRequestURI())
				.build();

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(response);
	}
}