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

import static io.carbynestack.testing.result.ResultAssert.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ResultTest {
    @SuppressWarnings("unused")
    private static final Arguments OF = Arguments.of((AnyThrowingSupplier<Integer>) () -> 12, "some");

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

    private static final class FailureException extends Exception implements CsFailureReason {
        private final String synopsis;
        private final String description;

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
