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

public class ThrowingSupplierTest {
    @Test
    public void get() {
        var value = 12;
        ThrowingSupplier<RuntimeException, Integer> supplier = () -> value;
        assertThat(supplier.get()).isEqualTo(value);
    }

    @Test
    public void getCheckedException() throws IOException {
        var value = 12;
        ThrowingSupplier<IOException, Integer> supplier = () -> value;
        assertThat(supplier.get()).isEqualTo(value);
    }

    @Test
    public void getThrowsException() {
        ThrowingSupplier<IOException, Integer> supplier = () -> {
            throw new IOException();
        };
        assertThatThrownBy(supplier::get)
                .isExactlyInstanceOf(IOException.class);
    }
}
