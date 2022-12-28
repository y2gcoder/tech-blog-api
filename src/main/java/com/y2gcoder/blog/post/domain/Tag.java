package com.y2gcoder.blog.post.domain;


import java.util.Objects;
import org.springframework.util.StringUtils;


public class Tag {
    private final TagId id;

    private final String name;

    public Tag(TagId id, String name) {
        assert StringUtils.hasText(name);

        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Tag tag = (Tag) o;
        return Objects.equals(id, tag.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public static class TagId {
        private final Long value;

        public TagId(Long value) {
            assert value > 0;
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            TagId tagId = (TagId) o;
            return value.equals(tagId.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }
    }

}
