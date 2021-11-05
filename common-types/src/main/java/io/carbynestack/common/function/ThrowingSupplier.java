/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/common.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.common.function;

/**
 * Represents a throwing supplier of results.
 *
 * @param <E> the type of throwable permitted by this supplier
 * @param <T> the type of results supplied by this supplier
 * @version JDK 8
 * @since 0.1.0
 */
@FunctionalInterface
public interface ThrowingSupplier<E extends Throwable, T> {
    /**
     * Gets a result or throw a {@link Throwable} of type {@link E}.
     *
     * @return a result
     * @throws E a throwable
     * @since 0.1.0
     */
    T get() throws E;
}
