package com.woowahan.riders.spring.practice.blog.controller.dto;

import com.woowahan.riders.spring.practice.blog.domain.Comment;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by leejaeil on 2016. 3. 22..
 */
public class CommentsResponse {
    private List<CommentResponse> comments;

    public static CommentsResponse of(List<Comment> comments) {
        CommentsResponse response = new CommentsResponse();
        response.comments = comments.stream()
                .map(CommentResponse::new)
                .collect(Collectors.toList());
        return response;
    }

    public List<CommentResponse> getComments() {
        return comments;
    }

    public static class CommentResponse {
        private Long id;
        private Long postId;
        private String content;

        public CommentResponse(Comment comment) {
            id = comment.getId();
            postId = comment.getPost().getId();
            content = comment.getContent();
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
}
