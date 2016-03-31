package com.woowahan.riders.spring.practice.blog.service;

import com.mysema.query.jpa.impl.JPAQuery;
import com.woowahan.riders.spring.practice.blog.domain.*;
import com.woowahan.riders.spring.practice.blog.repository.CommentRepository;
import com.woowahan.riders.spring.practice.blog.repository.PostRepository;
import com.woowahan.riders.spring.practice.blog.repository.SiteRepository;
import com.woowahan.riders.spring.practice.blog.service.dto.CommentResponse;
import com.woowahan.riders.spring.practice.blog.service.dto.CommentsResponse;
import com.woowahan.riders.spring.practice.blog.service.dto.PostResponse;
import com.woowahan.riders.spring.practice.blog.service.dto.PostsResponse;
import com.woowahan.riders.spring.practice.blog.service.ex.NotFoundPostException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by leejaeil on 2016. 3. 15..
 */
@Service
@Transactional
public class SimpleBlogService implements
        PostPublishService,
        PostSubscriptionService,
        CommentOfPostService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private SiteRepository siteRepository;
    @Autowired
    private CommentRepository commentRepository;
    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<PostResponse> writePost(Writer writer, String endpoint, String title, String content) {
        QSite site = QSite.site;
        Site endpointSite = siteRepository.findOne(site.endpoint.eq(endpoint));
        return Optional.ofNullable(PostResponse.of(postRepository.save(Post.of(writer, endpointSite, title, content))));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PostResponse> loadPost(Long id) {
        QPost post = QPost.post;
        return Optional.ofNullable(PostResponse.of(postRepository.findOne(post.id.eq(id))));
    }

    @Override
    @Transactional(readOnly = true)
    public PostsResponse loadPosts(String endpoint) {
        QPost post = QPost.post;
        QSite site = QSite.site;
        return PostsResponse.of(new JPAQuery(em)
                .from(post)
                .innerJoin(post.site, site)
                .where(site.endpoint.eq(endpoint))
                .list(post).stream().map(PostResponse::of).collect(Collectors.toList()));
    }

    @Override
    @Transactional(readOnly = true)
    public CommentsResponse loadComments(Long postId) {
        QComment comment = QComment.comment;
        QPost post = QPost.post;
        return CommentsResponse.of(new JPAQuery(em)
                .from(comment)
                .innerJoin(comment.post, post)
                .where(post.id.eq(postId))
                .list(comment));
    }

    @Override
    public Optional<CommentResponse> writeComment(Long postId, String content) throws NotFoundPostException {
        Post post = Optional.ofNullable(postRepository.findOne(postId)).orElseThrow(NotFoundPostException::new);
        return Optional.of(commentRepository.save(Comment.of(post, content))).map(CommentResponse::of);
    }

    @Override
    public Optional<CommentResponse> updateComment(Long commentId, String content) {
        Optional<Comment> optComment = Optional.ofNullable(commentRepository.findOne(commentId));
        optComment.ifPresent(comment -> comment.update(content));
        return optComment.map(CommentResponse::of);
    }
}
