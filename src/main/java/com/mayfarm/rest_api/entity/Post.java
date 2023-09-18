package com.mayfarm.rest_api.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Post {
    //When being created
    private int id;
    private String subject;
    private String content;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;


    //When being called

    @Builder
    public Post(String subject, String content) {
        this.subject = subject;
        this.content = content;
    }
}
