/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/common.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.common;

import io.carbynestack.common.result.Failure;

/**
 * Represents the Carbyne Stack failure reason base type.
 *
 * @version JDK 8
 * @see Failure
 * @since 0.1.0
 */
public interface CsFailureReason extends Describable {
    /**
     * A short summary of the failure reason description.
     *
     * @return the description synopsis
     * @see #description()
     * @since 0.1.0
     */
    @Override
    String synopsis();

    /**
     * The failure reason description.
     *
     * @return the full description
     * @see #synopsis()
     * @since 0.1.0
     */
    @Override
    String description();

    /**
     * Converts this {@code CsFailureReason} into a {@link Failure}.
     *
     * @param <S> the success value type
     * @param <F> the failure reason type
     * @return an {@link Failure}
     * @since 0.1.0
     */
    @SuppressWarnings("unchecked")
    default <S, F extends CsFailureReason> Failure<S, F> toFailure() {
        return new Failure<>((F) this);
    }
}
