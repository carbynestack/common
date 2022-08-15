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
    void givenFailureResultWhenCallingIsFailureOnResultThenReturnTrue() {
        assertThat(result).isInstanceOf(Result.class);
        assertThat(result.isFailure()).isTrue();
        assertThat(result).isFailure();
        assertThat(result.isSuccess()).isFalse();
    }

    @Test
    void givenFailureResultWhenCallingReasonOnResultThenReturnExpectedReason() {
        assertThat(result).hasReason(reason);
    }

    @Test
    void givenFailureResultWhenCallingMapOnResultThenReturnMappedResult() {
        assertThat(result.map(v -> v * 2)).hasReason(reason);
    }

    @Test
    void givenMappingFunctionIsNullWhenCallingMapOnResultThenThrowNullPointerException() {
        assertThatThrownBy(() -> result.map(null))
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    void givenFailureResultWhenCallingMapOnResultThenReturnMappedResultWithTransformedType() {
        assertThat(result.map(v -> String.format("%s * 2 -> %s", v, v * 2)))
                .hasReason(reason);
    }

    @Test
    void givenFailureResultWhenCallingMapFailureOnResultThenReturnResultWithMappedFailureReason() {
        assertThat(result.mapFailure(r -> r * 2)).hasReason(reason * 2);
    }

    @Test
    void givenMappingFunctionIsNullWhenCallingMapFailureOnResultThenThrowNullPointerException() {
        assertThatThrownBy(() -> result.mapFailure(null))
                .isExactlyInstanceOf(NullPointerException.class);
    }
  
    @Test
    void givenFailureResultWhenCallingTryMapOnResultThenReturnMappedResult() {
        assertThat(result.tryMap(v -> v * 2, reason * 2)).hasReason(reason);
    }

    @Test
    void givenMappingFunctionsIsNullWhenCallingTryMapOnResultThenThrowNullPointerException() {
        assertThatThrownBy(() -> result.tryMap(null, reason * 2))
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    void givenFailureResultWhenCallingMapFailureOnResultThenReturnResultWithMappedFailureReasonAndTransformedType() {
        assertThat(result.mapFailure(r -> String.format("%s * 2 -> %s", r, r * 2)))
                .hasReason("21 * 2 -> 42");
    }
    
    @Test
    void givenFailureResultWhenCallingTryMapOnResultThenReturnMappedResultAndTransformedType() {
        assertThat(result.tryMap(v -> String.format("%s * 2 -> %s", v, v * 2),
                reason * 2)).hasReason(reason);
    }

    @Test
    void givenThrowingMappingFunctionWhenCallingTryMapOnResultThenReturnFailureResult() {
        assertThat(result.tryMap(v -> {
            throw new IOException("-11");
        }, reason * 2)).hasReason(reason);
    }

    @Test
    void givenFailureResultWhenCallingPeekOnResultThenKeepSuccessValueOutput() {
        AtomicInteger output = new AtomicInteger(-1);
        result.peek(output::set);
        assertThat(output).hasValue(-1);
    }

    @Test
    void givenOutputFunctionIsNullWhenCallingPeekOnResultThenThrowNullPointerException() {
        assertThatThrownBy(() -> result.peek(null))
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    void givenFailureResultWhenCallingTryPeekOnResultThenKeepSuccessValueOutput() {
        AtomicInteger output = new AtomicInteger(-1);
        result.tryPeek(output::set, reason * 2);
        assertThat(output).hasValue(-1);
    }

    @Test
    void givenThrowingOutputFunctionWhenCallingTryPeekOnResultThenReturnFailureResult() {
        assertThat(result.tryPeek(v -> {
            throw new IOException("-11");
        }, reason * 2)).hasReason(reason);
    }

    @Test
    void givenOutputFunctionIsNullWhenCallingTryPeekOnResultThenThrowNullPointerException() {
        assertThatThrownBy(() -> result.tryPeek(null, reason * 2))
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    void givenFailureResultWhenCallingRecoverOnResultThenReturnSuccessResult() {
        assertThat(result.recover(r -> r * 2)).hasValue(42);
    }

    @Test
    void givenRecoveryFunctionIsNullWhenCallingRecoverOnResultThenThrowNullPointerException() {
        assertThatThrownBy(() -> result.recover(null))
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    void givenFailureResultWhenCallingFlatMapOnResultThenReturnCurrentFailureResult() {
        assertThat(result.flatMap(v -> new Success<>(v * 2))).hasReason(reason);
        assertThat(result.flatMap(v -> new Failure<>(v * 2))).hasReason(reason);
    }

    @Test
    void givenMappingFunctionWhenCallingFlatMapOnResultThenThrowNullPointerException() {
        assertThatThrownBy(() -> result.flatMap(null))
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    void givenOutermostNestedFailureResultWhenCallingUnsafeFlattenOnResultThenReturnFlattenedResult() {
        int reason = 21;
        Result<Integer, Result<Integer, Integer>> res = new Failure<>(new Failure<>(reason));
        assertThat(res.unsafeFlatten()).hasReason(reason);
    }

    @Test
    void givenOutermostFailureResultWithNestedSuccessResultWhenCallingUnsafeFlattenOnResultThenReturnFlattenedResult() {
        int value = 12;
        Result<Integer, Result<Integer, Integer>> res = new Failure<>(new Success<>(value));
        assertThat(res.unsafeFlatten()).hasValue(value);
    }

    @Test
    void givenNonNestedFailureResultWhenCallingUnsafeFlattenOnResultThenReturnNonNestedFailureResult() {
        int reason = 21;
        Result<Integer, Integer> res = new Failure<>(reason);
        assertThat(res.unsafeFlatten()).hasReason(reason);
    }

    @Test
    void givenFailureResultWhenCallingFoldOnResultThenReturnExpectedValue() {
        assertThat(result.<Integer>fold(v -> v * 2, r -> r + 9)).isEqualTo(30);
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
    void givenFailureResultWhenCallingFilterOnResultThenReturnCurrentFailureResult() {
        assertThat(result.filter(Predicate.isEqual(12), 22)).hasReason(reason);
    }

    @Test
    void givenPredicateIsNullWhenCallingFilterOnResultThenThrowNullPointerException() {
        assertThatThrownBy(() -> result.filter(null, null))
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    void givenFailureResultWhenCallingOrOnResultThenReturnSuccessResult() {
        int value = 12;
        assertThat(result.or(() -> new Success<>(value))).hasValue(value);
    }

    @Test
    void givenResultSupplierIsNullWhenCallingOrOnResultThenThrowNullPointerException() {
        assertThatThrownBy(() -> result.or(null))
                .isExactlyInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> result.or(() -> null))
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    void givenFailureResultWhenCallingSwapOnResultThenReturnSuccessResult() {
        assertThat(result.swap()).hasValue(reason);
    }

    @Test
    void givenFailureResultWhenCallingToOptionalOnResultThenReturnEmptyOptional() {
        assertThat(result.toOptional()).isEmpty();
    }

    @Test
    void givenFailureResultWhenCallingStreamOnResultThenReturnEmptyStream() {
        assertThat(result.stream()).isEmpty();
    }
}
