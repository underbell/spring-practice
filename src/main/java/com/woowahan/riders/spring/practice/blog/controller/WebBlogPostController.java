package com.woowahan.riders.spring.practice.blog.controller;

import com.woowahan.riders.spring.practice.blog.service.dto.CommentsResponse;
import com.woowahan.riders.spring.practice.blog.controller.dto.PostRequest;
import com.woowahan.riders.spring.practice.blog.service.dto.PostResponse;
import com.woowahan.riders.spring.practice.blog.service.dto.PostsResponse;
import com.woowahan.riders.spring.practice.blog.domain.Writer;
import com.woowahan.riders.spring.practice.blog.service.CommentOfPostService;
import com.woowahan.riders.spring.practice.blog.service.DummyAuthenticatedService;
import com.woowahan.riders.spring.practice.blog.service.PostPublishService;
import com.woowahan.riders.spring.practice.blog.service.PostSubscriptionService;
import com.woowahan.riders.spring.practice.blog.service.ex.NotFoundPostException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created by leejaeil on 2016. 3. 17..
 */
@Controller
@RequestMapping("{endpoint}/posts")
public class WebBlogPostController {
    public static final String BLOG_POSTS_LIST = "blog/posts/list";
    public static final String BLOG_POSTS_FORM = "blog/posts/form";
    public static final String BLOG_POSTS_VIEW = "blog/posts/view";

    @Autowired
    private PostSubscriptionService postSubscriptionService;
    @Autowired
    private PostPublishService postPublishService;
    @Autowired
    private CommentOfPostService commentOfPostService;
    @Autowired
    private DummyAuthenticatedService dummyAuthenticatedService;

    @RequestMapping(method = GET)
    public String getPosts(@PathVariable("endpoint") String endpoint, Model model) {
        PostsResponse posts = postSubscriptionService.loadPosts(endpoint);
        model.addAttribute("posts", posts);
        model.addAttribute("endpoint", endpoint);
        return BLOG_POSTS_LIST;
    }

    @RequestMapping(value = "form", method = GET)
    public String getForm(@PathVariable("endpoint") String endpoint, Model model) {
        model.addAttribute("post", PostRequest.empty());
        return BLOG_POSTS_FORM;
    }

    @RequestMapping(value = "form", method = POST)
    public RedirectView write(@PathVariable("endpoint") String endpoint, PostRequest post) {
        // TODO: 인증된 Writer 정보를 사용해야 한다.
        Writer writer = dummyAuthenticatedService.getWriterBy(endpoint);
        return postPublishService.writePost(writer, endpoint, post.getTitle(), post.getContent())
                .map(preset -> new RedirectView(String.valueOf(preset.getId())))
                .orElseThrow(NotFoundPostException::new);
    }

    @RequestMapping(value = "{id}", method = GET)
    public String getPost(@PathVariable("id") Long id, Model model) {
        PostResponse post = postSubscriptionService.loadPost(id).orElseThrow(NotFoundPostException::new);
        Long postId = post.getId();
        CommentsResponse comments = commentOfPostService.loadComments(postId);
        model.addAttribute("post", post);
        model.addAttribute("comments", comments);
        return BLOG_POSTS_VIEW;
    }
}