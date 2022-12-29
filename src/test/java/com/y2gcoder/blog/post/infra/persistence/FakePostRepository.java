package com.y2gcoder.blog.post.infra.persistence;

import com.y2gcoder.blog.post.application.service.PostRepository;
import com.y2gcoder.blog.post.domain.Post;
import com.y2gcoder.blog.post.domain.Post.PostId;
import com.y2gcoder.blog.post.domain.PostingTags;
import com.y2gcoder.blog.post.domain.Tag;
import java.util.ArrayList;
import java.util.List;

public class FakePostRepository implements PostRepository {

    private long incrementId = 1;
    private final List<Post> store = new ArrayList<>();

    @Override
    public void savePostWithTags(Post post, List<Tag> tags) {
        Post newPost = Post.of(new PostId(incrementId++), post.getTitle(), post.getContent(),
                post.getWrittenAt(),
                new PostingTags(tags));

        store.add(newPost);
    }

    public List<Post> getStore() {
        return new ArrayList<>(store);
    }
}
