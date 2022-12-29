package com.y2gcoder.blog.post.application.service;

import java.time.LocalDateTime;

public class TestLocalDateTimeHolder implements LocalDateTimeHolder {

    private final LocalDateTime localDateTime;

    public TestLocalDateTimeHolder(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    @Override
    public LocalDateTime now() {
        return localDateTime;
    }
}
