package com.y2gcoder.blog.post.infra.persistence;

import com.y2gcoder.blog.post.application.service.PostQueryRepository;
import com.y2gcoder.blog.post.domain.Post.PostId;
import com.y2gcoder.blog.post.domain.PostWithTags;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class PostQueryRepositoryImpl implements PostQueryRepository {

    private final PostJpaRepository postJpaRepository;
    private final TagJpaRepository tagJpaRepository;
    private final PostTagJpaRepository postTagJpaRepository;
    private final PostWithTagsMapper postWithTagsMapper;

    @Override
    public Optional<PostWithTags> findById(PostId postId) {
        Optional<PostJpaEntity> optionalPostJpaEntity = postJpaRepository.findById(postId.getValue());
        if (optionalPostJpaEntity.isEmpty()) {
            return Optional.empty();
        }
        PostJpaEntity postJpaEntity = optionalPostJpaEntity.get();

        PostWithTags postWithTags = getPostWithTags(postJpaEntity);
        return Optional.of(postWithTags);
    }

    @Override
    public PostWithTags getById(PostId postId) {
        PostJpaEntity postJpaEntity = postJpaRepository.findById(postId.getValue())
                .orElseThrow(EntityNotFoundException::new);

        return getPostWithTags(postJpaEntity);
    }

    private PostWithTags getPostWithTags(PostJpaEntity postJpaEntity) {
        List<Long> tagIdsByPostId = postTagJpaRepository.findTagIdsByPostId(postJpaEntity.getId());
        List<TagJpaEntity> tagJpaEntities = new ArrayList<>();
        if (!tagIdsByPostId.isEmpty()) {
            tagJpaEntities = tagJpaRepository.findAllByIdIn(tagIdsByPostId);
        }

        return postWithTagsMapper.mapToPostWithTags(postJpaEntity, tagJpaEntities);
    }

}
