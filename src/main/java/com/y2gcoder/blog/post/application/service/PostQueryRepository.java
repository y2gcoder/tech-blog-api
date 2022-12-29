package com.y2gcoder.blog.post.application.service;

import com.y2gcoder.blog.post.domain.Post;
import java.util.List;
import java.util.Optional;

public interface PostQueryRepository {

    List<Post> findAll();

    Optional<Post> findById(Long postId);

    Post getById(Long postId);
}
