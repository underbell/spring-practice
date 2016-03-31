package com.woowahan.riders.spring.practice.blog.service.dto;

import java.util.List;

/**
 * Created by leejaeil on 2016. 3. 16..
 */
public class PostsResponse {
    private List<PostResponse> posts;
    public static PostsResponse of(List<PostResponse> posts) {
        PostsResponse response = new PostsResponse();
        response.posts = posts;
        return response;
    }

    public List<PostResponse> getPosts() {
        return posts;
    }

    public void setPosts(List<PostResponse> posts) {
        this.posts = posts;
    }

    @Override
    public String toString() {
        return "PostsResponse{" +
                "posts=" + posts +
                '}';
    }
}
