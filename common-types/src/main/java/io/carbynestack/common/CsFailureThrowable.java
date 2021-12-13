/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/common.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.common;

import io.carbynestack.common.result.Failure;

import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;

/**
 * Represents the Carbyne Stack throwable failure base type.
 *
 * @version JDK 8
 * @see Failure
 * @see CsFailureReason
 * @since 0.2.0
 */
public interface CsFailureThrowable extends CsFailureReason {
    /**
     * {@inheritDoc}
     *
     * @return the description synopsis
     * @see #description()
     * @since 0.2.0
     */
    @Override
    default String synopsis() {
        return this instanceof Throwable
                ? ((Throwable) this).getLocalizedMessage()
                : "Throwable %s has been caught.".formatted(this);
    }

    /**
     * {@inheritDoc}
     *
     * @return the stack trace as an {@link Optional}
     * @since 0.2.0
     */
    @Override
    default Optional<StackTraceElement[]> stackTrace() {
        return this instanceof Throwable
                ? ofNullable(((Throwable) this).getStackTrace())
                : empty();
    }
}
