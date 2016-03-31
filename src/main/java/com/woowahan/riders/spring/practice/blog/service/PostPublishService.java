package com.woowahan.riders.spring.practice.blog.service;

import com.woowahan.riders.spring.practice.blog.domain.Writer;
import com.woowahan.riders.spring.practice.blog.service.dto.PostResponse;

import java.util.Optional;

/**
 * Created by leejaeil on 2016. 3. 15..
 */

/**
 * 블로그 포스트 등록
 */
public interface PostPublishService {
    /**
     * 블로그에 포스트를 등록 한다
     * @param writer
     * @param endpoint
     * @param title
     * @param content
     * @return
     */
    Optional<PostResponse> writePost(Writer writer, String endpoint, String title, String content);
}
