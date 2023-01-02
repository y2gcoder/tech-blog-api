package com.y2gcoder.blog.post.domain;

import com.y2gcoder.blog.common.domain.DomainId;
import com.y2gcoder.blog.post.domain.Post.PostId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.springframework.util.ObjectUtils;

public class Tagger {

    private final TaggerId id;

    private final PostId postId;

    private final List<Tag> tags;

    public Tagger(TaggerId id, PostId postId, List<Tag> tags) {
        this.id = id;
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

    public void addTag(Tag tag) {
        if (ObjectUtils.isEmpty(tag)) {
            throw new NullPointerException("tag is marked non-null but is null");
        }
        this.tags.add(tag);
    }

    public void addTags(List<Tag> tags) {
        if (ObjectUtils.isEmpty(tags)) {
            throw new NullPointerException("tags is marked non-null but is null");
        }
        this.tags.addAll(tags);
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
        return Objects.equals(id, tagger.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public static class TaggerId extends DomainId {

        public TaggerId(Long value) {
            super(value);
        }
    }

}