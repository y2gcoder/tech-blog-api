package com.y2gcoder.blog.post.presentation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.y2gcoder.blog.post.application.service.PostQueryService;
import com.y2gcoder.blog.post.application.service.PostService;
import com.y2gcoder.blog.post.domain.PostWithTags;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@AutoConfigureMockMvc
@SpringBootTest
public class GetPostE2ETest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostService postService;

    @Autowired
    private PostQueryService postQueryService;

    private List<Long> savedPostIds = new ArrayList<>();
    private ObjectMapper objectMapper;



    @BeforeEach
    void beforeEach() {
        for (int i = 1; i < 4; i++) {
            String title = "title "+i;
            String content = "content "+i;
            List<String> tagNames = IntStream.range(1 + (3 * (i - 1)), 4 + (3 * (i - 1))).mapToObj(v -> "tag " + v)
                    .collect(Collectors.toList());
            Long savedPostId = postService.write(title, content, tagNames);
            savedPostIds.add(savedPostId);
        }

        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @Transactional
    @Test
    @DisplayName("E2E: 포스트_ID로_해당_포스트를_조회할_수_있다.")
    void givenSavedPostId_whenGetPost_thenGetThatPost() throws Exception {
        //given
        Long postId = savedPostIds.get(2);

        //when
        ResultActions resultActions = mockMvc.perform(get("/posts/{postId}", postId));

        //then
        resultActions.andExpect(status().isOk());
        PostResponse response = objectMapper.readValue(
                resultActions.andReturn().getResponse().getContentAsString(),
                PostResponse.class);
        assertThat(response.getPostId()).isEqualTo(response.getPostId());

        PostWithTags postWithTags = postQueryService.getByPostId(postId);
        assertThat(response.getTitle()).isEqualTo(postWithTags.getTitle());
        assertThat(response.getWrittenAt()).isEqualToIgnoringNanos(postWithTags.getWrittenAt());
        assertThat(response.getTags().stream().map(TagDto::getTagId).collect(Collectors.toList()))
                .containsAll(
                        postWithTags.getTags().stream().map(tag -> tag.getId().getValue()).collect(
                                Collectors.toList()));
    }

}
