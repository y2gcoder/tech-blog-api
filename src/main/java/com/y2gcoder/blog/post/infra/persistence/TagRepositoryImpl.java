package com.y2gcoder.blog.post.infra.persistence;

import com.y2gcoder.blog.post.application.service.TagRepository;
import com.y2gcoder.blog.post.domain.Tag;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class TagRepositoryImpl implements TagRepository {

    private final TagJpaRepository tagJpaRepository;
    @Override
    public List<Tag> saveAll(List<Tag> tags) {
        List<TagJpaEntity> entities = tags.stream().map(d -> new TagJpaEntity(d.getName()))
                .collect(Collectors.toList());
        return tagJpaRepository.saveAll(entities)
                .stream().map(TagJpaEntity::toDomain)
                .collect(Collectors.toList());
    }
}
