package com.woowahan.riders.spring.practice.blog.domain;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by leejaeil on 2016. 3. 14..
 */
@Entity
public class Comment {
    @Id
    @GeneratedValue
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    private Post post;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDateTime;

    public static Comment of(Post post, String content) {
        Comment comment = new Comment();
        comment.post = post;
        comment.content = content;
        comment.createdDateTime = new Date();
        post.appendComment(comment);
        return comment;
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public Post getPost() {
        return post;
    }

    public Date getCreatedDateTime() {
        return createdDateTime;
    }

    public Comment update(String content) {
        this.content = content;
        return this;
    }
}
