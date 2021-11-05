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

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Predicate;

import static io.carbynestack.testing.result.ResultAssert.assertThat;
import static java.util.function.Function.identity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class FailureTest {
    @SuppressWarnings("unused")
    public static final Arguments FOLD = Arguments.of(identity(), identity());

    public final int reason = 21;
    public final Result<Integer, Integer> result = new Failure<>(reason);

    @Test
    public void isFailure() {
        assertThat(result).isInstanceOf(Result.class);
        assertThat(result.isFailure()).isTrue();
        assertThat(result).isFailure();
        assertThat(result.isSuccess()).isFalse();
    }

    @Test
    public void reason() {
        assertThat(result).hasReason(reason);
    }

    @Test
    public void map() {
        assertThat(result.map(v -> v * 2)).hasReason(reason);
    }

    @Test
    public void mapNullPointerException() {
        assertThatThrownBy(() -> result.map(null))
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    public void mapAndTransformType() {
        assertThat(result.map(v -> String.format("%s * 2 -> %s", v, v * 2)))
                .hasReason(reason);
    }

    @Test
    public void peek() {
        AtomicInteger output = new AtomicInteger(-1);
        result.peek(output::set);
        assertThat(output).hasValue(-1);
    }

    @Test
    public void peekNullPointerException() {
        assertThatThrownBy(() -> result.peek(null))
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    public void recover() {
        assertThat(result.recover(r -> r * 2)).hasValue(42);
    }

    @Test
    public void recoverNullPointerException() {
        assertThatThrownBy(() -> result.recover(null))
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    public void flatMap() {
        assertThat(result.flatMap(v -> new Success<>(v * 2))).hasReason(reason);
        assertThat(result.flatMap(v -> new Failure<>(v * 2))).hasReason(reason);
    }

    @Test
    public void flatMapNullPointerException() {
        assertThatThrownBy(() -> result.flatMap(null))
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    public void fold() {
        assertThat(result.<Integer>fold(r -> r + 9, v -> v * 2)).isEqualTo(30);
    }

    @ParameterizedTest
    @NullableParamSource("FOLD")
    public void foldNullPointerException(Function<? super Integer, ? super Integer> failureFunction,
                                         Function<? super Integer, ? super Integer> successFunction) {
        assertThatThrownBy(() -> result.fold(failureFunction, successFunction))
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    public void filter() {
        assertThat(result.filter(Predicate.isEqual(12), 22)).hasReason(reason);
    }

    @Test
    public void filterNullPointerException() {
        assertThatThrownBy(() -> result.filter(null, null))
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    public void or() {
        int value = 12;
        assertThat(result.or(() -> new Success<>(value))).hasValue(value);
    }

    @Test
    public void orNullPointerException() {
        assertThatThrownBy(() -> result.or(null))
                .isExactlyInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> result.or(() -> null))
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    public void toOptional() {
        assertThat(result.toOptional()).isEmpty();
    }

    @Test
    public void stream() {
        assertThat(result.stream()).isEmpty();
    }
}
