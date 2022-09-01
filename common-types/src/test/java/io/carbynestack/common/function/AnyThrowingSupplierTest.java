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

class AnyThrowingSupplierTest {
    @Test
    void givenNonThrowingComputationWhenCallingGetOnAnyThrowingSupplierThenSuccessfullyExecuteComputation() throws Throwable {
        var value = 12;
        AnyThrowingSupplier<Integer> supplier = () -> value;
        assertThat(supplier.get()).isEqualTo(value);
    }

    @Test
    void givenIOExceptionThrowingComputationWhenCallingGetOnAnyThrowingSupplierThenThrowIOException() {
        AnyThrowingSupplier<Integer> supplier = () -> {
            throw new IOException();
        };
        assertThatThrownBy(supplier::get)
                .isExactlyInstanceOf(IOException.class);
    }
}
