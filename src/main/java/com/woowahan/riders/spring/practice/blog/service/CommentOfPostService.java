package com.woowahan.riders.spring.practice.blog.service;

import com.woowahan.riders.spring.practice.blog.service.dto.CommentResponse;
import com.woowahan.riders.spring.practice.blog.service.dto.CommentsResponse;
import com.woowahan.riders.spring.practice.blog.service.ex.NotFoundCommentException;
import com.woowahan.riders.spring.practice.blog.service.ex.NotFoundPostException;

import java.util.Optional;

/**
 * 블로그 댓글 기능
 * Created by leejaeil on 2016. 3. 21..
 */
public interface CommentOfPostService {
    /**
     * 블로그 Post하나의 대한 댓글 목록을 반환한다.
     * @param postId
     * @return
     */
    CommentsResponse loadComments(Long postId);

    /**
     * 블로그 Post에 댓글을 작성한다.
     * @param postId
     * @param content
     * @return
     * @throws NotFoundPostException
     */
    Optional<CommentResponse> writeComment(Long postId, String content) throws NotFoundPostException;

    /**
     * 댓글 하나를 수정한다.
     * @param commentId
     * @param content
     * @return
     */
    Optional<CommentResponse> updateComment(Long commentId, String content);
}