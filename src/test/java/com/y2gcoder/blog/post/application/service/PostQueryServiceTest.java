package com.y2gcoder.blog.post.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.y2gcoder.blog.post.domain.Post;
import com.y2gcoder.blog.post.domain.PostingTags;
import com.y2gcoder.blog.post.infra.persistence.FakePostQueryRepository;
import com.y2gcoder.blog.post.infra.persistence.FakePostRepository;
import java.time.LocalDateTime;
import javax.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PostQueryServiceTest {

    private PostQueryService sut;

    private PostRepository postRepository;
    private LocalDateTimeHolder localDateTimeHolder;

    @BeforeEach
    void beforeEach() {
        postRepository = new FakePostRepository();
        localDateTimeHolder = new TestLocalDateTimeHolder(
                LocalDateTime.of(2022, 12, 29, 13, 48, 26)
        );
    }

    @Test
    @DisplayName("포스트_ID로_포스트를_찾을_수_있다.")
    void givenPostId_whenGetByPostId_thenReturnPost() {
        //given
        String title = "title";
        String content = "content";
        Long savedPostId = postRepository.savePostWithoutTags(
                Post.of(null, title, content, localDateTimeHolder.now(), new PostingTags()));

        //when
        sut = new PostQueryService(
                new FakePostQueryRepository(((FakePostRepository) postRepository).getStore()));
        Post result = sut.getByPostId(savedPostId);

        //then
        assertThat(result.getId().getValue()).isEqualTo(savedPostId);
        assertThat(result.getTitle()).isEqualTo(title);
    }

    @Test
    @DisplayName("해당_포스트_ID를_가지고_있는_포스트가_없다.")
    void givenWrongPostId_whenGetByPostId_thenThrowException() {
        //given
        String title = "title";
        String content = "content";
        postRepository.savePostWithoutTags(
                Post.of(null, title, content, localDateTimeHolder.now(), new PostingTags()));

        //when, then
        sut = new PostQueryService(
                new FakePostQueryRepository(((FakePostRepository) postRepository).getStore()));
        Long wrongPostId = 0L;
        assertThatThrownBy(() -> sut.getByPostId(wrongPostId)).isInstanceOf(
                EntityNotFoundException.class);
    }

}