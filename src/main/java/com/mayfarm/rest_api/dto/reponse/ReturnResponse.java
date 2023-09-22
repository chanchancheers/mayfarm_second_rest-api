package com.mayfarm.rest_api.dto.reponse;

import com.mayfarm.rest_api.entity.Pagination;
import com.mayfarm.rest_api.entity.Post;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class ReturnResponse {
    private final List<Post> posts;
    private final Map<String, Object> status;
    private Map<String, Integer> pagination;
//    private final List<Integer> paging;


    public ReturnResponse(List<Post> posts
                          ,Map<String, Object> status
//                          ,List<Integer> paging
                            ) {
        this.posts = posts;
        this.status = status;
//        this.paging = paging;
    }
    public ReturnResponse(List<Post> posts
                            ,Map<String, Object> status
                          ,Map<String, Integer> pagination){
        this.posts = posts;
        this.status = status;
        this.pagination = pagination;
    }

    public Map<String, Object> toEntity(){
        Map<String, Object> results = new HashMap<>();
        results.put("post", this.posts);
        results.put("status", this.status);

        return results;
    }
    public Map<String, Object> toEntityWithPaging(){
        Map<String, Object> results = new HashMap<>();
        results.put("post", this.posts);
        results.put("status", this.status);
        results.put("pagination", this.pagination);

        return results;
    }
}
