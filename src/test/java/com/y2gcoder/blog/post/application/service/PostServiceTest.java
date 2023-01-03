package com.y2gcoder.blog.post.application.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.y2gcoder.blog.post.domain.Post;
import com.y2gcoder.blog.post.domain.Tag;
import com.y2gcoder.blog.post.domain.Tagger;
import com.y2gcoder.blog.post.infra.persistence.FakePostRepository;
import com.y2gcoder.blog.post.infra.persistence.FakeTagRepository;
import com.y2gcoder.blog.post.infra.persistence.FakeTaggerRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PostServiceTest {

    private PostService sut;
    private PostRepository postRepository;
    private TagRepository tagRepository;
    private TaggerRepository taggerRepository;

    @BeforeEach
    void beforeEach() {
        postRepository = new FakePostRepository();
        tagRepository = new FakeTagRepository();
        taggerRepository = new FakeTaggerRepository();
        LocalDateTimeHolder localDateTimeHolder = new TestLocalDateTimeHolder(
                LocalDateTime.of(2022, 12, 29, 13, 48, 26)
        );
        sut = new PostService(postRepository, tagRepository, taggerRepository, localDateTimeHolder);
    }

    @Test
    @DisplayName("태그가_없는_포스트를_작성할_수_있다.")
    void givenNoTags_whenWrite_thenSuccess() {
        //given
        String title = "title";
        String content = "content";
        List<String> tagNames = new ArrayList<>();

        //when
        Long savedPostId = sut.write(title, content, tagNames);

        //then
        List<Post> result = ((FakePostRepository) postRepository).getStore();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.stream()
                .anyMatch(domain -> domain.getId().getValue().equals(savedPostId))).isTrue();
    }

    @Test
    @DisplayName("태그와_함께_포스트를_작성할_수_있다.")
    void given5Tags_whenWrite_thenSuccess() {
        //given
        String title = "title";
        String content = "content";
        List<String> tagNames = new ArrayList<>();
        for (int i = 1; i < 6; i++) {
            tagNames.add("tag " + i);
        }

        //when
        Long savedPostId = sut.write(title, content, tagNames);

        //then
        List<Post> posts = ((FakePostRepository) postRepository).getStore();
        List<Tag> tags = ((FakeTagRepository) tagRepository).getStore();
        List<Tagger> taggers = ((FakeTaggerRepository) taggerRepository).getStore();

        assertThat(posts.size()).isEqualTo(1);
        assertThat(posts.stream()
                .anyMatch(domain -> domain.getId().getValue().equals(savedPostId))).isTrue();

        Optional<Tagger> optionalTagger = taggers.stream()
                .filter(tagger -> tagger.getPostId().getValue().equals(savedPostId))
                .findAny();
        assertThat(optionalTagger.isPresent()).isTrue();
        Tagger tagger = optionalTagger.get();
        assertThat(tagger.getTags().size()).isEqualTo(tags.size());

    }

}