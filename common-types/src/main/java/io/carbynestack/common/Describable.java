/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/common.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.common;

/**
 * Implementations of this interface represent describable
 * objects that contain a description and synopsis.
 *
 * @since 0.1.0
 */
public interface Describable {
    /**
     * A short summary of the description.
     *
     * @return the description synopsis
     * @see #description()
     * @since 0.1.0
     */
    String synopsis();

    /**
     * The description.
     *
     * @return the full description
     * @see #synopsis()
     * @since 0.1.0
     */
    String description();
}
