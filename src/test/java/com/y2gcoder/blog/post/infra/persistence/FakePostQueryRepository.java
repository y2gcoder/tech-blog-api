package com.y2gcoder.blog.post.infra.persistence;

import com.y2gcoder.blog.post.application.service.PostQueryRepository;
import com.y2gcoder.blog.post.domain.Post;
import com.y2gcoder.blog.post.domain.Post.PostId;
import com.y2gcoder.blog.post.domain.PostWithTags;
import com.y2gcoder.blog.post.domain.Tag;
import com.y2gcoder.blog.post.domain.Tagger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;

public class FakePostQueryRepository implements PostQueryRepository {
    private final List<Post> postStore;
    private final List<Tagger> taggerStore;

    public FakePostQueryRepository(List<Post> postStore,
            List<Tagger> taggerStore) {
        this.postStore = postStore;
        this.taggerStore = taggerStore;
    }

    @Override
    public Optional<PostWithTags> findById(PostId postId) {
        Optional<Post> optionalPost = findPostByPostId(postId);
        if (optionalPost.isEmpty()) {
            return Optional.empty();
        }
        List<Tag> tags = getTagsByPostId(postId);
        Post post = optionalPost.get();
        return Optional.of(new PostWithTags(post.getId(), post.getTitle(), post.getContent(),
                post.getWrittenAt(),
                tags));
    }

    @Override
    public PostWithTags getById(PostId postId) {
        Post post = findPostByPostId(postId).orElseThrow(EntityNotFoundException::new);
        List<Tag> tags = getTagsByPostId(postId);
        return new PostWithTags(post.getId(), post.getTitle(), post.getContent(),
                post.getWrittenAt(),
                tags);
    }

    private Optional<Post> findPostByPostId(PostId postId) {
        for (Post post : postStore) {
            if (post.getId().equals(postId)) {
                return Optional.of(post);
            }
        }
        return Optional.empty();
    }

    private List<Tag> getTagsByPostId(PostId postId) {
        for (Tagger tagger : taggerStore) {
            if (tagger.getPostId().equals(postId)) {
                return tagger.getTags();
            }
        }
        return new ArrayList<>();
    }

}
