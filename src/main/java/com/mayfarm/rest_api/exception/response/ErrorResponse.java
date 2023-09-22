package com.mayfarm.rest_api.exception.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Slf4j
public class ErrorResponse {
    private int code;
    private HttpStatus httpStatus;
    private LocalDateTime occurTime;
    private String errorName;
    private String errorMessage;
//    private String path;


    public ErrorResponse(Exception e, HttpStatus status) {
        this.code = status.value();
        this.httpStatus = status;
        this.occurTime = LocalDateTime.now();
        this.errorName = e.getClass().getName();
        this.errorMessage = e.getMessage();
        log.error(errorMessage);
    }

    public ErrorResponse(Exception e, String errorMessages, HttpStatus status){
        this.code = status.value();
        this.httpStatus = status;
        this.occurTime = LocalDateTime.now();
        this.errorName = e.getClass().getName();
        this.errorMessage = errorMessages;
        log.error(errorMessage);

    }
}
