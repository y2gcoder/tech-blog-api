package com.y2gcoder.blog.post.domain;

import com.y2gcoder.blog.common.domain.EntityNotFoundException;
import com.y2gcoder.blog.common.error.ErrorCode;

public class PostNotFoundException extends EntityNotFoundException {

    public PostNotFoundException() {
        super(ErrorCode.NOT_FOUND_POST);
    }
}
