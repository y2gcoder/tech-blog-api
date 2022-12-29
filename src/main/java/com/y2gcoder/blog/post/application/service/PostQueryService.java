package com.y2gcoder.blog.post.application.service;

import com.y2gcoder.blog.post.domain.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PostQueryService {

    private final PostQueryRepository postQueryRepository;

    public Post getByPostId(Long postId) {
        return postQueryRepository.getById(postId);
    }
}
