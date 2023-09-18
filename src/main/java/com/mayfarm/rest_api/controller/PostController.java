package com.mayfarm.rest_api.controller;

import com.mayfarm.rest_api.dto.reponse.PostResponse;
import com.mayfarm.rest_api.dto.request.PostRequest;
import com.mayfarm.rest_api.entity.Post;
import com.mayfarm.rest_api.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService postService;

    @PostMapping("/api/posts")
    public ResponseEntity<Post> addPost(@RequestBody PostRequest request){
        Post post = postService.save(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(post);
    }

    @GetMapping("/api/posts/{id}")
    public ResponseEntity<PostResponse> findPost(@PathVariable("id") int id) {
        Post post = postService.findById(id);
        return ResponseEntity.ok()
                .body(new PostResponse(post));
    }


}
