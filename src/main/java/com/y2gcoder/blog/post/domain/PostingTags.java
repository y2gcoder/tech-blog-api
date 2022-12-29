package com.y2gcoder.blog.post.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PostingTags {
    private final List<Tag> tags;

    public PostingTags(List<Tag> tags) {
        if (tags == null) {
            throw new NullPointerException("tags is marked non-null but is null");
        }
        this.tags = tags;
    }

    public PostingTags(Tag... tags) {
        if (tags == null) {
            throw new NullPointerException("tags is marked non-null but is null");
        }
        this.tags = new ArrayList<>(Arrays.asList(tags));
    }

    public List<Tag> getTags() {
        return Collections.unmodifiableList(this.tags);
    }

    public void addTag(Tag tag) {
        this.tags.add(tag);
    }

    public void addTags(List<Tag> tags) {
        if (tags == null) {
            throw new NullPointerException("tags is marked non-null but is null");
        }
        this.tags.addAll(tags);
    }

    public void addTags(Tag... tags) {
        if (tags == null) {
            throw new NullPointerException("tags is marked non-null but is null");
        }
        this.tags.addAll(Arrays.asList(tags));
    }

    public void removeTag(Tag tag) {
        this.tags.remove(tag);
    }
}
