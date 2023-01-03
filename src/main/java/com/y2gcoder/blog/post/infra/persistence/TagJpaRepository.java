package com.y2gcoder.blog.post.infra.persistence;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagJpaRepository extends JpaRepository<TagJpaEntity, Long> {

    Optional<TagJpaEntity> findByName(String name);


    List<TagJpaEntity> findAllByIdIn(List<Long> ids);
}
