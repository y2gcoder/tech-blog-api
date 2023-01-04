package com.y2gcoder.blog.post.infra.persistence;

import com.y2gcoder.blog.post.application.service.PostRepository;
import com.y2gcoder.blog.post.domain.Post;
import com.y2gcoder.blog.post.domain.Post.PostId;
import com.y2gcoder.blog.post.domain.PostNotFoundException;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

@Transactional
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

    @Transactional(readOnly = true)
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

    @Override
    public void updatePost(Post post) {
        if (ObjectUtils.isEmpty(post.getId())) {
            savePost(post.getTitle(), post.getContent(), post.getWrittenAt());
            return;
        }
        PostJpaEntity postJpaEntity = postJpaRepository.findById(post.getId().getValue())
                .orElseThrow(PostNotFoundException::new);
        postJpaEntity.updateTitle(post.getTitle());
        postJpaEntity.updateContent(post.getContent());
    }
}
