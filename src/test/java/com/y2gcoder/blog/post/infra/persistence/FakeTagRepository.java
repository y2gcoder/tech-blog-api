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
    public List<Tag> saveAll(List<Tag> tags) {
        List<Tag> tagsWithId = tags.stream()
                .map(tag -> new Tag(new TagId(incrementId++), tag.getName()))
                .collect(Collectors.toList());
        store.addAll(tagsWithId);
        return Collections.unmodifiableList(tagsWithId);
    }
}
