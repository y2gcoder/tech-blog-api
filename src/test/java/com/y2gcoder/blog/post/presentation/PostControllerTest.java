package com.y2gcoder.blog.post.presentation;

import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.y2gcoder.blog.post.application.service.PostService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = PostController.class)
class PostControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    @Test
    @DisplayName("write_테스트_성공")
    void whenWrite_thenSuccess() throws Exception {
        //given
        String title = "title";
        String content = "content";
        List<String> tagNames = Stream.iterate(1, n -> n + 1).limit(5).map(number -> "tag " + number)
                .collect(Collectors.toList());
        PostWriteRequest postWriteRequest = new PostWriteRequest(title, content, tagNames);
        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(postWriteRequest);

        //when
        mockMvc.perform(
                post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
        ).andExpect(status().isCreated());

        //then
        then(postService).should().write(title, content, tagNames);
    }

    @Test
    @DisplayName("빈_포스트_제목을_보내면_검증_실패한다.")
    void givenBlankTitle_whenWrite_thenInvalid() throws Exception {
        //given
        String title = "";
        String content = "content";
        List<String> tagNames = Stream.iterate(1, n -> n + 1).limit(5).map(number -> "tag " + number)
                .collect(Collectors.toList());
        PostWriteRequest postWriteRequest = new PostWriteRequest(title, content, tagNames);
        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(postWriteRequest);

        //when
        //then
        mockMvc.perform(
                        post("/posts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isBadRequest());

    }

    @Test
    @DisplayName("빈_포스트_내용을_보내면_검증_실패한다.")
    void givenBlankContent_whenWrite_thenInvalid() throws Exception {
        //given
        String title = "title";
        String content = "";
        List<String> tagNames = Stream.iterate(1, n -> n + 1).limit(5).map(number -> "tag " + number)
                .collect(Collectors.toList());
        PostWriteRequest postWriteRequest = new PostWriteRequest(title, content, tagNames);
        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(postWriteRequest);

        //when
        //then
        mockMvc.perform(
                        post("/posts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isBadRequest());

    }

    @Test
    @DisplayName("태그_목록없이_보냈을_때_성공한다.")
    void givenWithoutTagNames_whenWrite_thenSuccess() throws Exception {
        //given
        String title = "title";
        String content = "content";
        Map<String, Object> postWriteRequest = new HashMap<>();
        postWriteRequest.put("title", title);
        postWriteRequest.put("content", content);

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(postWriteRequest);

        //when
        mockMvc.perform(
                        post("/posts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isCreated());

        //then
        then(postService).should().write(title, content, new ArrayList<>());

    }


}