package com.y2gcoder.blog.post.infra.persistence;

import com.y2gcoder.blog.post.application.service.PostQueryRepository;
import com.y2gcoder.blog.post.domain.Post;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
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
        return postJpaEntities.stream().map(this::getPostDomainByPostJpaEntity).collect(Collectors.toList());
    }

    @Override
    public Optional<Post> findById(Long postId) {
        Optional<PostJpaEntity> optionalPostJpaEntity = postJpaRepository.findById(postId);
        if (optionalPostJpaEntity.isEmpty()) {
            return Optional.empty();
        }

        PostJpaEntity postJpaEntity = optionalPostJpaEntity.get();
        return Optional.of(getPostDomainByPostJpaEntity(postJpaEntity));
    }

    @Override
    public Post getById(Long postId) {
        PostJpaEntity postJpaEntity = postJpaRepository.findById(postId)
                .orElseThrow(EntityNotFoundException::new);
        return getPostDomainByPostJpaEntity(postJpaEntity);
    }

    private Post getPostDomainByPostJpaEntity(PostJpaEntity postJpaEntity) {
        List<Long> tagIdsByPostId = postTagJpaRepository.findTagIdsByPostId(
                postJpaEntity.getId());
        List<TagJpaEntity> tagJpaEntities = new ArrayList<>();
        if (!tagIdsByPostId.isEmpty()) {
            tagJpaEntities = tagJpaRepository.findAllByIdIn(tagIdsByPostId);
        }
        return postMapper.mapToDomainEntity(postJpaEntity, tagJpaEntities);
    }
}
