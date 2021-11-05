/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/common.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.testing.blankstring;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.lang.annotation.*;
import java.util.stream.Stream;

/**
 * {@code @BlankStringSource} is an {@link ArgumentsSource} for blank
 * string values.
 *
 * <p>The {@code BlankStringProvider} produces a {@link Stream} of all
 * UTF-8 chars that are allowed in a blank string.
 *
 * @see ArgumentsSource
 * @see ParameterizedTest
 * @since 0.1.0
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ArgumentsSource(BlankStringProvider.class)
public @interface BlankStringSource {
}
