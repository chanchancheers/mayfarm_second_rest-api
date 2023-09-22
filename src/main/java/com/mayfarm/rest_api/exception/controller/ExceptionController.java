package com.mayfarm.rest_api.exception.controller;

import com.mayfarm.rest_api.exception.DataNotFoundException;
import com.mayfarm.rest_api.exception.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

;import javax.jws.WebResult;
import javax.validation.constraints.Null;
import javax.xml.ws.Response;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@RestControllerAdvice
@Slf4j
public class ExceptionController  {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        List<ObjectError> errors = e.getBindingResult().getAllErrors();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 1; i <= errors.size(); i++) {
            stringBuilder.append(i + ".");
            stringBuilder.append(errors.get(i-1).getDefaultMessage());
            stringBuilder.append(" ");
        }
        String errorMessages = stringBuilder.toString();
        log.error("The User Violates Constraints of Creating a new post.");
        ErrorResponse errorResponse = new ErrorResponse(e, errorMessages, HttpStatus.BAD_REQUEST);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> HttpMethodException(HttpRequestMethodNotSupportedException e){
        log.error("The User requests Wrong Http Method");
        ErrorResponse errorResponse = new ErrorResponse(e, HttpStatus.METHOD_NOT_ALLOWED);
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorResponse);
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<Object> SQLExceptionHandler(SQLException e) {
        log.error("SQLException occurs. Error messages is as follows: {}", e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(e, HttpStatus.BAD_REQUEST);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> IllegalArgumentExceptionHandler(IllegalArgumentException e) {

        ErrorResponse errorResponse = new ErrorResponse(e, HttpStatus.BAD_REQUEST);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResponse> NullPointerExceptionHandler(NullPointerException e){
        ErrorResponse errorResponse = new ErrorResponse(e, HttpStatus.NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<String> noPosts(DataNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());

    }
}
