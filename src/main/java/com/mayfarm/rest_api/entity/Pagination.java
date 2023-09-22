package com.mayfarm.rest_api.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class Pagination {

    private int nowPage;
    private int totalPost;
    private int totalPage;
    private int postPerPage;
    private int startIdx = 1;
    private int endIdx;

    public Pagination() { // No post in DB.
    }

    public Pagination(int totalPost) { //For no Request of Paging.
        this.totalPost = totalPost;
    }
    public Pagination(int totalPost, Integer nowPage, Integer postPerPage) {

        if (nowPage == null) {
            nowPage = 1;
        }
        if (postPerPage == null) {
            postPerPage = 10;
        }

        this.totalPost = totalPost;
        this.nowPage = nowPage;
        this.postPerPage = postPerPage;

        if (this.nowPage < 1) {
            this.nowPage = 1;
        }
        if (this.postPerPage < 1) {
            this.postPerPage = 1;
        }


        this.totalPage = (int) Math.ceil((double) this.totalPost / this.postPerPage);


        if (this.nowPage > this.totalPage) {
            this.nowPage = this.totalPage;
        }

        //현재 페이지의 시작번호
        this.startIdx = ((this.nowPage* this.postPerPage) - (this.postPerPage-1));  //쿼리에 적용할 때는 1을 빼서 적용한다
//        endIdx = postPerPage * nowPage > totalPost ? totalPost : postPerPage * nowPage;

        //한 페이지의 첫번째 게시물 번호로부터 마지막 게시물까지의 차이(limit개념).
        this.endIdx = (this.postPerPage * this.nowPage) > this.totalPost ? ((this.postPerPage * this.nowPage) - ((this.postPerPage * this.nowPage) - this.totalPost)) % this.postPerPage : this.postPerPage;
    }

    public Map<String, Integer> toEntity() {
        Map<String, Integer> results = new HashMap<>();
        results.put("totalPosts", this.totalPost);
        results.put("postsPerPage", this.postPerPage);
        results.put("currentPage", this.nowPage);
        results.put("totalPages", this.totalPage);
        return results;
    }
    public Map<String, Integer> toEntityOfNull() {
        Map<String, Integer> results = new HashMap<>();
        results.put("totalPosts", this.totalPost);
        return results;
    }
}
