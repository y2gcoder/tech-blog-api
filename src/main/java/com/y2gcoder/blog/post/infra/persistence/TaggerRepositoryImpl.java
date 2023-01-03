package com.y2gcoder.blog.post.infra.persistence;

import com.y2gcoder.blog.post.application.service.TaggerRepository;
import com.y2gcoder.blog.post.domain.Post.PostId;
import com.y2gcoder.blog.post.domain.Tag;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class TaggerRepositoryImpl implements TaggerRepository {

    private final PostTagJpaRepository postTagJpaRepository;

    @Override
    public void taggingPost(PostId postId, List<Tag> tags) {
        Long postIdValue = postId.getValue();

        List<PostTagJpaEntity> entities = tags.stream()
                .map(tag -> new PostTagJpaEntity(postIdValue, tag.getId().getValue())).collect(
                        Collectors.toList());
        postTagJpaRepository.saveAll(entities);
    }
}
