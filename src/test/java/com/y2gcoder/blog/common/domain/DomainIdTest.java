package com.y2gcoder.blog.common.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DomainIdTest {

    private static class TestDomainId extends DomainId {
        public TestDomainId(Long value) {
            super(value);
        }
    }

    @Test
    @DisplayName("도메인_ID를_만들_수_있다.")
    void givenValue_whenCreateDomainId_thenSuccess() {
        //given
        Long value = 1L;

        //when
        TestDomainId sut = new TestDomainId(value);

        //then
        assertThat(sut.getValue()).isEqualTo(value);
    }

    @Test
    @DisplayName("도메인_ID는_null_값으로_만들_수_없다.")
    void givenNullValue_whenCreateDomainId_thenThrowException() {
        //given
        Long value = null;

        //when
        //then
        assertThatThrownBy(() -> new TestDomainId(value))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("도메인_ID는_0으로_만들_수_없다.")
    void givenZeroValue_whenCreateDomainId_thenThrowException() {
        //given
        Long value = 0L;

        //when
        //then
        assertThatThrownBy(() -> new TestDomainId(value))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("도메인_ID는_음수로_만들_수_없다.")
    void givenMinusValue_whenCreateDomainId_thenThrowException() {
        //given
        Long value = -1L;

        //when
        //then
        assertThatThrownBy(() -> new TestDomainId(value))
                .isInstanceOf(IllegalArgumentException.class);
    }
}