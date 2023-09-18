package com.mayfarm.rest_api.dto.request;

import com.mayfarm.rest_api.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostRequest {
    private String subject;
    private String content;


    public Post toEntity() {
        return Post.builder()
                .subject(subject)
                .content(content)
                .build();
    }
}
