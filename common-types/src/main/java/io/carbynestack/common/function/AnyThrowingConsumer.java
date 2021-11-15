/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/common.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.common.function;

/**
 * Represents a {@link ThrowingConsumer} throwing any kind of {@link Throwable}.
 *
 * @param <T> the type of values consumed by this consumer
 * @version JDK 8
 * @since 0.1.0
 */
@FunctionalInterface
public interface AnyThrowingConsumer<T> extends ThrowingConsumer<T, Throwable> {
    /**
     * {@inheritDoc}
     *
     * @param t the input argument
     * @throws Throwable some throwable
     * @since 0.1.0
     */
    @Override
    void accept(T t) throws Throwable;
}
