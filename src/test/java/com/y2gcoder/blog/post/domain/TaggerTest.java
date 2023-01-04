package com.y2gcoder.blog.post.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.y2gcoder.blog.post.domain.Post.PostId;
import com.y2gcoder.blog.post.domain.Tag.TagId;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TaggerTest {

    @Test
    @DisplayName("포스트에_태그를_추가할_수_있다.")
    void givenOneTag_whenAddTag_thenAdded() {
        //given
        Tag tag1 = new Tag(new TagId(1L), "tag1");
        LocalDateTime writtenAt = LocalDateTime.of(2023, 1, 2, 23, 36, 15);
        Post post = Post.of(new PostId(1L), "title", "content", writtenAt);

        //when
        Tagger sut = new Tagger(post.getId(), new ArrayList<>());
        sut.addTag(tag1);

        //then
        assertThat(sut.getTags().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("태그_객체로_포스트에서_해당_태그를_제거할_수_있다.")
    void givenTag_whenRemoveTag_thenRemoved() {
        //given
        List<Tag> tags = new ArrayList<>();
        for (int i = 1; i < 11; i++) {
            tags.add(new Tag(new TagId((long) i), "tag " + i));
        }
        LocalDateTime writtenAt = LocalDateTime.of(2023, 1, 2, 23, 36, 15);
        Post post = Post.of(new PostId(1L), "title", "content", writtenAt);
        Tagger sut = new Tagger(post.getId(), tags);

        //when
        Tag target = tags.get(3);
        sut.removeTag(target);

        //then
        assertThat(sut.getTags().size()).isEqualTo(9);
        assertThat(sut.getTags()).doesNotContain(target);
    }

    @Test
    @DisplayName("태그_목록으로_포스트에서_태그들을_여러_개_추가할_수_있다.")
    void givenTagList_whenAddTags_thenAdded() {
        //given
        List<Tag> tags = new ArrayList<>();
        tags.add(new Tag(new TagId(1L), "tag 1"));
        LocalDateTime writtenAt = LocalDateTime.of(2023, 1, 2, 23, 36, 15);
        Post post = Post.of(new PostId(1L), "title", "content", writtenAt);
        Tagger sut = new Tagger(post.getId(), tags);

        //when
        List<Tag> addTags = new ArrayList<>();
        for (int i = 2; i < 11; i++) {
            addTags.add(new Tag(new TagId((long) i), "tag " + i));
        }
        sut.addTags(addTags);

        //then
        assertThat(sut.getTags().size()).isEqualTo(10);
        assertThat(sut.getTags()).containsAll(addTags);
    }

    @Test
    @DisplayName("조회한_태그_목록은_수정할_수_없다.")
    void whenGetTags_thenUnmodifiable() {
        //given
        List<Tag> tags = new ArrayList<>();
        for (int i = 1; i < 11; i++) {
            tags.add(new Tag(new TagId((long) i), "tag " + i));
        }
        LocalDateTime writtenAt = LocalDateTime.of(2023, 1, 2, 23, 36, 15);
        Post post = Post.of(new PostId(1L), "title", "content", writtenAt);
        Tagger sut = new Tagger(post.getId(), tags);

        //when
        //then
        List<Tag> sutTags = sut.getTags();
        assertThatThrownBy(sutTags::clear).isInstanceOf(UnsupportedOperationException.class);

    }

    @Test
    @DisplayName("이미_있는_태그를_중복으로_추가하지_않는다.")
    void givenTagAlreadyInThePost_whenAddTag_thenNotAdded() {
        //given
        List<Tag> tags = new ArrayList<>();
        for (int i = 1; i < 11; i++) {
            tags.add(new Tag(new TagId((long) i), "tag " + i));
        }
        LocalDateTime writtenAt = LocalDateTime.of(2023, 1, 2, 23, 36, 15);
        Post post = Post.of(new PostId(1L), "title", "content", writtenAt);
        Tagger sut = new Tagger(post.getId(), tags);

        //when
        Tag target = tags.get(0);
        sut.addTag(target);

        //then
        assertThat(sut.getTags().size()).isEqualTo(10);
        assertThat((int) sut.getTags().stream().filter(t -> t.equals(target)).count()).isEqualTo(1);
    }

}