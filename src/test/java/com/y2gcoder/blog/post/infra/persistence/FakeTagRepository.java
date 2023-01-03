package com.y2gcoder.blog.post.infra.persistence;

import com.y2gcoder.blog.post.application.service.TagRepository;
import com.y2gcoder.blog.post.domain.Tag;
import com.y2gcoder.blog.post.domain.Tag.TagId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FakeTagRepository implements TagRepository {

    private long incrementId = 1;
    private final List<Tag> store = new ArrayList<>();

    @Override
    public List<Tag> saveAll(List<String> tagNames) {
        List<Tag> tags = new ArrayList<>();

        for (String tagName : tagNames) {
            Tag savedTag = store.stream().filter(t -> t.getName().equals(tagName)).findAny()
                    .orElseGet(() -> {
                        Tag tag = new Tag(new TagId(incrementId++), tagName);
                        store.add(tag);
                        return tag;
                    });
            tags.add(savedTag);
        }

        return Collections.unmodifiableList(tags);
    }

    public List<Tag> getStore() {
        return store;
    }
}
