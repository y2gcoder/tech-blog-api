package com.y2gcoder.blog.post.presentation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.y2gcoder.blog.post.application.service.PostQueryService;
import com.y2gcoder.blog.post.application.service.PostService;
import com.y2gcoder.blog.post.domain.PostWithTags;
import com.y2gcoder.blog.post.domain.Tag;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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
public class EditPostE2ETest {
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
        objectMapper = new ObjectMapper();
    }

    @Transactional
    @Test
    @DisplayName("E2E: 포스트_제목과_내용을_수정할_수_있다.")
    void givenNewTitleAndNewContent_whenEditPost_thenEdited() throws Exception {
        //given
        Long postId = savedPostIds.get(1);
        PostWithTags before = postQueryService.getByPostId(postId);
        assertThat(before.getTitle()).isEqualTo("title 2");
        assertThat(before.getContent()).isEqualTo("content 2");
        String newTitle = "newTitle";
        String newContent = "newContent";
        List<String> tagNames = before.getTags().stream().map(Tag::getName).collect(
                Collectors.toList());

        PostEditRequest postEditRequest = new PostEditRequest(newTitle, newContent, tagNames);
        String requestJson = objectMapper.writeValueAsString(postEditRequest);

        //when
        ResultActions resultActions = mockMvc.perform(put("/posts/{postId}", postId).contentType(
                MediaType.APPLICATION_JSON).content(requestJson));

        //then
        resultActions.andExpect(status().isOk());
        PostWithTags postWithTags = postQueryService.getByPostId(postId);
        assertThat(postWithTags.getTitle()).isEqualTo(newTitle);
        assertThat(postWithTags.getContent()).isEqualTo(newContent);
        assertThat(postWithTags.getTags().size()).isEqualTo(3);
        assertThat(postWithTags.getTags().stream().map(Tag::getName)
                .collect(Collectors.toList())).containsAll(tagNames);

    }

    @Transactional
    @Test
    @DisplayName("E2E: 포스트_제목과_내용과_태그를_수정할_수_있다.")
    void givenNewTitleAndNewContentAndNewTags_whenEditPost_thenEdited() throws Exception {
        //given
        Long postId = savedPostIds.get(1);
        PostWithTags before = postQueryService.getByPostId(postId);
        assertThat(before.getTitle()).isEqualTo("title 2");
        assertThat(before.getContent()).isEqualTo("content 2");
        assertThat(before.getTags().size()).isEqualTo(3);

        String newTitle = before.getTitle();
        String newContent = "newContent";
        List<String> tagNames = IntStream.range(7, 17).mapToObj(v -> "tag " + v)
                .collect(Collectors.toList());
        PostEditRequest postEditRequest = new PostEditRequest(newTitle, newContent, tagNames);
        String requestJson = objectMapper.writeValueAsString(postEditRequest);

        //when
        ResultActions resultActions = mockMvc.perform(put("/posts/{postId}", postId).contentType(
                MediaType.APPLICATION_JSON).content(requestJson));

        //then
        resultActions.andExpect(status().isOk());
        PostWithTags postWithTags = postQueryService.getByPostId(postId);
        assertThat(postWithTags.getTitle()).isEqualTo(newTitle);
        assertThat(postWithTags.getContent()).isEqualTo(newContent);
        assertThat(postWithTags.getTags().size()).isEqualTo(10);
        assertThat(postWithTags.getTags().stream().map(Tag::getName)
                .collect(Collectors.toList())).containsAll(tagNames);

    }

    @Transactional
    @Test
    @DisplayName("E2E: 존재하지_않는_포스트를_삭제할_수_없다.")
    void givenNotFoundPostId_whenDeletePost_thenFail() throws Exception {
        //given
        Long postId = savedPostIds.get(2) + 1L;

        //when
        mockMvc.perform(delete("/posts/{postId}", postId))
                .andExpect(status().isBadRequest());

    }
}
