package com.mayfarm.rest_api.exception.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class ErrorResponse {
    private int code;
    private HttpStatus httpStatus;
    private LocalDateTime occurTime;
    private String errorName;
    private String errorMessage;
//    private String path;

}
