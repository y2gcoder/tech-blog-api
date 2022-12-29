package com.y2gcoder.blog.post.presentation;

import com.y2gcoder.blog.post.application.service.PostService;
import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;

    @PostMapping("/posts")
    ResponseEntity<Void> write(@Valid @RequestBody PostWriteRequest postWriteRequest) {
        String title = postWriteRequest.getTitle();
        String content = postWriteRequest.getContent();
        List<String> tagNames = postWriteRequest.getTagNames();
        Long postId = postService.write(title, content, tagNames);
        return ResponseEntity.created(URI.create("/posts/"+postId)).build();
    }

}
