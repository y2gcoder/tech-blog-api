package com.y2gcoder.blog.post.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.y2gcoder.blog.post.domain.Post.PostId;
import java.time.LocalDateTime;
import java.util.ArrayList;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PostTest {

    @Test
    @DisplayName("빈_제목으로_포스트를_생성할_수_없다.")
    void givenBlankTitle_whenPostOf_thenFail() {
        //given
        String title = "";
        String content = "content";
        LocalDateTime writtenAt = LocalDateTime
                .of(2022, 12, 28, 17, 38, 30);
        PostingTags postingTags = new PostingTags(new ArrayList<>());

        //when
        //then
        assertThatThrownBy(
                () -> Post.of(new PostId(1L), title, content, writtenAt, postingTags)
        )
                .isInstanceOf(IllegalArgumentException.class);


    }

    @Test
    @DisplayName("빈_내용으로_포스트를_생성할_수_없다.")
    void givenBlankContent_whenPostOf_thenFail() {
        //given
        String title = "title";
        String content = "";
        LocalDateTime writtenAt = LocalDateTime
                .of(2022, 12, 28, 17, 38, 30);
        PostingTags postingTags = new PostingTags(new ArrayList<>());

        //when
        //then
        assertThatThrownBy(
                () -> Post.of(new PostId(1L), title, content, writtenAt, postingTags)
        )
                .isInstanceOf(IllegalArgumentException.class);


    }

    @Test
    @DisplayName("포스트_제목과_내용을_변경할_수_있다.")
    void givenTitleAndContent_whenEdit_thenEditedPost() {
        //given
        String title = "title";
        String content = "content";
        LocalDateTime writtenAt = LocalDateTime
                .of(2022, 12, 28, 17, 38, 30);
        PostingTags postingTags = new PostingTags(new ArrayList<>());
        Post sut = Post.of(new PostId(1L), title, content, writtenAt, postingTags);

        //when
        String newTitle = "newTitle";
        String newContent = "newContent";
        sut.edit(newTitle, newContent);

        //then
        assertThat(sut.getTitle()).isEqualTo(newTitle);
        assertThat(sut.getContent()).isEqualTo(newContent);
    }

    @Test
    @DisplayName("빈_제목으로_변경할_수_없다.")
    void givenBlankTitle_whenEdit_thenNotEditedTitle() {
        //given
        String title = "title";
        String content = "content";
        LocalDateTime writtenAt = LocalDateTime
                .of(2022, 12, 28, 17, 38, 30);
        PostingTags postingTags = new PostingTags(new ArrayList<>());
        Post sut = Post.of(new PostId(1L), title, content, writtenAt, postingTags);

        //when
        String newTitle = "";
        String newContent = "newContent";
        sut.edit(newTitle, newContent);

        //then
        assertThat(sut.getTitle()).isEqualTo(title);
        assertThat(sut.getContent()).isEqualTo(newContent);
    }

    @Test
    @DisplayName("빈_내용으로_변경할_수_없다.")
    void givenBlankContent_whenEdit_thenNotEditedContent() {
        //given
        String title = "title";
        String content = "content";
        LocalDateTime writtenAt = LocalDateTime
                .of(2022, 12, 28, 17, 38, 30);
        PostingTags postingTags = new PostingTags(new ArrayList<>());
        Post sut = Post.of(new PostId(1L), title, content, writtenAt, postingTags);

        //when
        String newTitle = "newTitle";
        String newContent = "";
        sut.edit(newTitle, newContent);

        //then
        assertThat(sut.getTitle()).isEqualTo(newTitle);
        assertThat(sut.getContent()).isEqualTo(content);
    }

}