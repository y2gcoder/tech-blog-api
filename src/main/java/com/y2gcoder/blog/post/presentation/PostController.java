package com.y2gcoder.blog.post.presentation;

import com.y2gcoder.blog.post.application.service.PostQueryService;
import com.y2gcoder.blog.post.application.service.PostService;
import com.y2gcoder.blog.post.domain.PostWithTags;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/posts")
@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;
    private final PostQueryService postQueryService;

    @PostMapping()
    ResponseEntity<Void> write(@Valid @RequestBody PostWriteRequest postWriteRequest) {
        String title = postWriteRequest.getTitle();
        String content = postWriteRequest.getContent();
        List<String> tagNames = getDistinctTagNames(postWriteRequest);
        Long postId = postService.write(title, content, tagNames);
        return ResponseEntity.created(URI.create("/posts/"+postId)).build();
    }

    private static List<String> getDistinctTagNames(PostWriteRequest postWriteRequest) {
        return postWriteRequest.getTagNames().stream().distinct().collect(
                Collectors.toList());
    }

    @GetMapping("/{postId}")
    ResponseEntity<PostResponse> getPost(@PathVariable Long postId) {
        PostWithTags postWithTags = postQueryService.getByPostId(postId);
        PostResponse postResponse = new PostResponse(
                postWithTags.getId().getValue(),
                postWithTags.getTitle(),
                postWithTags.getContent(),
                postWithTags.getWrittenAt(),
                postWithTags.getTags().stream()
                        .map(domain -> new TagDto(domain.getId().getValue(),
                                domain.getName())).collect(Collectors.toList())
        );
        return ResponseEntity.ok(postResponse);
    }

    @DeleteMapping("/{postId}")
    ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        postService.delete(postId);
        return ResponseEntity.ok().build();
    }

}
