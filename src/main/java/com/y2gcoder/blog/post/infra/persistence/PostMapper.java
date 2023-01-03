package com.y2gcoder.blog.post.infra.persistence;

import com.y2gcoder.blog.post.domain.Post;
import com.y2gcoder.blog.post.domain.Post.PostId;
import org.springframework.stereotype.Component;

@Component
class PostMapper {

    Post mapToDomainEntity(PostJpaEntity postJpaEntity) {
        return Post.of(
                new PostId(postJpaEntity.getId()),
                postJpaEntity.getTitle(),
                postJpaEntity.getContent(),
                postJpaEntity.getWrittenAt()
        );
    }
}
