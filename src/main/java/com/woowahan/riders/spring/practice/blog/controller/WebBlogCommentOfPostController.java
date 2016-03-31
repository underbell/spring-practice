package com.woowahan.riders.spring.practice.blog.controller;

import com.woowahan.riders.spring.practice.blog.controller.dto.CommentRequest;
import com.woowahan.riders.spring.practice.blog.service.CommentOfPostService;
import com.woowahan.riders.spring.practice.blog.service.ex.NotFoundCommentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by leejaeil on 2016. 3. 22..
 */
@Controller
@RequestMapping("{endpoint}/posts/{postId}/comments")
public class WebBlogCommentOfPostController {
    static final Logger logger = LoggerFactory.getLogger(WebBlogCommentOfPostController.class);
    @Autowired
    private CommentOfPostService commentOfPostService;


    @RequestMapping(value = "form", method = RequestMethod.PUT)
    public RedirectView updateComment(
            @PathVariable("endpoint") String endpoint,
            CommentRequest comment) throws NotFoundCommentException {
        logger.debug("updateComment:{}", comment);
        return commentOfPostService.updateComment(comment.getId(), comment.getContent())
                .map(writtenComment -> writtenComment.getPostId())
                .map(_postId -> "../../" + _postId)
                .map(RedirectView::new)
                .orElseThrow(NotFoundCommentException::new);
    }

    /**
     * Post 에 대한 Comment 등록.
     *
     * @param endpoint
     * @param postId
     * @param comment
     * @return
     */
    @SuppressWarnings("UnusedParameters")
    @RequestMapping(value = "form", method = RequestMethod.POST)
    public RedirectView writeComment(
            @PathVariable("endpoint") String endpoint,
            @PathVariable("postId") Long postId,
            CommentRequest comment,
            HttpServletRequest request) {
        logger.debug("writeComment:{}", comment);
        return commentOfPostService.writeComment(postId, comment.getContent())
                .map(writtenComment -> writtenComment.getPostId())
                .map(_postId -> "../../" + _postId)
                .map(RedirectView::new)
                .orElseThrow(NotFoundCommentException::new);
    }
}
