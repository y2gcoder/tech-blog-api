package com.y2gcoder.blog.post.domain;

import com.y2gcoder.blog.post.domain.Post.PostId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.springframework.util.ObjectUtils;

public class Tagger {

    private final PostId postId;

    private final List<Tag> tags;

    public Tagger(PostId postId, List<Tag> tags) {
        if (ObjectUtils.isEmpty(postId)) {
            throw new NullPointerException("PostId is marked non-null but is null");
        }
        this.postId = postId;
        if (ObjectUtils.isEmpty(tags)) {
            tags = new ArrayList<>();
        }
        this.tags = tags;
    }

    public PostId getPostId() {
        return postId;
    }

    public List<Tag> getTags() {
        return Collections.unmodifiableList(this.tags);
    }

    public void addTags(List<Tag> tags) {
        if (ObjectUtils.isEmpty(tags)) {
            throw new NullPointerException("tags is marked non-null but is null");
        }
        for (Tag tag : tags) {
            addTag(tag);
        }
    }

    public void addTag(Tag tag) {
        if (ObjectUtils.isEmpty(tag)) {
            throw new NullPointerException("tag is marked non-null but is null");
        }
        if (isNewTag(tag)) {
            this.tags.add(tag);
        }

    }

    private boolean isNewTag(Tag tag) {
        return !tags.contains(tag) && tags.stream()
                .noneMatch(t -> t.getName().equals(tag.getName()));
    }

    public void removeTag(Tag tag) {
        if (ObjectUtils.isEmpty(tag)) {
            throw new NullPointerException("tag is marked non-null but is null");
        }
        this.tags.remove(tag);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Tagger tagger = (Tagger) o;
        return postId.equals(tagger.postId) && tags.equals(tagger.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId, tags);
    }
}
