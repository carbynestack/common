/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/common.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.testing.nullable;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.lang.annotation.*;

/**
 * {@code @NullableParamSource} is an {@link ArgumentsSource} for null
 * substitution of a set of baseline values.
 *
 * <p>The {@code NullableParamGenerator} produces all possible combinations
 * of the set of baseline values substituted with null to ensure that tests
 * using this source can wholly assert null specific behavior.
 *
 * <p>The set of baseline values are automatically extracted from a static
 * {@link Arguments} field that must be provided using {@link #value()}.
 *
 * @see ArgumentsSource
 * @see ParameterizedTest
 * @since 0.1.0
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ArgumentsSource(NullableParamGenerator.class)
public @interface NullableParamSource {
    /**
     * The field name used to lookup the baseline values.
     *
     * @return The baseline values.
     * @since 0.1.0
     */
    String value();
}
