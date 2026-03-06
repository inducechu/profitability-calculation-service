package com.induce.profitability_calculation_service.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.induce.profitability_calculation_service.dto.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(UserAlreadyExistsException.class)
  public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException e) {
    ErrorResponse error = ErrorResponse.builder()
        .errorCode("EMAIL_TAKEN")
        .message(e.getMessage())
        .build();
    return ResponseEntity.status(409).body(error);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException e) {
    String errorMessage = e.getBindingResult()
        .getFieldErrors()
        .get(0)
        .getDefaultMessage();

    ErrorResponse error = ErrorResponse.builder()
        .errorCode("VALIDATION_ERROR")
        .message(errorMessage)
        .build();

    return ResponseEntity.badRequest().body(error);
  }
}
