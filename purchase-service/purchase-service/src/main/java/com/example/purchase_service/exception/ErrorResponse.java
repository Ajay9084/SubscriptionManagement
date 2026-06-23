package com.example.purchase_service.exception;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {

	private LocalDate timestamp;

	private  String errorCode;

	private String path;

	private int status;

	private String message;
}
