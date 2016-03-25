package com.woowahan.riders.spring.practice.blog.controller.dto;

/**
 * Created by leejaeil on 2016. 3. 22..
 */
public class CommentRequest {
    private Long id; // commentId
    private Long postId; // postId
    private String content;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
