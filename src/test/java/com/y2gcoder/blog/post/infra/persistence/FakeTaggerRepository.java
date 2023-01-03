package com.y2gcoder.blog.post.infra.persistence;

import com.y2gcoder.blog.post.application.service.TaggerRepository;
import com.y2gcoder.blog.post.domain.Post.PostId;
import com.y2gcoder.blog.post.domain.Tag;
import com.y2gcoder.blog.post.domain.Tagger;
import com.y2gcoder.blog.post.domain.Tagger.TaggerId;
import java.util.ArrayList;
import java.util.List;

public class FakeTaggerRepository implements TaggerRepository {
    private long incrementId = 1;
    private final List<Tagger> store = new ArrayList<>();

    @Override
    public void taggingPost(PostId postId, List<Tag> tags) {
        Tagger tagger = new Tagger(new TaggerId(incrementId++), postId, tags);
        store.add(tagger);
    }

    public List<Tagger> getStore() {
        return store;
    }
}
