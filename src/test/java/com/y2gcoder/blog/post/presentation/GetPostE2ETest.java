package com.y2gcoder.blog.post.presentation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.y2gcoder.blog.post.application.service.PostQueryRepository;
import com.y2gcoder.blog.post.application.service.PostRepository;
import com.y2gcoder.blog.post.application.service.TagRepository;
import com.y2gcoder.blog.post.domain.Post;
import com.y2gcoder.blog.post.domain.PostingTags;
import com.y2gcoder.blog.post.domain.Tag;
import java.time.LocalDateTime;
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
public class GetPostE2ETest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostQueryRepository postQueryRepository;

    Post post;

    private ObjectMapper objectMapper;

    @BeforeEach
    void beforeEach() {
        List<Tag> tags = tagRepository.saveAll(IntStream.range(1, 5).mapToObj(i -> new Tag(null, "tag " + i))
                .collect(Collectors.toList()));
        LocalDateTime writtenAt = LocalDateTime.of(2022, 12, 30, 19, 1, 3);

        Long savedPostId = postRepository.savePostWithTags(
                Post.of(null, "title", "content", writtenAt, new PostingTags(tags)), tags);
        post = postQueryRepository.getById(savedPostId);
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @Transactional
    @Test
    @DisplayName("E2E: 포스트_ID로_해당_포스트를_조회할_수_있다.")
    void givenSavedPostId_whenGetPost_thenThatPost() throws Exception {
        //given
        Long postId = post.getId().getValue();

        //when
        ResultActions resultActions = mockMvc.perform(
                get("/posts/{postId}", postId)
        );

        //then
        resultActions.andExpect(status().isOk());
        PostResponse response = objectMapper.readValue(
                resultActions.andReturn().getResponse().getContentAsString(),
                PostResponse.class);
        assertThat(response.getPostId()).isEqualTo(postId);
        assertThat(response.getTitle()).isEqualTo(post.getTitle());
        assertThat(response.getTags().size()).isEqualTo(post.getPostingTags().getTags().size());
        assertThat(response.getWrittenAt()).isEqualTo(post.getWrittenAt());
    }

}
