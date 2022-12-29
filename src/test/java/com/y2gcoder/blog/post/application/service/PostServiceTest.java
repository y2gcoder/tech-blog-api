package com.y2gcoder.blog.post.application.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.y2gcoder.blog.post.domain.Post;
import com.y2gcoder.blog.post.domain.Tag;
import com.y2gcoder.blog.post.infra.persistence.FakePostQueryRepository;
import com.y2gcoder.blog.post.infra.persistence.FakePostRepository;
import com.y2gcoder.blog.post.infra.persistence.FakeTagRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PostServiceTest {

    private PostService sut;
    private PostRepository postRepository;
    private TagRepository tagRepository;
    private LocalDateTimeHolder localDateTimeHolder;

    @BeforeEach
    void beforeEach() {
        postRepository = new FakePostRepository();
        tagRepository = new FakeTagRepository();
        localDateTimeHolder = new TestLocalDateTimeHolder(
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
        sut.write(title, content, tagNames);

        //then
        FakePostQueryRepository postQueryRepository = new FakePostQueryRepository(
                ((FakePostRepository) postRepository).getStore());
        List<Post> result = postQueryRepository.findAll();

        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getTitle()).isEqualTo(title);
        assertThat(result.get(0).getPostingTags().getTags().size()).isEqualTo(3);
        assertThat(result.get(0).getPostingTags().getTags().stream().map(Tag::getName).collect(
                Collectors.toList())).containsAll(tagNames);

    }
}