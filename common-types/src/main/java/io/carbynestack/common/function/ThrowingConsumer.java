/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/common.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.common.function;

import java.util.function.Consumer;

/**
 * Represents a throwing {@link Consumer} of values.
 *
 * @param <T> the type of values consumed by this consumer
 * @param <E> the type of throwable permitted by this consumer
 * @version JDK 8
 * @since 0.1.0
 */
@FunctionalInterface
public interface ThrowingConsumer<T, E extends Throwable> {
    /**
     * Performs this operation on the given argument.
     *
     * @param t the input argument
     * @throws E a throwable
     * @since 0.1.0
     */
    void accept(T t) throws E;
}
