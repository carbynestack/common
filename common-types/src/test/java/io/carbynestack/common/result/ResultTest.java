/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/common.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.common.result;

import io.carbynestack.common.CsFailureReason;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static java.util.function.Function.identity;
import static org.assertj.core.api.Assertions.assertThat;

class ResultTest {
    @Test
    void success() {
        var value = 12;
        var other = new Success<Integer, Integer>(value);

        var res = Result.<Integer, Integer>of(() -> {
                    throw new IOException();
                }, 21)
                .map(v -> v * 2)
                .peek(System.out::println)
                .recover(r -> r)
                .peek(System.out::println)
                .flatMap(v -> new Failure<Integer, Integer>(v - 10))
                .peek(System.out::println)
                .filter(v -> v > 10, -1)
                .or(() -> other)
                .or(() -> new Failure<>(-11)
                        .flatMap(v -> new Success<>((Integer) v)))
                .filter(v -> v > 10, -1);

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

    @Test
    void result() {
        var res = new Failure<Integer, Integer>(21)
                .flatMap(Success::new)
                .recover(r -> r)
                .filter(v -> v < 5, -21);

        assertThat(res.isSuccess()).isFalse();
        assertThat(res.isFailure()).isTrue();
        assertThat(res.<Integer>fold(identity(), identity())).isEqualTo(-21);
        assertThat(res.toOptional()).isEmpty();
        assertThat(res.stream().toList()).isEmpty();
    }

    @Test
    void of() {
        var value = 12;
        FailureException reason = new FailureException();
        assertThat(Result.of(() -> value).isSuccess()).isTrue();
        assertThat(Result.of(() -> {
            throw new FailureException();
        }).isSuccess()).isFalse();
        assertThat(Result.of(() -> value, Collections.emptyMap(),
                new FailureException()).isSuccess()).isTrue();
        assertThat(Result.of(() -> {
            throw new IOException();
        }, Collections.emptyMap(), reason).isFailure()).isTrue();
        var reasons = new HashMap<Class<? extends Throwable>, FailureException>();
        reasons.put(IOException.class, reason);
        reasons.put(IllegalArgumentException.class, reason);
        reasons.put(Throwable.class, reason);
        reasons.put(NumberFormatException.class, new FailureException("IO", "IOException"));
        assertThat(Result.of(() -> {
            throw new IOException(new IllegalArgumentException());
        }, reasons, reason).isFailure()).isTrue();
    }
  
    @Test
    void unsafeFlatten() {
        var value = 12;
        var reason = 21;

        assertThat(new Success<>(new Success<>(value)).unsafeFlatten()
                .<Integer>fold(identity(), identity())).isEqualTo(value);
        assertThat(new Success<>(new Failure<>(reason)).unsafeFlatten()
                .<Integer>fold(identity(), identity())).isEqualTo(reason);
        assertThat(new Success<>(value).unsafeFlatten()
                .<Integer>fold(identity(), identity())).isEqualTo(value);

        assertThat(new Failure<>(new Failure<>(reason)).unsafeFlatten()
                .<Integer>fold(identity(), identity())).isEqualTo(reason);
        assertThat(new Failure<>(new Success<>(value)).unsafeFlatten()
                .<Integer>fold(identity(), identity())).isEqualTo(value);
        assertThat(new Failure<>(reason).unsafeFlatten()
                .<Integer>fold(identity(), identity())).isEqualTo(reason);
    }

    @Test
    void swap() {
        var value = 12;
        var res = new Success<Integer, Integer>(value);

        assertThat(res.<Integer>fold(identity(), r -> -1)).isEqualTo(value);
        assertThat(res.swap().<Integer>fold(identity(), r -> -1)).isEqualTo(-1);
        assertThat(res.swap().swap().<Integer>fold(identity(), r -> -1)).isEqualTo(value);
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
