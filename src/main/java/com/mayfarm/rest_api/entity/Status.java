package com.mayfarm.rest_api.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class Status {
    private String method;
    private String path;
    private HttpStatus httpStatus;
    private int code;

    public Status(String method, String path, HttpStatus httpStatus) {
        this.method = method;
        this.path = path;
        this.httpStatus = httpStatus;
        this.code = httpStatus.value();
    }
}
