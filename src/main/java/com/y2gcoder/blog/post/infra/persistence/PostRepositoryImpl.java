package com.y2gcoder.blog.post.infra.persistence;

import com.y2gcoder.blog.post.application.service.PostRepository;
import com.y2gcoder.blog.post.domain.Post;
import com.y2gcoder.blog.post.domain.Post.PostId;
import com.y2gcoder.blog.post.domain.PostNotFoundException;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class PostRepositoryImpl implements PostRepository {

    private final PostJpaRepository postJpaRepository;

    private final PostMapper postMapper;

    @Override
    public Post savePost(String title, String content, LocalDateTime writtenAt) {
        PostJpaEntity postJpaEntity = postJpaRepository.save(
                new PostJpaEntity(title, content, writtenAt));
        return postMapper.mapToDomainEntity(postJpaEntity);
    }

    @Override
    public Post getById(PostId postId) {
        PostJpaEntity postJpaEntity = postJpaRepository.findById(postId.getValue())
                .orElseThrow(PostNotFoundException::new);
        return postMapper.mapToDomainEntity(postJpaEntity);
    }

    @Override
    public void deleteByPostId(PostId postId) {
        PostJpaEntity postJpaEntity = postJpaRepository.findById(postId.getValue())
                .orElseThrow(PostNotFoundException::new);
        postJpaRepository.delete(postJpaEntity);
    }
}
