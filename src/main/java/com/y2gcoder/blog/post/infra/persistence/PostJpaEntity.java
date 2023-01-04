package com.y2gcoder.blog.post.infra.persistence;

import com.y2gcoder.blog.common.persistence.BaseTimeEntity;
import java.time.LocalDateTime;
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
@Table(name = "post")
@Entity
public class PostJpaEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    private LocalDateTime writtenAt;

    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeContent(String content) {
        this.content = content;
    }

    public PostJpaEntity(String title, String content, LocalDateTime writtenAt) {
        this.title = title;
        this.content = content;
        this.writtenAt = writtenAt;
    }

    public PostJpaEntity(Long id, String title, String content, LocalDateTime writtenAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.writtenAt = writtenAt;
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateContent(String content) {
        this.content = content;
    }
}
