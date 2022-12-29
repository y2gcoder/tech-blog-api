package com.y2gcoder.blog.post.presentation;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import org.springframework.util.ObjectUtils;


@Getter
public class PostWriteRequest {

    @NotBlank(message = "포스트 제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "포스트 내용을 입력해주세요.")
    private String content;

    private List<String> tagNames;

    public PostWriteRequest(String title, String content, List<String> tagNames) {
        this.title = title;
        this.content = content;
        if (ObjectUtils.isEmpty(tagNames)) {
            tagNames = new ArrayList<>();
        }
        this.tagNames = tagNames;
    }
}
