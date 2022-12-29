package com.y2gcoder.blog.post.application.service;

import com.y2gcoder.blog.post.domain.Post;
import com.y2gcoder.blog.post.domain.Tag;
import java.util.List;

public interface PostRepository {

    Long savePostWithTags(Post post, List<Tag> tags);

}
