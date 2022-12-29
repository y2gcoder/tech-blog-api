package com.y2gcoder.blog.post.application.service;

import com.y2gcoder.blog.post.domain.Tag;
import java.util.List;

public interface TagRepository {

    List<Tag> saveAll(List<Tag> tags);
}
