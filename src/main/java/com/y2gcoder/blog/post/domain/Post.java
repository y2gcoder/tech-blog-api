package com.y2gcoder.blog.post.domain;

import com.y2gcoder.blog.common.domain.DomainId;
import java.time.LocalDateTime;
import java.util.Objects;
import org.springframework.util.StringUtils;

public class Post {

    private final PostId id;
    private String title;
    private String content;

    private final LocalDateTime writtenAt;
    private final PostingTags postingTags;

    private Post(PostId id, String title, String content, LocalDateTime writtenAt, PostingTags postingTags) {
        if(!StringUtils.hasText(title)) {
            throw new IllegalArgumentException("post title doesn't have text");
        }
        if (!StringUtils.hasText(content)) {
            throw new IllegalArgumentException("post content doesn't have text");
        }
        if (writtenAt == null) {
            throw new NullPointerException("post writtenAt is null");
        }

        this.id = id;
        this.title = title;
        this.content = content;
        this.writtenAt = writtenAt;
        this.postingTags = postingTags;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getWrittenAt() {
        return writtenAt;
    }

    public PostingTags getPostingTags() {
        return postingTags;
    }

    public PostId getId() {
        return id;
    }

    public static Post of(
            PostId postId,
            String title,
            String content,
            LocalDateTime writtenAt,
            PostingTags postingTags
    ) {
        return new Post(postId, title, content, writtenAt, postingTags);
    }

    public void edit(String title, String content) {
        changeTitle(title);
        changeContent(content);
    }

    private void changeTitle(String title) {
        if (StringUtils.hasText(title)) {
            this.title = title;
        }
    }

    private void changeContent(String content) {
        if (StringUtils.hasText(content)) {
            this.content = content;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Post post = (Post) o;
        return Objects.equals(id, post.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public static class PostId extends DomainId {

        public PostId(Long value) {
            super(value);
        }
    }

}
