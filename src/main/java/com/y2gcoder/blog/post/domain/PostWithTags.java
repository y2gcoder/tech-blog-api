package com.y2gcoder.blog.post.domain;

import com.y2gcoder.blog.post.domain.Post.PostId;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PostWithTags {
    private final PostId id;
    private final String title;
    private final String content;
    private final LocalDateTime writtenAt;
    private final List<Tag> tags;

    public PostWithTags(PostId id, String title, String content, LocalDateTime writtenAt,
            List<Tag> tags) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.writtenAt = writtenAt;
        this.tags = tags;
    }

    public PostId getId() {
        return id;
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

    public List<Tag> getTags() {
        return new ArrayList<>(tags);
    }
}
