/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/common.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.common.function;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AnyThrowingFunctionTest {
    @Test
    void apply() throws Throwable {
        var value = 12;
        AnyThrowingFunction<Integer, Integer> function = v -> v;
        assertThat(function.apply(value)).isEqualTo(value);
    }

    @Test
    void applyThrowsException() {
        AnyThrowingFunction<Integer, Integer> function = v -> {
            throw new IOException();
        };
        assertThatThrownBy(() -> function.apply(12))
                .isExactlyInstanceOf(IOException.class);
    }
}
