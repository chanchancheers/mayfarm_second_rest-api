package com.mayfarm.rest_api.exception.controller;

import com.mayfarm.rest_api.exception.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

;import java.time.LocalDateTime;


@RestControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> IllegalArgumentExceptionHandler(IllegalArgumentException e) {

        ErrorResponse errorResponse = new ErrorResponse();

        errorResponse.setErrorName(IllegalArgumentException.class.getName());
        errorResponse.setErrorMessage(e.getMessage());
        errorResponse.setHttpStatus(HttpStatus.NOT_FOUND);
        errorResponse.setCode(HttpStatus.NOT_FOUND.value());
        errorResponse.setOccurTime(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
}
