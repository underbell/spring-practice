package com.woowahan.riders.spring.practice.blog.service.dto;

import com.woowahan.riders.spring.practice.blog.domain.Comment;

/**
 * Created by leejaeil on 2016. 3. 31..
 */
public class CommentResponse {
    private Long id;
    private Long postId;
    private String content;

    private CommentResponse(Comment comment) {
        id = comment.getId();
        postId = comment.getPost().getId();
        content = comment.getContent();
    }

    public static CommentResponse of(Comment comment) {
        return new CommentResponse(comment);
    }

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
