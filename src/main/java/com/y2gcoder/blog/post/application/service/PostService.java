package com.y2gcoder.blog.post.application.service;

import com.y2gcoder.blog.post.domain.Post;
import com.y2gcoder.blog.post.domain.Post.PostId;
import com.y2gcoder.blog.post.domain.Tag;
import com.y2gcoder.blog.post.domain.Tagger;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final TaggerRepository taggerRepository;
    private final LocalDateTimeHolder localDateTimeHolder;

    public Long write(String title, String content, List<String> tagNames) {
        if (tagNames.isEmpty()) {
            return postRepository
                    .savePost(title, content, localDateTimeHolder.now()).getId().getValue();
        }

        List<Tag> savedTags = tagRepository.saveAll(tagNames);
        Post post = postRepository.savePost(title, content, localDateTimeHolder.now());
        Tagger tagger = new Tagger(post.getId(), savedTags);
        taggerRepository.tagging(tagger);
        return post.getId().getValue();
    }

    public void delete(Long id) {
        PostId postId = new PostId(id);
        //포스트 삭제
        postRepository.deleteByPostId(postId);

        //태거 삭제
        taggerRepository.deleteByPostId(postId);
    }

    public void edit(Long id, String title, String content, List<String> tagNames) {
        PostId postId = new PostId(id);
        Post post = updatePost(title, content, postId);
        changeTags(tagNames, postId, post);
    }

    private Post updatePost(String title, String content, PostId postId) {
        Post post = postRepository.getById(postId);
        post.edit(title, content);
        postRepository.updatePost(post);
        return post;
    }

    private void changeTags(List<String> tagNames, PostId postId, Post post) {
        taggerRepository.deleteByPostId(postId);
        if (tagNames.isEmpty()) {
            return;
        }
        List<Tag> savedTags = tagRepository.saveAll(tagNames);
        Tagger tagger = new Tagger(post.getId(), savedTags);
        taggerRepository.tagging(tagger);
    }

}
