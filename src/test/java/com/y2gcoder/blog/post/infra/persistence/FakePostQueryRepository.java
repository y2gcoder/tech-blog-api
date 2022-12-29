package com.y2gcoder.blog.post.infra.persistence;

import com.y2gcoder.blog.post.application.service.PostQueryRepository;
import com.y2gcoder.blog.post.domain.Post;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
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

    @Override
    public Optional<Post> findById(Long postId) {
        for (Post post : store) {
            if (post.getId().getValue().equals(postId)) {
                return Optional.of(post);
            }
        }
        return Optional.empty();
    }

    @Override
    public Post getById(Long postId) {
        for (Post post : store) {
            if (post.getId().getValue().equals(postId)) {
                return post;
            }
        }
        throw new EntityNotFoundException();    //TODO 이거 JPA에 맞춰서 했는데 다른 걸로 래핑이 필요한가? 나중에
    }

}
