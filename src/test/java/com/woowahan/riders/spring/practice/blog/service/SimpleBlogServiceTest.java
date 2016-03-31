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
import com.woowahan.riders.spring.practice.blog.service.dto.CommentResponse;
import com.woowahan.riders.spring.practice.blog.service.dto.CommentsResponse;
import com.woowahan.riders.spring.practice.blog.service.dto.PostResponse;
import com.woowahan.riders.spring.practice.blog.service.dto.PostsResponse;
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
public class SimpleBlogServiceTest {
    @Autowired
    PostPublishService postPublishService;
    @Autowired
    PostSubscriptionService postSubscriptionService;
    @Autowired
    CommentOfPostService commentOfPostService;
    @Autowired
    WriterRepository writerRepository;
    @Autowired
    SiteRepository siteRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    CommentRepository commentRepository;

    @Test
    public void testWritePost() throws Exception {
        // Given
        Writer writer = writerRepository.save(new Writer());
        Site site = siteRepository.save(Site.of(writer, "sonegy"));
        String title = "title";
        String content = "content";
        // When
        Optional<PostResponse> optPost = postPublishService.writePost(writer, site.getEndpoint(), title, content);
        // Then
        assertTrue(optPost.isPresent());
        optPost.ifPresent(post -> {
            assertThat(post.getTitle(), is(title));
            assertThat(post.getContent(), is(content));
            assertThat(post.getWriter().getId(), is(writer.getId()));
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
        Optional<PostResponse> optPost = postSubscriptionService.loadPost(postId);
        // Then
        assertTrue(optPost.isPresent());
        optPost.ifPresent(present -> {
            assertThat(present.getTitle(), is(title));
            assertThat(present.getContent(), is(content));
            assertThat(present.getWriter().getId(), is(writer.getId()));
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
        PostsResponse posts = postSubscriptionService.loadPosts(endpoint);
        // Then
        assertThat(posts.getPosts().size(), is(4));
    }

    @Test
    public void 특정포스트에대한댓글목록을반환() throws Exception {
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
        CommentsResponse comments = commentOfPostService.loadComments(post.getId());
        // Then
        assertThat(comments.getComments().size(), is(2));
        assertThat(comments.getComments().get(0).getContent(), is("comment1"));
        assertThat(comments.getComments().get(1).getContent(), is("comment2"));
    }

    @Test
    public void 특정포스트에제목과내용을입력하여등록() throws Exception {
        // Given
        Writer writer = writerRepository.save(new Writer());
        String endpoint = "sonegy";
        Site sonegy = siteRepository.save(Site.of(writer, endpoint));
        Post post = postRepository.save(
                Post.of(writer, sonegy, "title", "content")
        );
        // When
        Optional<CommentResponse> optComment = commentOfPostService.writeComment(post.getId(), "comment1");
        // Then
        assertTrue(optComment.isPresent());
        optComment.ifPresent(comment -> {
            assertThat(comment.getContent(), is("comment1"));
            assertThat(comment.getPostId(), is(post.getId()));
        });
    }

    @Test
    public void 특정포스트에대한댓글을수정후확인() throws Exception {
        // Given
        Writer writer = writerRepository.save(new Writer());
        String endpoint = "sonegy";
        Site sonegy = siteRepository.save(Site.of(writer, endpoint));
        Post post = postRepository.save(
                Post.of(writer, sonegy, "title", "content")
        );
        Comment comment = commentRepository.save(Comment.of(post, "comment1"));
        // When
        Optional<CommentResponse> optComment = commentOfPostService.updateComment(comment.getId(), "updated");
        // Then
        assertTrue(optComment.isPresent());
        optComment.ifPresent(updatedComment -> {
            assertThat(updatedComment.getContent(), is("updated"));
            assertThat(updatedComment.getPostId(), is(post.getId()));
        });
    }
}