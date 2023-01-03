package com.y2gcoder.blog.post.presentation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.y2gcoder.blog.post.application.service.PostQueryRepository;
import com.y2gcoder.blog.post.domain.Post.PostId;
import com.y2gcoder.blog.post.domain.PostWithTags;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

@AutoConfigureMockMvc
@SpringBootTest
class WritePostE2ETest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostQueryRepository postQueryRepository;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void beforeEach() {
        objectMapper = new ObjectMapper();
    }


    @Transactional
    @Test
    @DisplayName("E2E: 포스트_글쓰기를_할_수_있다.")
    void whenWritePost_thenSuccess() throws Exception {
        //given
        String title = "title";
        String content = "content";
        List<String> tagNames = Stream.iterate(1, n -> n + 1).limit(5).map(number -> "tag " + number)
                .collect(Collectors.toList());
        PostWriteRequest postWriteRequest = new PostWriteRequest(title, content, tagNames);
        String requestJson = objectMapper.writeValueAsString(postWriteRequest);

        //when
        ResultActions resultActions = mockMvc.perform(
                        post("/posts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isCreated());

        //then
        String locationHeader = resultActions.andReturn().getResponse().getHeader("Location");
        Long savedPostId = Long.valueOf(locationHeader.replace("/posts/", ""));
        Optional<PostWithTags> optionalPostWithTags = postQueryRepository.findById(new PostId(savedPostId));
        assertThat(optionalPostWithTags.isPresent()).isTrue();
        PostWithTags postWithTags = optionalPostWithTags.get();
        assertThat(postWithTags.getTitle()).isEqualTo(title);
        assertThat(postWithTags.getTags().size()).isEqualTo(5);
    }

    @Transactional
    @Test
    @DisplayName("E2E: 태그가_없는_포스트도_쓸_수_있다.")
    void givenNoTag_whenWritePost_thenSuccess() throws Exception {
        //given
        String title = "title";
        String content = "content";
        Map<String, Object> postWriteRequest = new HashMap<>();
        postWriteRequest.put("title", title);
        postWriteRequest.put("content", content);

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(postWriteRequest);

        //when
        ResultActions resultActions = mockMvc.perform(
                        post("/posts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isCreated());

        //then
        String locationHeader = resultActions.andReturn().getResponse().getHeader("Location");
        Long savedPostId = Long.valueOf(locationHeader.replace("/posts/", ""));
        Optional<PostWithTags> optionalPostWithTags = postQueryRepository.findById(new PostId(savedPostId));
        assertThat(optionalPostWithTags.isPresent()).isTrue();
        PostWithTags postWithTags = optionalPostWithTags.get();
        assertThat(postWithTags.getTitle()).isEqualTo(title);
        assertThat(postWithTags.getTags().size()).isEqualTo(0);

    }
}