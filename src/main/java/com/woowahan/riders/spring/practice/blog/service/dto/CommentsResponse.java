package com.woowahan.riders.spring.practice.blog.service.dto;

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
                .map(CommentResponse::of)
                .collect(Collectors.toList());
        return response;
    }

    public List<CommentResponse> getComments() {
        return comments;
    }

}
