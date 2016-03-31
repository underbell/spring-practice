package com.woowahan.riders.spring.practice.blog.service.dto;

import com.woowahan.riders.spring.practice.blog.domain.Writer;

/**
 * Created by leejaeil on 2016. 3. 16..
 */
public class WriterResponse {
    private Long id;
    private String nickname;

    public static WriterResponse of(Long id, String nickname) {
        WriterResponse writer = new WriterResponse();
        writer.id = id;
        writer.nickname = nickname;
        return writer;
    }

    public static WriterResponse of(Writer writer) {
        return of(writer.getId(), writer.getNickname());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        return "WriterResponse{" +
                "id=" + id +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}
