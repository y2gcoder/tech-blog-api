package com.y2gcoder.blog.post.application.service;

import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
public class SystemLocalDateTimeHolder implements LocalDateTimeHolder {

    @Override
    public LocalDateTime now() {
        return LocalDateTime.now();
    }
}
