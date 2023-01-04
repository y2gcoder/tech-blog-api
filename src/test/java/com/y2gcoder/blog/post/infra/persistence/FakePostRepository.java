package com.y2gcoder.blog.post.infra.persistence;

import com.y2gcoder.blog.post.application.service.PostRepository;
import com.y2gcoder.blog.post.domain.Post;
import com.y2gcoder.blog.post.domain.Post.PostId;
import com.y2gcoder.blog.post.domain.PostNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FakePostRepository implements PostRepository {

    private long incrementId = 1;
    private final List<Post> store = new ArrayList<>();

    @Override
    public Post savePost(String title, String content, LocalDateTime writtenAt) {
        Post post = Post.of(new PostId(incrementId++), title, content, writtenAt);
        store.add(post);
        return post;
    }

    @Override
    public Post getById(PostId postId) {
        for (Post post : store) {
            if (post.getId().equals(postId)) {
                return post;
            }
        }
        throw new PostNotFoundException();
    }

    @Override
    public void deleteByPostId(PostId postId) {
        Post post = getById(postId);
        this.store.remove(post);
    }

    public List<Post> getStore() {
        return new ArrayList<>(store);
    }
}
