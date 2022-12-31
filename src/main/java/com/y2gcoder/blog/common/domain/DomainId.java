package com.y2gcoder.blog.common.domain;

import java.util.Objects;
import org.springframework.util.ObjectUtils;

public abstract class DomainId {
    private final Long value;

    public DomainId(Long value) {
        if (ObjectUtils.isEmpty(value)) {
            throw new IllegalArgumentException("id value should be null");
        }
        if (value <= 0) {
            throw new IllegalArgumentException("id value should be greater than 0");
        }
        this.value = value;
    }

    public Long getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DomainId domainId = (DomainId) o;
        return value.equals(domainId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
