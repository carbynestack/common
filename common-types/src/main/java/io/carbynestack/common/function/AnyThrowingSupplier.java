/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/common.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.common.function;

/**
 * Represents a {@link ThrowingSupplier} throwing any kind of {@link Throwable}.
 *
 * @param <T> the type of results supplied by this supplier
 * @version JDK 8
 * @since 0.1.0
 */
@FunctionalInterface
public interface AnyThrowingSupplier<T> extends ThrowingSupplier<T, Throwable> {
    /**
     * {@inheritDoc}
     *
     * @return a result
     * @throws Throwable some throwable
     * @since 0.1.0
     */
    @Override
    T get() throws Throwable;
}
