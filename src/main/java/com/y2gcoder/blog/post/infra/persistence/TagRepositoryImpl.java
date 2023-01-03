package com.y2gcoder.blog.post.infra.persistence;

import com.y2gcoder.blog.post.application.service.TagRepository;
import com.y2gcoder.blog.post.domain.Tag;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Repository
public class TagRepositoryImpl implements TagRepository {

    private final TagJpaRepository tagJpaRepository;
    private final TagMapper tagMapper;

    @Transactional
    @Override
    public List<Tag> saveAll(List<String> tagNames) {
        List<TagJpaEntity> tagJpaEntities = tagJpaRepository.saveAll(
                tagNames.stream().map(tagName -> tagJpaRepository.findByName(tagName)
                        .orElseGet(() -> new TagJpaEntity(tagName))).collect(Collectors.toList()));
        return tagJpaEntities.stream()
                .map(tagMapper::mapToDomainEntity).collect(
                        Collectors.toList());
    }

}
