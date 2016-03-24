package com.woowahan.riders.spring.practice.blog.service;

import com.woowahan.riders.spring.practice.blog.domain.Comment;

import java.util.List;
import java.util.Optional;

/**
 * Created by leejaeil on 2016. 3. 21..
 */
public interface CommentOfPostService {
    List<Comment> loadComments(Long postId);

    Optional<Comment> writeComment(Long postId, String content);

    Optional<Comment> updateComment(Long commentId, String content);
}