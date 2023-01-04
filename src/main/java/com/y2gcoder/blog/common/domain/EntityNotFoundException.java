package com.y2gcoder.blog.common.domain;

import com.y2gcoder.blog.common.error.ErrorCode;
import com.y2gcoder.blog.common.error.exception.BusinessException;

public class EntityNotFoundException extends BusinessException {

    public EntityNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
