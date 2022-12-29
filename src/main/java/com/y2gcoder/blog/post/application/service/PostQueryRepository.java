package com.y2gcoder.blog.post.application.service;

import com.y2gcoder.blog.post.domain.Post;
import java.util.List;

public interface PostQueryRepository {

    List<Post> findAll();

}
