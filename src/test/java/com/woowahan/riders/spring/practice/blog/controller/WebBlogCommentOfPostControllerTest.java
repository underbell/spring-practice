package com.woowahan.riders.spring.practice.blog.controller;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.woowahan.riders.spring.practice.SpringPracticeApplication;
import com.woowahan.riders.spring.practice.blog.domain.Comment;
import com.woowahan.riders.spring.practice.blog.domain.Post;
import com.woowahan.riders.spring.practice.blog.domain.QComment;
import com.woowahan.riders.spring.practice.blog.domain.Writer;
import com.woowahan.riders.spring.practice.blog.repository.CommentRepository;
import com.woowahan.riders.spring.practice.blog.service.CommentOfPostService;
import com.woowahan.riders.spring.practice.blog.service.DummyAuthenticatedService;
import com.woowahan.riders.spring.practice.blog.service.PostPublishService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by leejaeil on 2016. 3. 22..
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringPracticeApplication.class)
@WebAppConfiguration
@Transactional
public class WebBlogCommentOfPostControllerTest {
    @Autowired
    WebApplicationContext wac;
    @Autowired
    DummyAuthenticatedService dummyAuthenticatedService;
    @Autowired
    PostPublishService postPublishService;
    @Autowired
    CommentOfPostService commentOfPostService;
    @Autowired
    CommentRepository commentRepository;
    @PersistenceContext
    EntityManager em;

    MockMvc mockMvc;
    WebClient webClient;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        webClient = MockMvcWebClientBuilder.mockMvcSetup(mockMvc).useMockMvcForHosts().build();
    }

    @Test
    public void testWriteComment() throws Exception {
        // Given
        Writer writer = dummyAuthenticatedService.getWriterBy("sonegy");
        Post post = postPublishService.writePost(writer, "sonegy", "t", "c").orElseThrow(RuntimeException::new);
        HtmlPage postPage = webClient.getPage("http://localhost/sonegy/posts/" + post.getId());
        HtmlForm form = postPage.getHtmlElementById("commentForm");
        DomNode textarea = form.querySelector("textarea");
        HtmlSubmitInput submit = form.getOneHtmlElementByAttribute("input", "type", "submit");
        // When
        textarea.setTextContent("comment1");
        HtmlPage click = submit.click();
        String path = click.getUrl().getPath();
        // Then
        assertThat(path, is("/sonegy/posts/" + post.getId()));
        List<Comment> comments = new ArrayList<>();
        commentRepository.findAll(QComment.comment.post.eq(post)).forEach(comments::add);
        assertThat(comments.size(), is(1));
        assertThat(comments.stream().findFirst().get().getContent(), is("comment1"));
    }

    @Test
    public void testUpdateComment() throws Exception {
        // Given
        Writer writer = dummyAuthenticatedService.getWriterBy("sonegy");
        Post post = postPublishService.writePost(writer, "sonegy", "t", "c").orElseThrow(RuntimeException::new);
        Comment comment = commentOfPostService.writeComment(post.getId(), "comment1").get();
        HtmlPage postPage = webClient.getPage("http://localhost/sonegy/posts/" + post.getId());
        // When
        HtmlElement commentsEl = postPage.getHtmlElementById("comments");
        HtmlForm form = commentsEl.querySelector("form");
        form.getTextAreaByName("content").setText("updated comment");
        HtmlSubmitInput submit = form.getOneHtmlElementByAttribute("input", "type", "submit");
        submit.click();
        // Then
    }
}