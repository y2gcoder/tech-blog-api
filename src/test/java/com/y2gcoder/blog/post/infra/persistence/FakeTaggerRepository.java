package com.y2gcoder.blog.post.infra.persistence;

import com.y2gcoder.blog.post.application.service.TaggerRepository;
import com.y2gcoder.blog.post.domain.Post.PostId;
import com.y2gcoder.blog.post.domain.Tagger;
import java.util.ArrayList;
import java.util.List;

public class FakeTaggerRepository implements TaggerRepository {
    private long incrementId = 1;
    private final List<Tagger> store = new ArrayList<>();

    @Override
    public void tagging(Tagger tagger) {
        for (Tagger t : store) {
            if (t.getPostId().equals(tagger.getPostId())) {
                t.addTags(tagger.getTags());
                return;
            }
        }
        store.add(tagger);
    }

    @Override
    public void deleteByPostId(PostId postId) {
        this.store.removeIf(tagger -> tagger.getPostId().equals(postId));
    }

    public List<Tagger> getStore() {
        return store;
    }
}
