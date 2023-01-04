package com.y2gcoder.blog.post.infra.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostTagJpaRepository extends JpaRepository<PostTagJpaEntity, Long> {

    @Query("select pt.tagId from PostTagJpaEntity pt where pt.postId = :postId")
    List<Long> findTagIdsByPostId(Long postId);

    void deleteAllByPostId(Long postId);
}
