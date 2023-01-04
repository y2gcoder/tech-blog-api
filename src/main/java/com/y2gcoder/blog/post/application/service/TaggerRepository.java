package com.y2gcoder.blog.post.application.service;

import com.y2gcoder.blog.post.domain.Post.PostId;
import com.y2gcoder.blog.post.domain.Tagger;

public interface TaggerRepository {

    void tagging(Tagger tagger);

    void deleteByPostId(PostId postId);
}
