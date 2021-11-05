/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/common.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.testing.nullable;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.support.AnnotationConsumer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Collections.singletonList;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toCollection;

/**
 * {@code NullableParamGenerator} is an {@code ArgumentsProvider} responsible for
 * {@linkplain #provideArguments providing} a stream of {@link Arguments} to be
 * passed to a {@code @ParameterizedTest} method.
 *
 * @see ParameterizedTest
 * @see ArgumentsSource
 * @see Arguments
 * @see AnnotationConsumer
 * @since 0.1.0
 */
public class NullableParamGenerator implements ArgumentsProvider, AnnotationConsumer<NullableParamSource> {
    /**
     * The field name used to extract the baseline values from.
     *
     * @since 0.1.0
     */
    private String fieldName;

    /**
     * Returns an {@link Optional} reference to a {@link Field}
     * using the given {@link #fieldName name} from the provided
     * class.
     *
     * @param clazz the class to retrieve the {@code Field} from
     * @return an {@code Optional} reference to the {@code Field}
     * @since 0.1.0
     */
    private Optional<Field> getField(Class<?> clazz) {
        try {
            return Optional.of(clazz.getDeclaredField(fieldName));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Returns an {@link Optional} containing the {@code Field}
     * value retrieved using reflection.
     *
     * @param field the {@code Field} to retrieve the value from
     * @return an {@code Optional} containing the {@code Field}
     * {@code Arguments}
     * @since 0.1.0
     */
    private Optional<Arguments> getFieldValue(Field field) {
        try {
            return Optional.ofNullable((Arguments) field.get(null));
        } catch (Throwable ignored) {
            return Optional.empty();
        }
    }

    /**
     * Returns a {@link List} containing the provided argument
     * and a {@code null} value.
     *
     * @param arg the first {@code List} entry
     * @return a {@code List} containing the value and one
     * {@code null}
     * @since 0.1.0
     */
    private List<Object> appendNull(Object arg) {
        List<Object> array = new ArrayList<>(2);
        array.add(arg);
        array.add(null);
        return array;
    }

    /**
     * Provides a {@link Stream} of {@link Arguments} to be passed to a
     * {@code @ParameterizedTest} method based on the referenced field
     * values.
     *
     * <p>Every {@code Arguments} object contains a copy of the baseline
     * value set with one or more {@code null} substitutions.
     *
     * @param context the current extension context
     * @return a {@code Stream} of {@code Arguments}
     * @since 0.1.0
     */
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        return context.getTestClass()
                .flatMap(this::getField)
                .flatMap(this::getFieldValue)
                .map(Arguments::get)
                .map(args -> Arrays.stream(args)
                        .map(this::appendNull)
                        .collect(toCollection(ArrayList::new)))
                .map(this::uniqueCombinations)
                .map(args -> args.stream()
                        .skip(args.size() > 0 ? 1 : 0)
                        .map(List::toArray)
                        .map(Arguments::of))
                .orElseThrow(() -> new IllegalArgumentException(
                        "Failed to load test params from static variable."));
    }

    /**
     * Generates all unique combinations of the provided list of pairs.
     *
     * @param items the set of unprocessed pairs
     * @return the generated {@code List} of combinations
     * @since 0.1.0
     */
    private List<List<Object>> uniqueCombinations(List<List<Object>> items) {
        return this.uniqueCombinations(items, new ArrayList<>());
    }

    /**
     * Generates all unique combinations of the provided list of pairs.
     *
     * @param items   the set of unprocessed pairs
     * @param prepend the first entries of the output
     * @return the generated {@code List} of combinations
     * @since 0.1.0
     */
    private List<List<Object>> uniqueCombinations(List<List<Object>> items, List<Object> prepend) {
        if (items == null || items.size() == 0) {
            return new ArrayList<>(singletonList(requireNonNull(prepend)));
        }
        List<List<Object>> out = new ArrayList<>();
        for (int i = 0; i < items.get(0).size(); i++) {
            out = new ArrayList<>(out);
            List<Object> array = new ArrayList<>(prepend);
            array.add(items.get(0).get(i));
            out.addAll(this.uniqueCombinations(items.subList(1, items.size()), array));
        }
        return out;
    }

    /**
     * Accepts the {@link NullableParamSource} annotation from the
     * test to extract the field name.
     *
     * @param nullableParamSource the {@code NullableParamSource} annotation
     * @since 0.1.0
     */
    @Override
    public void accept(NullableParamSource nullableParamSource) {
        fieldName = nullableParamSource.value();
    }
}
