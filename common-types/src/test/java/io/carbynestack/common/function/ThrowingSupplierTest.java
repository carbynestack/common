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

class ThrowingSupplierTest {
    @Test
    void get() {
        var value = 12;
        ThrowingSupplier<Integer, RuntimeException> supplier = () -> value;
        assertThat(supplier.get()).isEqualTo(value);
    }

    @Test
    void getCheckedException() throws IOException {
        var value = 12;
        ThrowingSupplier<Integer, IOException> supplier = () -> value;
        assertThat(supplier.get()).isEqualTo(value);
    }

    @Test
    void getThrowsException() {
        ThrowingSupplier<Integer, IOException> supplier = () -> {
            throw new IOException();
        };
        assertThatThrownBy(supplier::get)
                .isExactlyInstanceOf(IOException.class);
    }
}
