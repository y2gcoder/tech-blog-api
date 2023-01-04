package com.y2gcoder.blog.post.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.y2gcoder.blog.post.domain.Post;
import com.y2gcoder.blog.post.domain.Post.PostId;
import com.y2gcoder.blog.post.domain.PostNotFoundException;
import com.y2gcoder.blog.post.domain.Tag;
import com.y2gcoder.blog.post.domain.Tagger;
import com.y2gcoder.blog.post.infra.persistence.FakePostRepository;
import com.y2gcoder.blog.post.infra.persistence.FakeTagRepository;
import com.y2gcoder.blog.post.infra.persistence.FakeTaggerRepository;
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

    @Test
    @DisplayName("이미_있는_태그와_함께_포스트를_저장할_수_있다.")
    void given3TagsAlreadyInTheRepositoryAnd2NewTags_whenWrite_thenSuccess() {
        //given
        String title1 = "title1";
        String content1 = "content1";
        List<String> tagNames1 = new ArrayList<>();
        for (int i = 1; i < 6; i++) {
            tagNames1.add("tag " + i);
        }
        sut.write(title1, content1, tagNames1);

        //when
        String title2 = "title2";
        String content2 = "content2";

        List<String> tagNames2 = new ArrayList<>();
        for (int i = 3; i < 8; i++) {
            tagNames2.add("tag " + i);
        }
        Long savedPostId = sut.write(title2, content2, tagNames2);


        //then
        List<Post> posts = ((FakePostRepository) postRepository).getStore();
        List<Tag> tags = ((FakeTagRepository) tagRepository).getStore();
        List<Tagger> taggers = ((FakeTaggerRepository) taggerRepository).getStore();
        assertThat(posts.size()).isEqualTo(2);

        assertThat(tags.size()).isEqualTo(7);

        Optional<Tagger> optionalTagger = taggers.stream()
                .filter(tagger -> tagger.getPostId().getValue().equals(savedPostId))
                .findAny();

        assertThat(optionalTagger.isPresent()).isTrue();
        Tagger tagger = optionalTagger.get();
        List<Tag> taggerTags = tagger.getTags();
        assertThat(taggerTags.size()).isEqualTo(5);
        assertThat(taggerTags.stream().map(Tag::getName).collect(Collectors.toList())).containsAll(
                tagNames2);

    }

    @Test
    @DisplayName("존재하지_않는_포스트를_삭제할_수_없다.")
    void givenInvalidPostId_whenDelete_thenThrowException() {
        //given
        String title = "title";
        String content = "content";
        List<String> tagNames = new ArrayList<>();
        Long savedPostId = sut.write(title, content, tagNames);

        //when
        //then
        assertThatThrownBy(() -> sut.delete(savedPostId + 1L))
                .isInstanceOf(PostNotFoundException.class);

    }

    @Test
    @DisplayName("태그가_없는_포스트를_삭제할_수_있다.")
    void givenPostWithoutTag_whenDelete_thenSuccess() {
        //given
        String title = "title";
        String content = "content";
        List<String> tagNames = new ArrayList<>();
        Long savedPostId = sut.write(title, content, tagNames);
        List<Post> before = ((FakePostRepository) postRepository).getStore();
        assertThat(before).isNotEmpty();

        //when
        sut.delete(savedPostId);

        //then
        List<Post> after = ((FakePostRepository) postRepository).getStore();
        assertThat(after).isEmpty();
    }

    @Test
    @DisplayName("태그가_있는_포스트를_삭제할_수_있다.")
    void givenPostWithTags_whenDelete_thenSuccess() {
        //given
        String title = "title";
        String content = "content";
        List<String> tagNames = new ArrayList<>();
        for (int i = 1; i < 6; i++) {
            tagNames.add("tag " + i);
        }
        Long savedPostId = sut.write(title, content, tagNames);

        List<Post> beforePosts = ((FakePostRepository) postRepository).getStore();
        assertThat(beforePosts).isNotEmpty();
        List<Tag> beforeTags = ((FakeTagRepository) tagRepository).getStore();
        assertThat(beforeTags.size()).isEqualTo(5);
        List<Tagger> beforeTaggers = ((FakeTaggerRepository) taggerRepository).getStore();
        assertThat(beforeTaggers).isNotEmpty();

        //when
        sut.delete(savedPostId);

        //then
        List<Post> afterPosts = ((FakePostRepository) postRepository).getStore();
        assertThat(afterPosts).isEmpty();
        List<Tag> afterTags = ((FakeTagRepository) tagRepository).getStore();
        assertThat(afterTags.size()).isEqualTo(5);
        List<Tagger> afterTaggers = ((FakeTaggerRepository) taggerRepository).getStore();
        assertThat(afterTaggers).isEmpty();
    }

    @Test
    @DisplayName("포스트의_타이틀과_제목을_수정할_수_있다.")
    void givenChangedTitleAndContent_whenEdit_thenEdited() {
        //given
        String title = "title";
        String content = "content";
        List<String> tagNames = new ArrayList<>();
        for (int i = 1; i < 6; i++) {
            tagNames.add("tag " + i);
        }
        Long savedPostId = sut.write(title, content, tagNames);
        List<Post> beforePosts = ((FakePostRepository) postRepository).getStore();
        assertThat(beforePosts).isNotEmpty();
        assertThat(beforePosts.get(0).getTitle()).isEqualTo(title);
        assertThat(beforePosts.get(0).getContent()).isEqualTo(content);

        //when
        String newTitle = "newTitle";
        String newContent = "newContent";
        sut.edit(savedPostId, newTitle, newContent, tagNames);

        //then
        List<Post> afterPosts = ((FakePostRepository) postRepository).getStore();
        Optional<Post> optionalPost = afterPosts.stream()
                .filter(post -> post.getId().equals(new PostId(savedPostId))).findAny();
        assertThat(optionalPost).isPresent();
        Post result = optionalPost.get();
        assertThat(result.getTitle()).isEqualTo(newTitle);
        assertThat(result.getContent()).isEqualTo(newContent);

        List<Tagger> afterTaggers = ((FakeTaggerRepository) taggerRepository).getStore();
        Optional<Tagger> optionalTagger = afterTaggers.stream()
                .filter(tagger -> tagger.getPostId().equals(new PostId(savedPostId)))
                .findAny();
        assertThat(optionalTagger).isPresent();
        assertThat(optionalTagger.get().getTags().size()).isEqualTo(5);
    }

    @Test
    @DisplayName("포스트의_태그를_수정할_수_있다.")
    void givenChangedTags_whenEdit_thenEdited() {
        //given
        String title = "title";
        String content = "content";
        List<String> tagNames = new ArrayList<>();
        for (int i = 1; i < 6; i++) {
            tagNames.add("tag " + i);
        }
        Long savedPostId = sut.write(title, content, tagNames);
        List<Post> beforePosts = ((FakePostRepository) postRepository).getStore();
        assertThat(beforePosts).isNotEmpty();
        List<Tag> beforeTags = ((FakeTagRepository) tagRepository).getStore();
        assertThat(beforeTags.size()).isEqualTo(5);
        List<Tagger> beforeTaggers = ((FakeTaggerRepository) taggerRepository).getStore();
        assertThat(beforeTaggers).isNotEmpty();

        //when
        List<String> newTagNames = new ArrayList<>();
        for (int i = 4; i < 9; i++) {
            newTagNames.add("tag " + i);
        }
        sut.edit(savedPostId, title, content, newTagNames);

        //then
        List<Post> afterPosts = ((FakePostRepository) postRepository).getStore();
        assertThat(afterPosts).isNotEmpty();
        List<Tag> afterTags = ((FakeTagRepository) tagRepository).getStore();
        assertThat(afterTags.size()).isEqualTo(8);
        List<Tagger> afterTaggers = ((FakeTaggerRepository) taggerRepository).getStore();
        Optional<Tagger> optionalTagger = afterTaggers.stream()
                .filter(tagger -> tagger.getPostId().equals(new PostId(savedPostId)))
                .findAny();
        assertThat(optionalTagger).isPresent();
        Tagger tagger = optionalTagger.get();
        assertThat(tagger.getTags().stream().map(Tag::getName)
                .collect(Collectors.toList())).containsAll(newTagNames);
    }

}