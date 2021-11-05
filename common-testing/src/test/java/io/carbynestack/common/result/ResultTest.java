/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/common.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.common.result;

import io.carbynestack.common.function.ThrowingSupplier;
import io.carbynestack.testing.nullable.NullableParamSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;

import java.io.IOException;

import static io.carbynestack.testing.result.ResultAssert.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ResultTest {
    @SuppressWarnings("unused")
    public static final Arguments OF = Arguments.of((ThrowingSupplier<Throwable, Integer>) () -> 12, "some");

    @Test
    public void of() {
        int value = 12;
        Result<Integer, String> result = Result.of(() -> value, "some");
        assertThat(result).hasValue(value);
    }

    @Test
    public void ofThrowsToFailure() {
        String reason = "some";
        Result<Integer, String> result = Result.of(() -> {
            throw new IOException();
        }, reason);
        assertThat(result).hasReason(reason);
    }

    @ParameterizedTest
    @NullableParamSource("OF")
    public void ofNullPointerException(ThrowingSupplier<Throwable, Integer> supplier, String reason) {
        assertThatThrownBy(() -> Result.of(supplier, reason))
                .isExactlyInstanceOf(NullPointerException.class);
    }
}
