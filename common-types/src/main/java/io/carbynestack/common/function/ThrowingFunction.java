/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/common.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.common.function;

/**
 * Represents a throwing function that accepts one
 * argument and produces a result.
 *
 * @param <T> the type of the input to the function
 * @param <R> the type of the result of the function
 * @version JDK 8
 * @since 0.2.0
 */
public interface ThrowingFunction<T, R, E extends Throwable> {
    /**
     * Applies this function to the given argument
     * or throws a {@link Throwable} of type {@link E}.
     *
     * @param t the function argument
     * @return the function result
     * @throws E a throwable
     * @since 0.2.0
     */
    R apply(T t) throws E;
}
