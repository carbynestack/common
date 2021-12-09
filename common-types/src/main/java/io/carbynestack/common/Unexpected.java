/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/common.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.common;

import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

/**
 * Represents an unexpected {@link Throwable} based failure reason.
 *
 * @version JDK 8
 * @see CsFailureReason
 * @since 0.2.0
 */
public final class Unexpected implements CsFailureReason {
    /**
     * The failure reason.
     *
     * @since 0.2.0
     */
    private final Throwable throwable;

    /**
     * Creates an {@code Unexpected} instance from a {@code Throwable}.
     *
     * @param throwable the failure reason
     * @since 0.2.0
     */
    public Unexpected(Throwable throwable) {
        this.throwable = requireNonNull(throwable);
    }

    /**
     * {@inheritDoc}
     *
     * @return the description synopsis
     * @see #description()
     * @since 0.2.0
     */
    @Override
    public String synopsis() {
        return "An unknown exception has occurred.";
    }

    /**
     * {@inheritDoc}
     *
     * @return the full description
     * @see #synopsis()
     * @since 0.2.0
     */
    @Override
    public String description() {
        return throwable.getLocalizedMessage();
    }

    /**
     * {@inheritDoc}
     *
     * @return the stack trace as an {@link Optional}
     * @since 0.2.0
     */
    @Override
    public Optional<StackTraceElement[]> stackTrace() {
        return ofNullable(throwable.getStackTrace());
    }

    /**
     * {@inheritDoc}
     *
     * @return {@code true} if the request should be triggered,
     * otherwise {@code false}.
     * @since 0.2.0
     */
    @Override
    public boolean reportIssue() {
        return true;
    }
}
