package com.y2gcoder.blog.post.presentation;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.y2gcoder.blog.post.application.service.PostQueryService;
import com.y2gcoder.blog.post.application.service.PostService;
import com.y2gcoder.blog.post.domain.Post.PostId;
import com.y2gcoder.blog.post.domain.PostWithTags;
import java.time.LocalDateTime;
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
    private PostQueryService postQueryService;

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
    @DisplayName("중복된_태그_이름_목록으로_요청하면_중복을_제거한다.")
    void givenTagNamesDuplicated_whenWrite_thenDistinctTagNames() throws Exception {
        //given
        String title = "title";
        String content = "content";
        List<String> tagNames = Stream.iterate(1, n -> n + 1).limit(5).map(number -> "tag 1")
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
        then(postService).should().write(title, content, List.of("tag 1"));

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

    @Test
    @DisplayName("포스트_단일_검색에_성공한다.")
    void givenPostId_whenGetPost_thenSuccess() throws Exception {
        //given
        Long postId = 1L;
        String title = "title";
        String content = "content";
        LocalDateTime writtenAt = LocalDateTime.of(2022, 12, 30, 17, 22, 17);
        given(postQueryService.getByPostId(postId))
                .willReturn(new PostWithTags(
                        new PostId(postId),
                        title,
                        content,
                        writtenAt,
                        new ArrayList<>()
                ));

        //when
        mockMvc.perform(
                get("/posts/{postId}", postId)
        ).andExpect(status().isOk());

        //then
        then(postQueryService).should().getByPostId(postId);
    }

    @Test
    @DisplayName("포스트_단일_검색할_때_포스트_ID에_다른_형식의_값을_입력하면_안된다.")
    void givenPostIdNotLong_whenGetPost_thenThrowException() throws Exception {
        //given
        String postId = "blabber";

        //when
        //then
        mockMvc.perform(
                        get("/posts/{postId}", postId)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("포스트를_삭제할_수_있다.")
    void givenPostId_whenDeletePost_thenSuccess() throws Exception {
        //given
        //when
        mockMvc.perform(
                delete("/posts/{postId}", 1L)
        ).andExpect(status().isOk());

        //then
        then(postService).should().delete(1L);
    }

}