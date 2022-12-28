package com.y2gcoder.blog.post.domain;

import java.time.LocalDateTime;
import java.util.Objects;
import org.springframework.util.StringUtils;

public class Post {

    private PostId id;
    private String title;
    private String content;

    private LocalDateTime writtenAt;

    private Post(PostId id, String title, String content, LocalDateTime writtenAt) {
        this.id = id;
        changeTitle(title);
        changeContent(content);
        this.writtenAt = writtenAt;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public static Post of(
            PostId postId,
            String title,
            String content,
            LocalDateTime writtenAt
    ) {
        assert StringUtils.hasText(title);
        assert StringUtils.hasText(content);
        return new Post(postId, title, content, writtenAt);
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


    public static class PostId {
        private final Long value;

        public PostId(Long value) {
            assert value > 0;
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            PostId postId = (PostId) o;
            return value.equals(postId.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }
    }

}
