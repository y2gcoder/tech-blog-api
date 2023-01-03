package com.y2gcoder.blog.post.application.service;

import com.y2gcoder.blog.post.domain.Post.PostId;
import com.y2gcoder.blog.post.domain.Tag;
import java.util.List;

public interface TaggerRepository {

    void taggingPost(PostId postId, List<Tag> tags);
}
