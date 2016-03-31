package com.woowahan.riders.spring.practice.blog.controller;

import com.woowahan.riders.spring.practice.blog.controller.dto.PostRequest;
import com.woowahan.riders.spring.practice.blog.service.dto.PostResponse;
import com.woowahan.riders.spring.practice.blog.service.dto.PostsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by leejaeil on 2016. 3. 16..
 */
@RestController
@RequestMapping("api/blog/posts")
public class BlogPostController {
    static final Logger logger = LoggerFactory.getLogger(BlogPostController.class);

    @RequestMapping(method = RequestMethod.GET)
    public PostsResponse getPosts() {
        List<PostResponse> posts = Arrays.asList(
                PostResponse.of(1l, "title 1", "content 1", new Date()),
                PostResponse.of(2l, "title 2", "content 3", new Date())
        );
        posts.stream().forEach(post -> logger.debug("{}", post));
        return PostsResponse.of(posts);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public PostResponse view(@PathVariable("id") Long id) {
        return PostResponse.of(id, "title 1", "content", new Date());
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Long> write(@RequestBody PostRequest post) {
        logger.debug("{}", post);
        return new ResponseEntity<>(post.getId(), HttpStatus.OK);
    }
}
