package com.y2gcoder.blog.post.infra.persistence;

import com.y2gcoder.blog.post.domain.Tag;
import com.y2gcoder.blog.post.domain.Tag.TagId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "tag")
@Entity
public class TagJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    public TagJpaEntity(String name) {
        this.name = name;
    }

    public Tag toDomain() {
        return new Tag(new TagId(id), this.name);
    }
}