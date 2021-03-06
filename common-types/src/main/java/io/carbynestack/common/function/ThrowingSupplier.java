/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/common.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.common.function;

import java.util.function.Supplier;

/**
 * Represents a throwing {@link Supplier} of results.
 *
 * @param <T> the type of results supplied by this supplier
 * @param <E> the type of throwable permitted by this supplier
 * @version JDK 8
 * @since 0.1.0
 */
@FunctionalInterface
public interface ThrowingSupplier<T, E extends Throwable> {
    /**
     * Gets a result or throw a {@link Throwable} of type {@link E}.
     *
     * @return a result
     * @throws E a throwable
     * @since 0.1.0
     */
    T get() throws E;
}
