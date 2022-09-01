/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/common.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.common.function;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AnyThrowingConsumerTest {
    @Test
    void givenNonThrowingComputationWhenCallingAcceptOnAnyThrowingConsumerThenSuccessfullyExecuteComputation() throws Throwable {
        var value = 12;
        var output = new AtomicInteger(-1);
        AnyThrowingConsumer<Integer> consumer = output::set;
        consumer.accept(value);
        assertThat(output).hasValue(value);
    }

    @Test
    void givenIOExceptionThrowingComputationWhenCallingAcceptOnAnyThrowingConsumerThenThrowIOException() {
        AnyThrowingConsumer<Integer> consumer = v -> {
            throw new IOException();
        };
        assertThatThrownBy(() -> consumer.accept(12))
                .isExactlyInstanceOf(IOException.class);
    }
}
