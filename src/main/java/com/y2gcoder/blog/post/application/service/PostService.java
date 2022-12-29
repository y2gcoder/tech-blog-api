package com.y2gcoder.blog.post.application.service;

import com.y2gcoder.blog.post.domain.Post;
import com.y2gcoder.blog.post.domain.PostingTags;
import com.y2gcoder.blog.post.domain.Tag;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final TagRepository tagRepository;

    private final LocalDateTimeHolder localDateTimeHolder;

    public Long write(String title, String content, List<String> tagNames) {
        List<Tag> tags = tagNames.stream().map(tagName -> new Tag(null, tagName))
                .collect(Collectors.toList());
        List<Tag> savedTags = tagRepository.saveAll(tags);
        Post post = Post
                .of(null, title, content, localDateTimeHolder.now(), new PostingTags());
        return postRepository.savePostWithTags(post, savedTags);
    }

}
