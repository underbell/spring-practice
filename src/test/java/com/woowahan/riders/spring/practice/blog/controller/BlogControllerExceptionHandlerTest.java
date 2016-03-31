package com.woowahan.riders.spring.practice.blog.controller;

import com.woowahan.riders.spring.practice.SpringPracticeApplication;
import com.woowahan.riders.spring.practice.blog.service.ex.NotFoundPostException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import static com.woowahan.riders.spring.practice.blog.controller.BlogControllerExceptionHandler.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by leejaeil on 2016. 3. 21..
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringPracticeApplication.class)
@WebAppConfiguration
@Transactional
public class BlogControllerExceptionHandlerTest {

    @Autowired
    WebApplicationContext wac;
    MockMvc mockMvc;

    @RestController
    @RequestMapping("apitest")
    public static class ApiTestController {

        @RequestMapping("exception")
        public void raise404() {
            throw new NotFoundPostException();
        }

        @RequestMapping("ok")
        public String raiseOk() {
            return "OK";
        }
    }

    @Controller
    @RequestMapping("webtest")
    public static class WebTestController {
        @RequestMapping("exception")
        public void raise404() {
            throw new NotFoundPostException();
        }
    }

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void testApiExceptionOk() throws Exception {
        mockMvc.perform(get("/apitest/ok")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().string("OK"))
                .andDo(print());
    }

    @Test
    public void testApiExceptionNotFound() throws Exception {
        mockMvc.perform(get("/apitest/exception")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(Message.NOT_FOUND_POST.getCode()))
                .andExpect(jsonPath("$.description").value(Message.NOT_FOUND_POST.getDescription()))
                .andDo(print());
    }

    @Test
    public void testWebExceptionNotFound() throws Exception {
        mockMvc.perform(get("/webtest/exception")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.parseMediaType("text/html;charset=UTF-8")))
                .andDo(print());
    }
}