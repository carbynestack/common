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

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Predicate;

import static io.carbynestack.testing.result.ResultAssert.assertThat;
import static java.util.function.Function.identity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FailureTest {
    @SuppressWarnings("unused")
    private static final Arguments FOLD = Arguments.of(identity(), identity());

    private final int reason = 21;
    private final Result<Integer, Integer> result = new Failure<>(reason);

    @Test
    void isFailure() {
        assertThat(result).isInstanceOf(Result.class);
        assertThat(result.isFailure()).isTrue();
        assertThat(result).isFailure();
        assertThat(result.isSuccess()).isFalse();
    }

    @Test
    void reason() {
        assertThat(result).hasReason(reason);
    }

    @Test
    void map() {
        assertThat(result.map(v -> v * 2)).hasReason(reason);
    }

    @Test
    void mapNullPointerException() {
        assertThatThrownBy(() -> result.map(null))
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    void mapAndTransformType() {
        assertThat(result.map(v -> String.format("%s * 2 -> %s", v, v * 2)))
                .hasReason(reason);
    }

    @Test
    void peek() {
        AtomicInteger output = new AtomicInteger(-1);
        result.peek(output::set);
        assertThat(output).hasValue(-1);
    }

    @Test
    void peekNullPointerException() {
        assertThatThrownBy(() -> result.peek(null))
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    void tryPeek() {
        AtomicInteger output = new AtomicInteger(-1);
        result.tryPeek(output::set, reason * 2);
        assertThat(output).hasValue(-1);
    }

    @Test
    void tryPeekWithException() {
        Result<Integer, Integer> res = result.tryPeek(v -> {
            throw new IOException("-11");
        }, reason * 2);
        assertThat(res).isFailure();
        assertThat(res).hasReason(reason);
    }

    @Test
    void tryPeekNullPointerException() {
        assertThatThrownBy(() -> result.tryPeek(null, reason * 2))
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    void recover() {
        assertThat(result.recover(r -> r * 2)).hasValue(42);
    }

    @Test
    void recoverNullPointerException() {
        assertThatThrownBy(() -> result.recover(null))
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    void flatMap() {
        assertThat(result.flatMap(v -> new Success<>(v * 2))).hasReason(reason);
        assertThat(result.flatMap(v -> new Failure<>(v * 2))).hasReason(reason);
    }

    @Test
    void flatMapNullPointerException() {
        assertThatThrownBy(() -> result.flatMap(null))
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    void fold() {
        assertThat(result.<Integer>fold(v -> v * 2, r -> r + 9)).isEqualTo(30);
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
        assertThat(result.filter(Predicate.isEqual(12), 22)).hasReason(reason);
    }

    @Test
    void filterNullPointerException() {
        assertThatThrownBy(() -> result.filter(null, null))
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    void or() {
        int value = 12;
        assertThat(result.or(() -> new Success<>(value))).hasValue(value);
    }

    @Test
    void orNullPointerException() {
        assertThatThrownBy(() -> result.or(null))
                .isExactlyInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> result.or(() -> null))
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    void swap() {
        assertThat(result.swap()).hasValue(reason);
    }

    @Test
    void toOptional() {
        assertThat(result.toOptional()).isEmpty();
    }

    @Test
    void stream() {
        assertThat(result.stream()).isEmpty();
    }
}
