package com.y2gcoder.blog.post.presentation;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.y2gcoder.blog.post.application.service.PostQueryService;
import com.y2gcoder.blog.post.application.service.PostService;
import com.y2gcoder.blog.post.domain.PostNotFoundException;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

@AutoConfigureMockMvc
@SpringBootTest
public class DeletePostE2ETest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostService postService;

    @Autowired
    private PostQueryService postQueryService;

    private List<Long> savedPostIds = new ArrayList<>();

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
    }

    @Transactional
    @Test
    @DisplayName("E2E: 포스트_ID로_해당_포스트를_삭제할_수_있다.")
    void givenSavedPostId_whenDeletePost_thenDeleted() throws Exception {
        //given
        Long postId = savedPostIds.get(1);

        //when
        ResultActions resultActions = mockMvc.perform(delete("/posts/{postId}", postId));

        //then
        resultActions.andExpect(status().isOk());
        assertThatThrownBy(() -> postQueryService.getByPostId(postId))
                .isInstanceOf(PostNotFoundException.class);

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
