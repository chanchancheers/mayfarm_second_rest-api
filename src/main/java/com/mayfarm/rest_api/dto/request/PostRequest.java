package com.mayfarm.rest_api.dto.request;

import com.mayfarm.rest_api.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostRequest {

    @NotBlank(message = "Neither blank or Null allowed for Post subject.")
    @Size(max=50, message = "Subject must be less than 50 characters.")
    private String subject;

    @NotBlank(message = "Neither blank or Null allowed for Post content.")
    private String content;


    public Post toEntity() {
        return Post.builder()
                .subject(subject)
                .content(content)
                .build();
    }
}
