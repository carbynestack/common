/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/common.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.common.result;

import io.carbynestack.testing.nullable.NullableParamSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Predicate;

import static io.carbynestack.testing.result.ResultAssert.assertThat;
import static java.util.function.Function.identity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SuccessTest {
    @SuppressWarnings("unused")
    private static final Arguments FOLD = Arguments.of(identity(), identity());

    private final int value = 12;
    private final Result<Integer, Integer> result = new Success<>(value);

    @Test
    void isSuccess() {
        assertThat(result).isInstanceOf(Result.class);
        assertThat(result.isSuccess()).isTrue();
        assertThat(result).isSuccess();
        assertThat(result.isFailure()).isFalse();
    }

    @Test
    void value() {
        assertThat(result).hasValue(value);
    }

    @Test
    void map() {
        assertThat(result.map(v -> v * 2)).hasValue(24);
    }

    @Test
    void mapNullPointerException() {
        assertThatThrownBy(() -> result.map(null))
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    void mapAndTransformType() {
        assertThat(result.map(v -> String.format("%s * 2 -> %s", v, v * 2)))
                .hasValue("12 * 2 -> 24");
    }

    @Test
    void peek() {
        AtomicInteger output = new AtomicInteger(-1);
        result.peek(output::set);
        assertThat(output).hasValue(value);
    }

    @Test
    void peekNullPointerException() {
        assertThatThrownBy(() -> result.peek(null))
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    void recover() {
        assertThat(result.recover(identity())).hasValue(value);
    }

    @Test
    void recoverNullPointerException() {
        assertThatThrownBy(() -> result.recover(null))
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    void flatMap() {
        assertThat(result.flatMap(v -> new Success<>(v * 2))).hasValue(24);
        assertThat(result.flatMap(v -> new Failure<>(v + 9))).hasReason(21);
    }

    @Test
    void flatMapNullPointerException() {
        assertThatThrownBy(() -> result.flatMap(null))
                .isExactlyInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> result.flatMap(v -> null))
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    void fold() {
        assertThat(result.<Integer>fold(v -> v * 2, r -> r + 9)).isEqualTo(24);
    }

    @ParameterizedTest
    @NullableParamSource("FOLD")
    void foldNullPointerException(Function<? super Integer, ? super Integer> successFunction,
                                  Function<? super Integer, ? super Integer> failureFunction) {
        assertThatThrownBy(() -> result.fold(successFunction, failureFunction))
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    void filter() {
        assertThat(result.filter(Predicate.isEqual(value), 21)).hasValue(value);
        assertThat(result.filter(v -> !Objects.equals(v, value), 21)).hasReason(21);
    }

    @Test
    void filterNullPointerException() {
        assertThatThrownBy(() -> result.filter(null, null))
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    void or() {
        assertThat(result.or(() -> new Success<>(24))).hasValue(value);
    }

    @Test
    void orNullPointerException() {
        assertThatThrownBy(() -> result.or(null))
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    void toOptional() {
        assertThat(result.toOptional()).hasValue(value);
    }

    @Test
    void stream() {
        assertThat(result.stream()).containsExactly(value);
    }
}
