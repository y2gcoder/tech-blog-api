package com.y2gcoder.blog.post.infra.persistence;

import com.y2gcoder.blog.post.domain.Tag;
import com.y2gcoder.blog.post.domain.Tag.TagId;
import org.springframework.stereotype.Component;

@Component
public class TagMapper {

    Tag mapToDomainEntity(TagJpaEntity tagJpaEntity) {
        return new Tag(new TagId(tagJpaEntity.getId()), tagJpaEntity.getName());
    }

}
