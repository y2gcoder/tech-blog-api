package com.y2gcoder.blog.post.infra.persistence;

import com.y2gcoder.blog.post.application.service.PostQueryRepository;
import com.y2gcoder.blog.post.domain.Post;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class PostQueryRepositoryImpl implements PostQueryRepository {

    private final PostJpaRepository postJpaRepository;
    private final PostTagJpaRepository postTagJpaRepository;
    private final TagJpaRepository tagJpaRepository;
    private final PostMapper postMapper;

    @Override
    public List<Post> findAll() {
        List<PostJpaEntity> postJpaEntities = postJpaRepository.findAll();

        return postJpaEntities.stream().map(postJpaEntity -> {
            List<Long> tagIdsByPostId = postTagJpaRepository.findTagIdsByPostId(
                    postJpaEntity.getId());
            List<TagJpaEntity> tagJpaEntities = tagJpaRepository.findAllByIdIn(tagIdsByPostId);
            return postMapper.mapToDomainEntity(postJpaEntity, tagJpaEntities);
        }).collect(Collectors.toList());
    }

    @Override
    public Optional<Post> findById(Long postId) {
        Optional<PostJpaEntity> optionalPostJpaEntity = postJpaRepository.findById(postId);
        if (optionalPostJpaEntity.isEmpty()) {
            return Optional.empty();
        }
        PostJpaEntity postJpaEntity = optionalPostJpaEntity.get();
        List<Long> tagIdsByPostId = postTagJpaRepository.findTagIdsByPostId(postId);
        List<TagJpaEntity> tagJpaEntities = tagJpaRepository.findAllByIdIn(tagIdsByPostId);
        return Optional.of(postMapper.mapToDomainEntity(postJpaEntity, tagJpaEntities));
    }
}
