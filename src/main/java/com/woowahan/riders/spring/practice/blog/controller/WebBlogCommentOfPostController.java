package com.woowahan.riders.spring.practice.blog.controller;

import com.woowahan.riders.spring.practice.blog.controller.dto.CommentRequest;
import com.woowahan.riders.spring.practice.blog.service.CommentOfPostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Created by leejaeil on 2016. 3. 22..
 */
@Controller
@RequestMapping("{endpoint}/posts/{postId}/comments")
public class WebBlogCommentOfPostController {
    static final Logger logger = LoggerFactory.getLogger(WebBlogCommentOfPostController.class);
    @Autowired
    private CommentOfPostService commentOfPostService;

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
            CommentRequest comment) {
        return commentOfPostService.writeComment(postId, comment.getContent())
                .map(writtenComment -> writtenComment.getPost())
                .map(post -> post.getId())
                .map(_postId -> "../../" + _postId)
                .map(RedirectView::new)
                .orElseThrow(RuntimeException::new);
    }

    @RequestMapping(value = "form", method = RequestMethod.PUT)
    public void updateComment(
            @PathVariable("endpoint") String endpoint,
            CommentRequest comment) {
        logger.debug("{}", comment);
        commentOfPostService.updateComment(comment.getId(), comment.getContent());
    }
}
