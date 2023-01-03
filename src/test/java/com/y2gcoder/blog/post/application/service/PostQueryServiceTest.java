package com.y2gcoder.blog.post.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.y2gcoder.blog.post.domain.Post;
import com.y2gcoder.blog.post.domain.Post.PostId;
import com.y2gcoder.blog.post.domain.PostWithTags;
import com.y2gcoder.blog.post.domain.Tag;
import com.y2gcoder.blog.post.domain.Tagger;
import com.y2gcoder.blog.post.infra.persistence.FakePostQueryRepository;
import com.y2gcoder.blog.post.infra.persistence.FakePostRepository;
import com.y2gcoder.blog.post.infra.persistence.FakeTagRepository;
import com.y2gcoder.blog.post.infra.persistence.FakeTaggerRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import javax.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PostQueryServiceTest {

    private PostQueryService sut;
    private PostRepository postRepository;
    private TaggerRepository taggerRepository;

    private List<Post> posts;

    @BeforeEach
    void beforeEach() {
        postRepository = new FakePostRepository();
        TagRepository tagRepository = new FakeTagRepository();
        taggerRepository = new FakeTaggerRepository();
        List<String> tagNames = LongStream.range(1, 6).mapToObj(l -> "tag " + l)
                .collect(Collectors.toList());
        List<Tag> tags = tagRepository.saveAll(tagNames);
        LocalDateTimeHolder localDateTimeHolder = new TestLocalDateTimeHolder(
                LocalDateTime.of(2022, 12, 29, 13, 48, 26)
        );
        posts = LongStream.range(1, 3).mapToObj(l -> {
            String title = "title " + l;
            String content = "content " + l;
            return postRepository.savePost(title, content, localDateTimeHolder.now());
        }).collect(Collectors.toList());
        //postId 1 post에 태그 5개 태깅
        PostId postId = posts.get(0).getId();
        taggerRepository.taggingPost(postId, tags);
    }

    @Test
    @DisplayName("포스트_ID로_포스트를_찾을_수_있다.")
    void givenPostId_whenGetByPostId_thenReturnPostWithTags() {
        //given
        PostId postId = posts.get(0).getId();

        //when
        List<Post> postStore = ((FakePostRepository) postRepository).getStore();
        List<Tagger> taggerStore = ((FakeTaggerRepository) taggerRepository).getStore();
        sut = new PostQueryService(new FakePostQueryRepository(postStore, taggerStore));
        PostWithTags result = sut.getByPostId(postId.getValue());

        //then
        assertThat(result.getId()).isEqualTo(postId);
        assertThat(result.getTags().size()).isEqualTo(5);
    }

    @Test
    @DisplayName("해당_포스트_ID를_가지고_있는_포스트가_없다.")
    void givenWrongPostId_whenGetByPostId_thenThrowException() {
        //given
        Long wrongPostId = 3L;

        //when
        List<Post> postStore = ((FakePostRepository) postRepository).getStore();
        List<Tagger> taggerStore = ((FakeTaggerRepository) taggerRepository).getStore();
        sut = new PostQueryService(new FakePostQueryRepository(postStore, taggerStore));

        //then
        assertThatThrownBy(() -> sut.getByPostId(wrongPostId))
                .isInstanceOf(EntityNotFoundException.class);
    }

}