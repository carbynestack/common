/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/common.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.common.function;

/**
 * Represents a {@link ThrowingFunction} throwing any kind of {@link Throwable}.
 *
 * @param <T> the type of the input to the function
 * @param <R> the type of the result of the function
 * @version JDK 8
 * @since 0.2.0
 */
public interface AnyThrowingFunction<T, R> extends ThrowingFunction<T, R, Throwable> {
    /**
     * {@inheritDoc}
     *
     * @param t the function argument
     * @return the function result
     * @throws Throwable some throwable
     * @since 0.2.0
     */
    @Override
    R apply(T t) throws Throwable;
}
