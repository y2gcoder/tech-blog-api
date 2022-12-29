package com.y2gcoder.blog.post.application.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.y2gcoder.blog.post.domain.Post;
import com.y2gcoder.blog.post.domain.Tag;
import com.y2gcoder.blog.post.infra.persistence.FakePostQueryRepository;
import com.y2gcoder.blog.post.infra.persistence.FakePostRepository;
import com.y2gcoder.blog.post.infra.persistence.FakeTagRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PostServiceTest {

    private PostService sut;
    private PostRepository postRepository;

    @BeforeEach
    void beforeEach() {
        postRepository = new FakePostRepository();
        TagRepository tagRepository = new FakeTagRepository();
        LocalDateTimeHolder localDateTimeHolder = new TestLocalDateTimeHolder(
                LocalDateTime.of(2022, 12, 29, 13, 48, 26)
        );
        sut = new PostService(postRepository, tagRepository, localDateTimeHolder);
    }

    @Test
    @DisplayName("포스트를_작성할_수_있다.")
    void givenTitleAndContentAnd3Tags_whenWrite_thenSuccess() {
        //given
        String title = "title";
        String content = "content";
        List<String> tagNames = List.of("태그1", "태그2", "태그3");

        //when
        Long savedPostId = sut.write(title, content, tagNames);

        //then
        FakePostQueryRepository postQueryRepository = new FakePostQueryRepository(
                ((FakePostRepository) postRepository).getStore());
        Optional<Post> result = postQueryRepository.findById(savedPostId);

        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getTitle()).isEqualTo(title);
        assertThat(result.get().getPostingTags().getTags().size()).isEqualTo(3);
        assertThat(result.get().getPostingTags().getTags().stream().map(Tag::getName).collect(
                Collectors.toList())).containsAll(tagNames);
    }

    @Test
    @DisplayName("태그가_없는_포스트를_작성할_수_있다.")
    void givenNoTag_whenWrite_thenSuccess() {
        //given
        String title = "title";
        String content = "content";

        //when
        Long savedPostId = sut.write(title, content, new ArrayList<>());

        //then
        FakePostQueryRepository postQueryRepository = new FakePostQueryRepository(
                ((FakePostRepository) postRepository).getStore());
        Optional<Post> result = postQueryRepository.findById(savedPostId);

        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getTitle()).isEqualTo(title);
        assertThat(result.get().getPostingTags().getTags().size()).isZero();

    }
}