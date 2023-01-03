package com.y2gcoder.blog.post.application.service;

import com.y2gcoder.blog.post.domain.Post.PostId;
import com.y2gcoder.blog.post.domain.PostWithTags;
import java.util.Optional;

public interface PostQueryRepository {

    Optional<PostWithTags> findById(PostId postId);

    PostWithTags getById(PostId postId);
}
