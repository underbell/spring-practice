package com.woowahan.riders.spring.practice.blog.service;

import com.mysema.query.jpa.impl.JPAQuery;
import com.woowahan.riders.spring.practice.blog.domain.*;
import com.woowahan.riders.spring.practice.blog.repository.CommentRepository;
import com.woowahan.riders.spring.practice.blog.repository.PostRepository;
import com.woowahan.riders.spring.practice.blog.repository.SiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

/**
 * Created by leejaeil on 2016. 3. 15..
 */
@Service
@Transactional
public class SimplePostService implements
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
    public Optional<Post> writePost(Writer writer, String endpoint, String title, String content) {
        QSite site = QSite.site;
        Site endpointSite = siteRepository.findOne(site.endpoint.eq(endpoint));
        return Optional.ofNullable(postRepository.save(Post.of(writer, endpointSite, title, content)));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Post> readOne(Long id) {
        QPost post = QPost.post;
        return Optional.ofNullable(postRepository.findOne(post.id.eq(id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Post> readAll(String endpoint) {
        QPost post = QPost.post;
        QSite site = QSite.site;
        return new JPAQuery(em)
                .from(post)
                .innerJoin(post.site, site)
                .where(site.endpoint.eq(endpoint))
                .list(post);
    }

    @Override
    public List<Comment> loadComments(Long postId) {
        QComment comment = QComment.comment;
        QPost post = QPost.post;
        return new JPAQuery(em)
                .from(comment)
                .innerJoin(comment.post, post)
                .where(post.id.eq(postId))
                .list(comment);
    }

    @Override
    public Optional<Comment> writeComment(Long postId, String content) {
        return Optional.ofNullable(commentRepository.save(
                Comment.of(Optional.of(postRepository.findOne(postId)).get(), content))
        );
    }

    @Override
    public Optional<Comment> updateComment(Long commentId, String content) {
        Optional<Comment> commentOptional = Optional.ofNullable(commentRepository.findOne(commentId));
        commentOptional.ifPresent(comment -> comment.update(content));
        return commentOptional;
    }
}
