package com.mayfarm.rest_api.service;

import com.mayfarm.rest_api.dto.request.PostRequest;
import com.mayfarm.rest_api.entity.Post;
import com.mayfarm.rest_api.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;

    public Post save(PostRequest request) {
        return postRepository.save(request.toEntity());
    }

    public Post findById(int id)  {
        Post post = postRepository.findById(id);
            if (post == null) {
                throw new IllegalArgumentException("Cannot find Post you are looking for in Database - WRONG POST ID  : " + id);
            }
        return post;

    }
}
