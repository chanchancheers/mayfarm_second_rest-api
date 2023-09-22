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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.xml.ws.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService postService;

    @GetMapping("/api/posts")
    public ResponseEntity<Map<String, Object>> allPostsWithPaging(@RequestParam(value="order_key", required = false) String orderKey,
                                                                  @RequestParam(value="page", required = false) Integer page,
                                                                  @RequestParam(value="postPerPage", required = false) Integer postPerPage,
                                                                  @RequestParam(value="kw", defaultValue = "") String kw,
                                                                  HttpServletRequest servletRequest){
        servletRequest.getPathInfo();
        return ResponseEntity.ok()
                .body(postService.findAllWithPaging(orderKey,page,postPerPage, kw).toEntityWithPaging());
    }


    @PostMapping("/api/posts")
    public ResponseEntity<Map<String,Object>> addPost(@Valid  @RequestBody PostRequest request){

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(postService.save(request).toEntity());
    }

    @GetMapping("/api/posts/{id}")
    public ResponseEntity<Map<String,Object>> findPost(@PathVariable("id") int id) {
        return ResponseEntity.ok()
                .body(postService.getPostById(id).toEntity());
    }


    @PutMapping("/api/posts/{id}")
    public ResponseEntity<Map<String,Object>> modifyPost(@PathVariable("id") int id,
                                                   @RequestBody PostRequest request) {
               return ResponseEntity.ok()
                .body(postService.modifyById(id,request).toEntity());
    }

    @DeleteMapping("/api/posts/{id}")
    public ResponseEntity<Map<String,Object>> deletePost(@PathVariable("id") int id){
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(postService.deleteById(id).toEntity());

    }

}
