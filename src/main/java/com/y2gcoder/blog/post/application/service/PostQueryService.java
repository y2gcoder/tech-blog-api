package com.y2gcoder.blog.post.application.service;

import com.y2gcoder.blog.post.domain.Post.PostId;
import com.y2gcoder.blog.post.domain.PostWithTags;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PostQueryService {

    private final PostQueryRepository postQueryRepository;

    public PostWithTags getByPostId(Long postId) {
        return postQueryRepository.getById(new PostId(postId));
    }
}
