package com.y2gcoder.blog.post.infra.persistence;

import com.y2gcoder.blog.post.application.service.PostRepository;
import com.y2gcoder.blog.post.domain.Post;
import com.y2gcoder.blog.post.domain.Tag;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class PostRepositoryImpl implements PostRepository {

    private final PostJpaRepository postJpaRepository;
    private final PostTagJpaRepository postTagJpaRepository;


    @Override
    public Long savePostWithTags(Post post, List<Tag> tags) {
        PostJpaEntity postJpaEntity = postJpaRepository.save(
                new PostJpaEntity(post.getTitle(), post.getContent(), post.getWrittenAt()));
        List<PostTagJpaEntity> postTagJpaEntities = tags.stream()
                .map(tag -> new PostTagJpaEntity(postJpaEntity.getId(), tag.getId().getValue()))
                .collect(
                        Collectors.toList());
        postTagJpaRepository.saveAll(postTagJpaEntities);

        return postJpaEntity.getId();
    }
}
