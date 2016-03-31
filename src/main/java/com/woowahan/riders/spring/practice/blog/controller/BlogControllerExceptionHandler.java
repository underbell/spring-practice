package com.woowahan.riders.spring.practice.blog.controller;

import com.woowahan.riders.spring.practice.blog.service.ex.NotFoundPostException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by leejaeil on 2016. 3. 21..
 */
public class BlogControllerExceptionHandler {

    public static class Message {
        public static final Message ERROR = new Message("00001", "정의 되지 않는 에러");
        public static final Message NOT_FOUND_POST = new Message("10001", "포스트를 찾을 수 없습니다.");

        private String code;
        private String description;

        private Message(String code, String description) {
            this.code = code;
            this.description = description;
        }

        public String getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }

        public static Message findBy(Exception e) {
            Message message = ERROR;
            if (NotFoundPostException.class.isAssignableFrom(e.getClass())) {
                message = Message.NOT_FOUND_POST;
            }
            return message;
        }
    }

    @ControllerAdvice(annotations = {RestController.class})
    public static class ApiExceptionHandler {
        @ExceptionHandler({NotFoundPostException.class})
        public ResponseEntity<Message> handleNotFound(Exception e) {
            return new ResponseEntity<>(Message.findBy(e), HttpStatus.NOT_FOUND);
        }
    }

    @ControllerAdvice(annotations = {Controller.class})
    public static class WebExceptionHandler {
        @ResponseStatus(HttpStatus.NOT_FOUND)
        @ExceptionHandler({NotFoundPostException.class})
        public String handleNotFound(Model model, Exception e) {
            model.addAttribute("message", Message.findBy(e));
            return "blog/error";
        }
    }
}
