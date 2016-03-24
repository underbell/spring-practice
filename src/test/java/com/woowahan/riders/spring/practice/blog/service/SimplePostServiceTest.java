package com.woowahan.riders.spring.practice.blog.service;

import com.woowahan.riders.spring.practice.SpringPracticeApplication;
import com.woowahan.riders.spring.practice.blog.domain.Comment;
import com.woowahan.riders.spring.practice.blog.domain.Post;
import com.woowahan.riders.spring.practice.blog.domain.Site;
import com.woowahan.riders.spring.practice.blog.domain.Writer;
import com.woowahan.riders.spring.practice.blog.repository.CommentRepository;
import com.woowahan.riders.spring.practice.blog.repository.PostRepository;
import com.woowahan.riders.spring.practice.blog.repository.SiteRepository;
import com.woowahan.riders.spring.practice.blog.repository.WriterRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by leejaeil on 2016. 3. 15..
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {SpringPracticeApplication.class})
@WebAppConfiguration
@Transactional
public class SimplePostServiceTest {
    PostPublishService postPublishService;
    PostSubscriptionService postSubscriptionService;
    CommentOfPostService commentOfPostService;
    WriterRepository writerRepository;
    SiteRepository siteRepository;
    PostRepository postRepository;
    CommentRepository commentRepository;

    @Autowired
    public void init(PostPublishService publishService,
                     PostSubscriptionService postSubscriptionService,
                     CommentOfPostService commentOfPostService,
                     WriterRepository writerRepository,
                     SiteRepository siteRepository,
                     PostRepository postRepository,
                     CommentRepository commentRepository) {
        this.postPublishService = publishService;
        this.postSubscriptionService = postSubscriptionService;
        this.commentOfPostService = commentOfPostService;
        this.writerRepository = writerRepository;
        this.siteRepository = siteRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    @Test
    public void testWritePost() throws Exception {
        // Given
        Writer writer = writerRepository.save(new Writer());
        Site site = siteRepository.save(Site.of(writer, "sonegy"));
        String title = "title";
        String content = "content";
        // When
        Optional<Post> postOptional = postPublishService.writePost(writer, site.getEndpoint(), title, content);
        // Then
        assertTrue(postOptional.isPresent());
        postOptional.ifPresent(post -> {
            assertThat(post.getTitle(), is(title));
            assertThat(post.getContent(), is(content));
            assertThat(post.getWriter(), is(writer));
            assertThat(post.getSite(), is(site));
        });
    }

    @Test
    public void testReadOne() throws Exception {
        // Given
        Writer writer = writerRepository.save(new Writer());
        Site site = siteRepository.save(Site.of(writer, "sonegy"));
        String title = "title";
        String content = "content";
        Post post = postRepository.save(Post.of(writer, site, title, content));
        Long postId = post.getId();
        Assert.notNull(postId);
        // When
        Optional<Post> postOptional = postSubscriptionService.readOne(postId);
        // Then
        assertTrue(postOptional.isPresent());
        postOptional.ifPresent(present -> {
            assertThat(present.getTitle(), is(title));
            assertThat(present.getContent(), is(content));
            assertThat(present.getSite(), is(site));
            assertThat(present.getWriter(), is(writer));
        });
    }

    @Test
    public void testReadAll() throws Exception {
        // Given
        Writer writer = writerRepository.save(new Writer());
        String endpoint = "sonegy";
        Site sonegy = siteRepository.save(Site.of(writer, endpoint));
        postRepository.save(Arrays.asList(
                Post.of(writer, sonegy, "title", "content"),
                Post.of(writer, sonegy, "title", "content"),
                Post.of(writer, sonegy, "title", "content"),
                Post.of(writer, sonegy, "title", "content")
        ));
        // When
        List<Post> posts = postSubscriptionService.readAll(endpoint);
        // Then
        assertThat(posts.size(), is(4));
    }

    @Test
    public void testLoadComments() throws Exception {
        // Given
        Writer writer = writerRepository.save(new Writer());
        String endpoint = "sonegy";
        Site sonegy = siteRepository.save(Site.of(writer, endpoint));
        Post post = postRepository.save(
                Post.of(writer, sonegy, "title", "content")
        );
        commentRepository.save(Arrays.asList(
                Comment.of(post, "comment1"),
                Comment.of(post, "comment2")
        ));
        // When
        List<Comment> comments = commentOfPostService.loadComments(post.getId());
        // Then
        assertThat(comments.size(), is(2));
        assertThat(comments.get(0).getContent(), is("comment1"));
        assertThat(comments.get(1).getContent(), is("comment2"));
    }

    @Test
    public void testSaveComment() throws Exception {
        // Given
        Writer writer = writerRepository.save(new Writer());
        String endpoint = "sonegy";
        Site sonegy = siteRepository.save(Site.of(writer, endpoint));
        Post post = postRepository.save(
                Post.of(writer, sonegy, "title", "content")
        );
        // When
        Optional<Comment> optional = commentOfPostService.writeComment(post.getId(), "comment1");
        // Then
        assertTrue(optional.isPresent());
        optional.ifPresent(comment -> {
            assertThat(comment.getContent(), is("comment1"));
            assertThat(comment.getPost(), is(post));
        });
    }

    @Test
    public void testUpdateComment() throws Exception {
        // Given
        Writer writer = writerRepository.save(new Writer());
        String endpoint = "sonegy";
        Site sonegy = siteRepository.save(Site.of(writer, endpoint));
        Post post = postRepository.save(
                Post.of(writer, sonegy, "title", "content")
        );
        Comment comment = commentRepository.save(Comment.of(post, "comment1"));
        // When
        Optional<Comment> optional = commentOfPostService.updateComment(comment.getId(), "updated");
        // Then
        assertTrue(optional.isPresent());
        optional.ifPresent(updatedComment -> {
            assertThat(updatedComment.getContent(), is("updated"));
            assertThat(updatedComment.getPost(), is(post));
        });
    }
}