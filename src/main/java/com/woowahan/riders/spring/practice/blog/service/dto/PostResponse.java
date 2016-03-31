package com.woowahan.riders.spring.practice.blog.service.dto;

import com.woowahan.riders.spring.practice.blog.domain.Post;

import java.util.Date;

/**
 * Created by leejaeil on 2016. 3. 16..
 */
public class PostResponse {
    private Long id;
    private String title;
    private String content;
    private WriterResponse writer;
    private Date createdDateTime;

    public static PostResponse of(Long id, String title, String content, Date createdDateTime) {
        PostResponse post = new PostResponse();
        post.id = id;
        post.title = title;
        post.content = content;
        post.createdDateTime = createdDateTime;
        return post;
    }

    public static PostResponse of(Post entity) {
        PostResponse post = PostResponse.of(
                entity.getId(), entity.getTitle(), entity.getContent(), entity.getCreatedDateTime());
        post.writer = WriterResponse.of(entity.getWriter());
        return post;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(Date createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public WriterResponse getWriter() {
        return writer;
    }

    @Override
    public String toString() {
        return "PostResponse{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", writer=" + writer +
                ", createdDateTime=" + createdDateTime +
                '}';
    }
}
