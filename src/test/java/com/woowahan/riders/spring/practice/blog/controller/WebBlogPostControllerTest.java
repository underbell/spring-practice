package com.woowahan.riders.spring.practice.blog.controller;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.woowahan.riders.spring.practice.SpringPracticeApplication;
import com.woowahan.riders.spring.practice.blog.domain.Post;
import com.woowahan.riders.spring.practice.blog.domain.Writer;
import com.woowahan.riders.spring.practice.blog.service.CommentOfPostService;
import com.woowahan.riders.spring.practice.blog.service.DummyAuthenticatedService;
import com.woowahan.riders.spring.practice.blog.service.PostPublishService;
import com.woowahan.riders.spring.practice.blog.service.PostSubscriptionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * Created by leejaeil on 2016. 3. 17..
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringPracticeApplication.class)
@WebAppConfiguration
@Sql(value = "WebBlogPostControllerTest.sql")
@Transactional
public class WebBlogPostControllerTest {
    @Autowired
    WebApplicationContext wac;
    @Autowired
    PostSubscriptionService postSubscriptionService;
    @Autowired
    PostPublishService postPublishService;
    @Autowired
    CommentOfPostService commentOfPostService;
    @Autowired
    DummyAuthenticatedService dummyAuthenticatedService;

    MockMvc mockMvc;
    WebClient webClient;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        webClient = MockMvcWebClientBuilder.mockMvcSetup(mockMvc).useMockMvcForHosts().build();
    }

    @Test
    public void testGetPosts() throws Exception {
        // Given
        // When
        HtmlPage postsPage = webClient.getPage("http://localhost/sonegy/posts");
        DomElement posts = postsPage.getElementById("posts");
        // Then
        assertNotNull(posts);
        DomNodeList<DomNode> li = posts.querySelectorAll("li a");
        assertThat(li.get(0).getTextContent(), is("title1"));
        assertThat(li.get(0).getAttributes().getNamedItem("href").getTextContent(), is("/sonegy/posts/1"));
        assertThat(li.get(1).getTextContent(), is("title2"));
        assertThat(li.get(1).getAttributes().getNamedItem("href").getTextContent(), is("/sonegy/posts/2"));
        assertThat(li.size(), is(2));
    }

    @Test
    public void testGetForm() throws Exception {
        // Given
        // When
        HtmlPage createMsgFormPage = webClient.getPage("http://localhost/sonegy/posts/form");
        HtmlForm form = createMsgFormPage.getHtmlElementById("postForm");
        HtmlInput title = form.getInputByName("title");
        HtmlTextArea content = form.getTextAreaByName("content");
        HtmlSubmitInput submit = form.getOneHtmlElementByAttribute("input", "type", "submit");
        // Then
        assertThat(title, is(notNullValue()));
        assertThat(content, is(notNullValue()));
        assertThat(submit, is(notNullValue()));
    }

    @Test
    public void testWrite() throws Exception {
        // Given
        HtmlPage createMsgFormPage = webClient.getPage("http://localhost/sonegy/posts/form");
        HtmlForm form = createMsgFormPage.getHtmlElementById("postForm");
        HtmlInput title = form.getInputByName("title");
        HtmlTextArea content = form.getTextAreaByName("content");
        HtmlSubmitInput submit = form.getOneHtmlElementByAttribute("input", "type", "submit");
        // When
        title.setValueAttribute("test title");
        content.setText("test content");
        HtmlPage newMessagePage = submit.click();
        // Then
        assertThat(newMessagePage.getUrl().toString(), is(startsWith("http://localhost/sonegy/posts/")));
        Post savedPost = postSubscriptionService.readOne(
                Long.parseLong(newMessagePage.getUrl().getPath().substring("/sonegy/posts/".length())))
                .get();
        assertThat(savedPost.getTitle(), is("test title"));
        assertThat(savedPost.getContent(), is("test content"));
    }

    @Test
    public void testGetPost() throws Exception {
        // Given
        Writer writer = dummyAuthenticatedService.getWriterBy("sonegy");
        Post post = postPublishService.writePost(writer, "sonegy", "t", "c").orElseThrow(RuntimeException::new);
        commentOfPostService.writeComment(post.getId(), "comment1");
        commentOfPostService.writeComment(post.getId(), "comment2");
        // When
        HtmlPage postPage = webClient.getPage("http://localhost/sonegy/posts/" + post.getId());
        // Then
        assertThat(postPage, is(notNullValue()));
        HtmlElement postElement = postPage.getHtmlElementById("post");
        HtmlElement commentsElement = postPage.getHtmlElementById("comments");
        assertThat(postElement.querySelector("dd._title").getTextContent(), is("t"));
        assertThat(postElement.querySelector("pre._content").getTextContent(), is("c"));
        assertThat(commentsElement.querySelectorAll("li div").get(0).getTextContent(), is("comment1"));
        assertThat(commentsElement.querySelectorAll("li div").get(1).getTextContent(), is("comment2"));

    }
}