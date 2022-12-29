package com.y2gcoder.blog.post.infra.persistence;

import com.y2gcoder.blog.post.application.service.PostQueryRepository;
import com.y2gcoder.blog.post.domain.Post;
import java.util.Collections;
import java.util.List;
import org.springframework.util.ObjectUtils;

public class FakePostQueryRepository implements PostQueryRepository {
    private final List<Post> store;

    public FakePostQueryRepository(List<Post> store) {
        if (ObjectUtils.isEmpty(store)) {
            throw new NullPointerException("store should not be null");
        }
        this.store = store;
    }

    @Override
    public List<Post> findAll() {
        return Collections.unmodifiableList(store);
    }
}