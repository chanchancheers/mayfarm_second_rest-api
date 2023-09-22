package com.mayfarm.rest_api.service;

import com.mayfarm.rest_api.dto.reponse.PostResponse;
import com.mayfarm.rest_api.dto.reponse.ReturnResponse;
import com.mayfarm.rest_api.dto.request.PostRequest;
import com.mayfarm.rest_api.entity.OrderKey;
import com.mayfarm.rest_api.entity.Pagination;
import com.mayfarm.rest_api.entity.Post;
import com.mayfarm.rest_api.exception.DataNotFoundException;
import com.mayfarm.rest_api.exception.response.ErrorResponse;
import com.mayfarm.rest_api.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.util.StringUtils.hasText;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;

    public ReturnResponse save(PostRequest request) {
        Post post = postRepository.save(request.toEntity());
        List<Post> postList = new ArrayList<Post>();
        postList.add(post);



        Map<String, Object> status = this.getStatus(HttpStatus.CREATED, "/api/posts","POST");
        ReturnResponse response = new ReturnResponse(postList, status);
        return response;
    }


    public ReturnResponse findAllWithPaging(String userInput, Integer nowPage, Integer postPerPage, String kw) {

        // Null for both Page, PostPerPage = FindAll
        if (nowPage == null && postPerPage == null){
            return this.findAllBy(userInput, kw);
        }

        int totalPost = postRepository.countAll(kw);


        // GET pagination
        Pagination pagination;

        if (totalPost == 0) {
            pagination = new Pagination();
        } else {
            pagination = new Pagination(totalPost, nowPage, postPerPage);
        }

        //GET OrderKey
        String orderKey = userInput;
        String mappingPath = "/api/posts?order_key=" + orderKey;
        if(userInput != null) {
            switch (userInput) {
                case "id":
                    userInput = OrderKey.ID.getValue();
                    break;
                case "idd":
                    userInput = OrderKey.ID_DESC.getValue();
                    break;
                case "date":
                    userInput = OrderKey.CREATED_DATE.getValue();
                    break;
                case "dated":
                    userInput = OrderKey.CREATED_DATE_DESC.getValue();
                    break;
                case "mdate":
                    userInput = OrderKey.MODIFIED_DATE.getValue();
                    break;
                case "mdated":
                    userInput = OrderKey.MODIFIED_DATE_DESC.getValue();
                    break;
                default:
                    log.error("Wrong OrderKey was Entered - User input was {}", userInput);
                    throw new IllegalArgumentException("Wrong Order Keyword was entered - Choose among these : ['id', 'idd', 'date', 'dated', 'mdate', 'mdated']");
            }
        } else {
            userInput = OrderKey.ID.getValue();
            mappingPath = "/api/posts";
        }


        List<Post> posts = postRepository.findAllWithPaging(userInput, pagination, kw);

        Map<String, Object> status;
        if(posts == null) {
//            throw new DataNotFoundException("There is no post in Database.");
            status = this.getStatus(HttpStatus.NO_CONTENT, "/api/posts", "GET");
        } else {
            status = this.getStatus(HttpStatus.OK, "/api/posts", "GET");
        }
        ReturnResponse response = new ReturnResponse(posts, status, pagination.toEntity());
        return response;
    }

    public ReturnResponse findAllBy(String userInput, String kw){
        Pagination pagination = new Pagination(postRepository.countAll(kw));

        String orderKey = userInput;
        String mappingPath = "/api/posts?order_key=" + orderKey;
        if(userInput != null) {
            switch (userInput) {
                case "id":
                    userInput = OrderKey.ID.getValue();
                    break;
                case "idd":
                    userInput = OrderKey.ID_DESC.getValue();
                    break;
                case "date":
                    userInput = OrderKey.CREATED_DATE.getValue();
                    break;
                case "dated":
                    userInput = OrderKey.CREATED_DATE_DESC.getValue();
                    break;
                case "mdate":
                    userInput = OrderKey.MODIFIED_DATE.getValue();
                    break;
                case "mdated":
                    userInput = OrderKey.MODIFIED_DATE_DESC.getValue();
                    break;
                default:
                    log.error("IllegalArgumentException for findAllBy() query parameter. Input was {}", userInput);
                    throw new IllegalArgumentException("Wrong Order Keyword was entered - Choose among these : ['id', 'idd', 'date', 'dated', 'mdate', 'mdated']");
            }
        } else {
            userInput = OrderKey.ID.getValue();
            mappingPath = "/api/posts";
        }

        List<Post> posts = postRepository.findAllBy(userInput, kw);
        Map<String, Object> status;
        if(posts == null) {
//            throw new DataNotFoundException("There is no post in Database.");
            status = this.getStatus(HttpStatus.NO_CONTENT, "/api/posts", "GET");
        } else {
            status = this.getStatus(HttpStatus.OK, "/api/posts", "GET");
        }
        ReturnResponse response = new ReturnResponse(posts, status, pagination.toEntityOfNull());
        return response;
    }

    public List<Post> findById(int id)  {
        List<Post> post = postRepository.findById(id);
            if (post == null) {
                log.error("Wrong Post ID Input - Post ID {} is Not in Database.", id);
                throw new NullPointerException("Cannot find Post you are looking for in Database - WRONG POST ID  : " + id);

            }
        return post;
    }

    public ReturnResponse getPostById(int id) {
        List<Post> post = this.findById(id);

        Map<String, Object> status = this.getStatus(HttpStatus.OK, "/api/posts/"+id,"GET");

        ReturnResponse response = new ReturnResponse(post, status);

        return response;
    }


    public ReturnResponse modifyById(int id, PostRequest request) {

        List<Post> post = this.findById(id);

        Map<String, Object> status;
        if( !hasText(request.getContent()) &&  !hasText(request.getSubject())){
            status = this.getStatus(HttpStatus.NO_CONTENT, "/api/posts/" + id,"PUT");
            log.info("Nothing Changed - Neither Subject or Content passed : POST ID {}", id);
            ReturnResponse response = new ReturnResponse(post, status);
            return response;
        }

        if (hasText(request.getContent())) {
            post.get(0).setContent(request.getContent());
        }

        if (hasText(request.getSubject())) {
            if (request.getSubject().length() > 50) {
                log.error("Subject length exceeds 50 Characters - Cannot Modify the Post ID : {}", id);
                throw new IllegalArgumentException("Subject must be less than 50 characters. ");
            }
            post.get(0).setSubject(request.getSubject());
        }

        post.get(0).setModifiedDate(LocalDateTime.now());
        this.postRepository.modify(id, post.get(0));
        log.info("POST MODIFICATION SUCCESS : POST ID {}", id);


        status = this.getStatus(HttpStatus.OK, "/api/posts/" + id,"PUT");
        ReturnResponse response = new ReturnResponse(post, status);
        return response;

    }

    public ReturnResponse deleteById(int id){
        this.findById(id);
        this.postRepository.delete(id);
        log.info("POST DELETION SUCCESS : POST ID {}", id);

        Map<String, Object> status = this.getStatus(HttpStatus.NO_CONTENT, "/api/posts/" + id,"DELETE");
        ReturnResponse response = new ReturnResponse(null, status);
        return response;
    }

    public Map<String, Object> getStatus(HttpStatus httpStatus
            ,String mappingPath
            ,String requestMethod
    ) {
        Map<String, Object> status = new HashMap<>();
        status.put("httpStatus", httpStatus);
        status.put("code", httpStatus.value());
        status.put("method", requestMethod);
        status.put("path", mappingPath);
        return status;
    }
}
