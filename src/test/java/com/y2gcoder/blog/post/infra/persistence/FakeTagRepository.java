package com.y2gcoder.blog.post.infra.persistence;

import com.y2gcoder.blog.post.application.service.TagRepository;
import com.y2gcoder.blog.post.domain.Tag;
import com.y2gcoder.blog.post.domain.Tag.TagId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FakeTagRepository implements TagRepository {

    private long incrementId = 1;
    private final List<Tag> store = new ArrayList<>();

    @Override
    public List<Tag> saveAll(List<String> tagNames) {
        List<Tag> tags = tagNames.stream().map(s -> new Tag(new TagId(incrementId++), s))
                .collect(Collectors.toList());
        store.addAll(tags);
        return Collections.unmodifiableList(tags);
    }

    public List<Tag> getStore() {
        return store;
    }
}
