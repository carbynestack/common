/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/common.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.common.result;

import io.carbynestack.common.CsFailureReason;
import io.carbynestack.common.function.AnyThrowingSupplier;
import io.carbynestack.testing.nullable.NullableParamSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static io.carbynestack.testing.result.ResultAssert.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ResultTest {
    @SuppressWarnings("unused")
    private static final Arguments OF = Arguments.of((AnyThrowingSupplier<Integer>) () -> 12, "some");
    @SuppressWarnings("unused")
    private static final Arguments OF_WITH_MAPPED_EXCEPTIONS = Arguments.of((AnyThrowingSupplier<Integer>)
            () -> 12, Collections.emptyMap());

    @Test
    void of() {
        int value = 12;
        Result<Integer, String> result = Result.of(() -> value, "some");
        assertThat(result).hasValue(value);
    }

    @Test
    void ofThrowsToFailure() {
        String reason = "some";
        Result<Integer, String> result = Result.of(() -> {
            throw new IOException();
        }, reason);
        assertThat(result).hasReason(reason);
    }

    @ParameterizedTest
    @NullableParamSource("OF")
    void ofNullPointerException(AnyThrowingSupplier<Integer> supplier, String reason) {
        assertThatThrownBy(() -> Result.of(supplier, reason))
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    void ofWithCsFailureReason() {
        int value = 12;
        Result<Integer, ? extends Throwable> result = Result.of(() -> value);
        assertThat(result).hasValue(value);
    }

    @Test
    void ofWithCsFailureReasonToFailure() {
        FailureException reason = new FailureException();
        Result<Object, FailureException> result = Result.of(() -> {
            throw reason;
        });
        assertThat(result).hasReason(reason);
    }

    @Test
    void ofWithCsFailureReasonNullPointerException() {
        assertThatThrownBy(() -> Result.of(null))
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    void ofWithMappedExceptions() {
        int value = 12;
        Result<Integer, FailureException> result = Result.of(() -> value, Collections.emptyMap(),
                new FailureException());
        assertThat(result).hasValue(value);
    }

    @Test
    void ofWithNoMappedExceptions() {
        FailureException exception = new FailureException();
        Result<Integer, FailureException> result = Result.of(() -> {
            throw new IOException();
        }, Collections.emptyMap(), exception);
        assertThat(result).hasReason(exception);
    }

    @Test
    void ofWithMappedExactExceptionToFailure() {
        FailureException reason = new FailureException("IO", "IOException");
        Map<Class<? extends Throwable>, FailureException> reasons = new HashMap<>();
        reasons.put(IOException.class, reason);
        Result<Integer, FailureException> result = Result.of(() -> {
            throw new IOException();
        }, reasons, new FailureException());
        assertThat(result).hasReason(reason);
    }

    @Test
    void ofWithMappedCauseExceptionToFailure() {
        FailureException reason = new FailureException("IO", "IOException");
        Map<Class<? extends Throwable>, FailureException> reasons = new HashMap<>();
        reasons.put(IllegalArgumentException.class, reason);
        Result<Integer, FailureException> result = Result.of(() -> {
            throw new IOException(new IllegalArgumentException());
        }, reasons, new FailureException());
        assertThat(result).hasReason(reason);
    }

    @Test
    void ofWithMappedAssignableExceptionToFailure() {
        FailureException reason = new FailureException("IO", "IOException");
        Map<Class<? extends Throwable>, FailureException> reasons = new HashMap<>();
        reasons.put(Throwable.class, reason);
        Result<Integer, FailureException> result = Result.of(() -> {
            throw new IOException(new IllegalArgumentException());
        }, reasons, new FailureException());
        assertThat(result).hasReason(reason);
    }

    @Test
    void ofWithMappedExceptionsMissingEntry() {
        FailureException exception = new FailureException();
        Map<Class<? extends Throwable>, FailureException> reasons = new HashMap<>();
        reasons.put(NumberFormatException.class, new FailureException("IO", "IOException"));
        Result<Integer, FailureException> result = Result.of(() -> {
            throw new IOException(new IllegalArgumentException());
        }, reasons, exception);
        assertThat(result).hasReason(exception);
    }

    @ParameterizedTest
    @NullableParamSource("OF_WITH_MAPPED_EXCEPTIONS")
    void ofWithMappedExceptionsNullPointerException(AnyThrowingSupplier<Integer> supplier, Map<Class<? extends Throwable>, FailureException> reasons) {
        assertThatThrownBy(() -> Result.of(supplier, reasons, new FailureException()));
    }

    private static final class FailureException extends Exception implements CsFailureReason {
        private final String synopsis, description;

        public FailureException(String synopsis, String description) {
            this.synopsis = synopsis;
            this.description = description;
        }

        public FailureException() {
            this("synopsis", "description");
        }

        @Override
        public String synopsis() {
            return synopsis;
        }

        @Override
        public String description() {
            return description;
        }
    }
}
