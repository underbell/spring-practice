package com.woowahan.riders.spring.practice.blog.service;

import com.woowahan.riders.spring.practice.blog.service.dto.PostResponse;
import com.woowahan.riders.spring.practice.blog.service.dto.PostsResponse;

import java.util.Optional;

/**
 * 블로그 포스트 서비스
 * Created by leejaeil on 2016. 3. 15..
 */
public interface PostSubscriptionService {

    /**
     * 하나의 포스트를 로드한다.
     * @param postId
     * @return
     */
    Optional<PostResponse> loadPost(Long postId);

    /**
     * 조건에 맞는 포스트 목록을 반환한다
     * @param endpoint
     * @return
     */
    PostsResponse loadPosts(String endpoint);
}
