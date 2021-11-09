/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/common.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.common.result;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static java.util.function.Function.identity;
import static org.assertj.core.api.Assertions.assertThat;

class ResultTest {
    @Test
    void success() {
        var value = 12;
        var other = new Success<Integer, Integer>(value);

        var res = Result.<IOException, Integer, Integer>of(() -> {
                    throw new IOException();
                }, 21)
                .map(v -> v * 2)
                .peek(System.out::println)
                .recover(r -> r)
                .peek(System.out::println)
                .flatMap(v -> new Failure<Integer, Integer>(v - 10))
                .peek(System.out::println)
                .filter(v -> v > 10, -1)
                .or(() -> other);

        assertThat(res.isSuccess()).isTrue();
        assertThat(res.isFailure()).isFalse();
        assertThat(res.<Integer>fold(identity(), identity())).isEqualTo(value);
        assertThat(res.toOptional()).isPresent();
        assertThat(res.stream().toList()).containsExactly(value);
    }

    @Test
    void failure() {
        var res = Result.of(() -> 21, 21)
                .map(v -> v * 2)
                .peek(System.out::println)
                .recover(r -> r)
                .peek(System.out::println)
                .flatMap(v -> new Failure<Integer, Integer>(v - 10))
                .peek(System.out::println)
                .filter(v -> v < 10, -1)
                .or(() -> new Failure<>(-11));

        assertThat(res.isSuccess()).isFalse();
        assertThat(res.isFailure()).isTrue();
        assertThat(res.<Integer>fold(identity(), identity())).isEqualTo(-11);
        assertThat(res.toOptional()).isEmpty();
        assertThat(res.stream().toList()).isEmpty();
    }
}
