package com.y2gcoder.blog.post.application.service;

import com.y2gcoder.blog.post.domain.Post;
import com.y2gcoder.blog.post.domain.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        taggerRepository.taggingPost(post.getId(), savedTags);
        return post.getId().getValue();
    }

}
