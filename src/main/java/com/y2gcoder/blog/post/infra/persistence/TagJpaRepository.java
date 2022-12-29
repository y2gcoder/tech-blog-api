package com.y2gcoder.blog.post.infra.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagJpaRepository extends JpaRepository<TagJpaEntity, Long> {

    List<TagJpaEntity> findAllByIdIn(List<Long> ids);
}
