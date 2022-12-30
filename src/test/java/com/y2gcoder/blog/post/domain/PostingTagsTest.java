package com.y2gcoder.blog.post.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.y2gcoder.blog.post.domain.Tag.TagId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PostingTagsTest {

    @Test
    @DisplayName("포스팅_태그에_태그를_추가할_수_있다.")
    void given1Tag_whenAddToPostingTagsWhereHad2Tags_thenPostingTagsHas3Tags() {
        //given
        Tag tag1 = new Tag(new TagId(1L), "tag1");
        Tag tag2 = new Tag(new TagId(2L), "tag2");
        PostingTags sut = new PostingTags(tag1, tag2);

        //when
        Tag tag3 = new Tag(new TagId(3L), "tag3");
        sut.addTag(tag3);

        //then
        List<Tag> result = sut.getTags();
        assertThat(result.size()).isEqualTo(3);
        assertThat(result).contains(tag3);
    }

    @Test
    @DisplayName("포스팅_태그에_태그를_여러_개_추가할_수_있다.")
    void givenPostingTagsHaving2Tags_whenAdd3Tags_then5tags() {
        //given
        Tag tag1 = new Tag(new TagId(1L), "tag1");
        Tag tag2 = new Tag(new TagId(2L), "tag2");
        PostingTags sut = new PostingTags(tag1, tag2);

        //when
        Tag tag3 = new Tag(new TagId(3L), "tag3");
        Tag tag4 = new Tag(new TagId(4L), "tag4");
        Tag tag5 = new Tag(new TagId(5L), "tag5");
        sut.addTags(tag3, tag4, tag5);

        List<Tag> result = sut.getTags();
        assertThat(result.size()).isEqualTo(5);
        assertThat(result).containsExactly(tag1, tag2, tag3, tag4, tag5);
    }

    @Test
    @DisplayName("포스팅_태그에_태그를_제거할_수_있다.")
    void givenPostingTagsHaving3Tags_whenRemove1Tag_thenRemain2Tags() {
        //given
        Tag tag1 = new Tag(new TagId(1L), "tag1");
        Tag tag2 = new Tag(new TagId(2L), "tag2");
        Tag tag3 = new Tag(new TagId(3L), "tag3");
        PostingTags sut = new PostingTags(new ArrayList<>(Arrays.asList(tag1, tag2, tag3)));

        //when
        sut.removeTag(tag2);

        //then
        List<Tag> result = sut.getTags();
        assertThat(result.size()).isEqualTo(2);
        assertThat(result).containsExactly(tag1, tag3);
    }

    @Test
    @DisplayName("포스팅_태그에_태그_목록을_NULL로_주면_빈_태그_목록을_만든다.")
    void givenNull_whenMakeInstance_thenEmptyTagList() {
        List<Tag> nullTags = null;
        PostingTags sut = new PostingTags(nullTags);

        //then
        List<Tag> result = sut.getTags();
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("포스팅_태그에서_태그_중_null이 있으면_null은_제외한다.")
    void givenNoTags_whenMakeInstance_thenEmptyTagList() {
        Tag tag1 = new Tag(new TagId(1L), "tag1");
        PostingTags sut = new PostingTags(null, tag1);

        //then
        List<Tag> result = sut.getTags();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result).contains(tag1);
    }

}