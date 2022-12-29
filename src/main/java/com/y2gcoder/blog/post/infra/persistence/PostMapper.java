package com.y2gcoder.blog.post.infra.persistence;

import com.y2gcoder.blog.post.domain.Post;
import com.y2gcoder.blog.post.domain.Post.PostId;
import com.y2gcoder.blog.post.domain.PostingTags;
import com.y2gcoder.blog.post.domain.Tag;
import com.y2gcoder.blog.post.domain.Tag.TagId;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
class PostMapper {

    Post mapToDomainEntity(PostJpaEntity postJpaEntity, List<TagJpaEntity> tagJpaEntities) {
        List<Tag> tags = tagJpaEntities.stream()
                .map(tagJpaEntity -> new Tag(new TagId(tagJpaEntity.getId()),
                        tagJpaEntity.getName())).collect(
                        Collectors.toList());
        return Post.of(
                new PostId(postJpaEntity.getId()),
                postJpaEntity.getTitle(),
                postJpaEntity.getContent(),
                postJpaEntity.getWrittenAt(),
                new PostingTags(tags)
        );
    }
}
