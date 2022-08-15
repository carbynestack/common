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
    void givenSuccessResultWhenCallingIsSuccessOnResultThenReturnTrue() {
        assertThat(result).isInstanceOf(Result.class);
        assertThat(result.isSuccess()).isTrue();
        assertThat(result).isSuccess();
        assertThat(result.isFailure()).isFalse();
    }

    @Test
    void givenSuccessResultWhenCallingValueOnResultThenReturnExpectedValue() {
        assertThat(result).hasValue(value);
    }

    @Test
    void givenSuccessResultWhenCallingMapOnResultThenReturnMappedResult() {
        assertThat(result.map(v -> v * 2)).hasValue(24);
    }

    @Test
    void givenMappingFunctionIsNullWhenCallingMapOnResultThenThrowNullPointerException() {
        assertThatThrownBy(() -> result.map(null))
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    void givenSuccessResultWhenCallingMapOnResultThenReturnMappedResultWithTransformedType() {
        assertThat(result.map(v -> String.format("%s * 2 -> %s", v, v * 2)))
                .hasValue("12 * 2 -> 24");
    }

    @Test
    void givenSuccessResultWhenCallingMapFailureOnResultThenReturnResultWithMappedFailureReason() {
        assertThat(result.mapFailure(r -> r * 2)).hasValue(value);
    }

    @Test
    void givenMappingFunctionIsNullWhenCallingMapFailureOnResultThenThrowNullPointerException() {
        assertThatThrownBy(() -> result.mapFailure(null))
              .isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    void givenSuccessResultWhenCallingTryMapOnResultThenReturnMappedResult() {
        assertThat(result.tryMap(v -> v * 2, -11)).hasValue(24);
    }

    @Test
    void givenMappingFunctionsIsNullWhenCallingTryMapOnResultThenThrowNullPointerException() {
        assertThatThrownBy(() -> result.tryMap(null, -11))
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    void givenSuccessResultWhenCallingMapFailureOnResultThenReturnResultWithMappedFailureReasonAndTransformedType() {
        assertThat(result.mapFailure(r -> String.format("%s * 2 -> %s", r, r * 2)))
                .hasValue(value);
    }

    @Test
    void givenSuccessResultWhenCallingTryMapOnResultThenReturnMappedResultAndTransformedType() {
        assertThat(result.tryMap(v -> String.format("%s * 2 -> %s", v, v * 2),
                -11)).hasValue("12 * 2 -> 24");
    }

    @Test
    void givenThrowingMappingFunctionWhenCallingTryMapOnResultThenReturnFailureResult() {
        int reason = -11;
        assertThat(result.tryMap(v -> {
            throw new IOException("-11");
        }, reason)).hasReason(reason);
    }

    @Test
    void givenSuccessResultWhenCallingPeekOnResultThenOutputSuccessValue() {
        AtomicInteger output = new AtomicInteger(-1);
        result.peek(output::set);
        assertThat(output).hasValue(value);
    }

    @Test
    void givenOutputFunctionIsNullWhenCallingPeekOnResultThenThrowNullPointerException() {
        assertThatThrownBy(() -> result.peek(null))
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    void givenSuccessResultWhenCallingTryPeekOnResultThenOutputSuccessValue() {
        AtomicInteger output = new AtomicInteger(-1);
        result.tryPeek(output::set, -11);
        assertThat(output).hasValue(value);
    }

    @Test
    void givenThrowingOutputFunctionWhenCallingTryPeekOnResultThenReturnFailureResult() {
        int reason = -11;
        assertThat(result.tryPeek(v -> {
            throw new IOException("-11");
        }, reason)).hasReason(reason);
    }

    @Test
    void givenOutputFunctionIsNullWhenCallingTryPeekOnResultThenThrowNullPointerException() {
        assertThatThrownBy(() -> result.tryPeek(null, -11))
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    void givenSuccessResultWhenCallingRecoverOnResultThenReturnCurrentResult() {
        assertThat(result.recover(identity())).hasValue(value);
    }

    @Test
    void givenRecoveryFunctionIsNullWhenCallingRecoverOnResultThenThrowNullPointerException() {
        assertThatThrownBy(() -> result.recover(null))
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    void givenSuccessResultWhenCallingFlatMapOnResultThenReturnMappedResult() {
        assertThat(result.flatMap(v -> new Success<>(v * 2))).hasValue(24);
        assertThat(result.flatMap(v -> new Failure<>(v + 9))).hasReason(21);
    }

    @Test
    void givenMappingFunctionWhenCallingFlatMapOnResultThenThrowNullPointerException() {
        assertThatThrownBy(() -> result.flatMap(null))
                .isExactlyInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> result.flatMap(v -> null))
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    void givenOutermostNestedSuccessResultWhenCallingUnsafeFlattenOnResultThenReturnFlattenedResult() {
        int value = 12;
        Result<Result<Integer, Integer>, Integer> res = new Success<>(new Success<>(value));
        assertThat(res.unsafeFlatten()).hasValue(value);
    }

    @Test
    void givenOutermostSuccessResultWithNestedFailureResultWhenCallingUnsafeFlattenOnResultThenReturnFlattenedResult() {
        int reason = 21;
        Result<Result<Integer, Integer>, Integer> res = new Success<>(new Failure<>(reason));
        assertThat(res.unsafeFlatten()).hasReason(reason);
    }

    @Test
    void givenNonNestedSuccessResultWhenCallingUnsafeFlattenOnResultThenReturnNonNestedSuccessResult() {
        int value = 12;
        Result<Integer, Integer> res = new Success<>(value);
        assertThat(res.unsafeFlatten()).hasValue(value);
    }

    @Test
    void givenSuccessResultWhenCallingFoldOnResultThenReturnExpectedValue() {
        assertThat(result.<Integer>fold(v -> v * 2, r -> r + 9)).isEqualTo(24);
    }

    @ParameterizedTest
    @NullableParamSource("FOLD")
    void givenFoldingFunctionsAreNullWhenCallingFoldOnResultThenThrowNullPointerException(
            Function<? super Integer, ? super Integer> successFunction,
            Function<? super Integer, ? super Integer> failureFunction) {
        assertThatThrownBy(() -> result.fold(successFunction, failureFunction))
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    void givenSuccessResultWhenCallingFilterOnResultThenReturnFilteredResult() {
        assertThat(result.filter(Predicate.isEqual(value), 21)).hasValue(value);
        assertThat(result.filter(v -> !Objects.equals(v, value), 21)).hasReason(21);
    }

    @Test
    void givenPredicateIsNullWhenCallingFilterOnResultThenThrowNullPointerException() {
        assertThatThrownBy(() -> result.filter(null, null))
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    void givenSuccessResultWhenCallingOrOnResultThenReturnCurrentSuccessResult() {
        assertThat(result.or(() -> new Success<>(24))).hasValue(value);
    }

    @Test
    void givenResultSupplierIsNullWhenCallingOrOnResultThenThrowNullPointerException() {
        assertThatThrownBy(() -> result.or(null))
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    void givenSuccessResultWhenCallingSwapOnResultThenReturnFailureResult() {
        assertThat(result.swap()).hasReason(value);
    }

    @Test
    void givenSuccessResultWhenCallingToOptionalOnResultThenReturnOptionalWithSuccessValue() {
        assertThat(result.toOptional()).hasValue(value);
    }

    @Test
    void givenSuccessResultWhenCallingStreamOnResultThenReturnStreamContainingSuccessValue() {
        assertThat(result.stream()).containsExactly(value);
    }
}
