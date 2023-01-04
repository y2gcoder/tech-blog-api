package com.y2gcoder.blog.post.application.service;

import com.y2gcoder.blog.post.domain.Post;
import com.y2gcoder.blog.post.domain.Post.PostId;
import java.time.LocalDateTime;

public interface PostRepository {

    Post savePost(String title, String content, LocalDateTime writtenAt);

    Post getById(PostId postId);

    void deleteByPostId(PostId postId);

}
