package com.y2gcoder.blog.post.infra.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PostTagJpaRepository extends JpaRepository<PostTagJpaEntity, Long> {

}
