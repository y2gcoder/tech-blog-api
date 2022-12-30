package com.y2gcoder.blog.post.presentation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagDto {
    private Long tagId;
    private String name;
}
