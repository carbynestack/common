/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/common.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.testing.blankstring;

import org.junit.jupiter.api.Named;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.support.AnnotationConsumer;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * {@code BlankStringProvider} is an {@code ArgumentsProvider} responsible for
 * {@linkplain #provideArguments providing} a stream of named {@link Arguments}
 * containing UTF-8 characters which valid in a blank string.
 *
 * @see ParameterizedTest
 * @see ArgumentsSource
 * @see Arguments
 * @see AnnotationConsumer
 * @since 0.1.0
 */
public class BlankStringProvider implements ArgumentsProvider {
    /**
     * The blank string key-value pairs as a map.
     *
     * @since 0.1.0
     */
    private static final Map<String, String> STRINGS;

    static {
        STRINGS = new HashMap<>();
        STRINGS.put("SPACE", "\u0020");
        STRINGS.put("EN QUAD", "\u2000");
        STRINGS.put("EM QUAD", "\u2001");
        STRINGS.put("EN SPACE", "\u2002");
        STRINGS.put("EM SPACE", "\u2003");
        STRINGS.put("THREE-PER-EM SPACE", "\u2004");
        STRINGS.put("FOUR-PER-EM SPACE", "\u2005");
        STRINGS.put("SIX-PER-EM SPACE", "\u2006");
        STRINGS.put("PUNCTUATION SPACE", "\u2008");
        STRINGS.put("THIN SPACE", "\u2009");
        STRINGS.put("HAIR SPACE", "\u200A");
        STRINGS.put("MEDIUM MATHEMATICAL SPACE", "\u205F");
        STRINGS.put("IDEOGRAPHIC SPACE", "\u3000");
        STRINGS.put("OGHAM SPACE MARK", "\u1680");
        STRINGS.put("HORIZONTAL TABULATION", "\t");
        STRINGS.put("LINE FEED", "\n");
        STRINGS.put("VERTICAL TABULATION", "\u000B");
        STRINGS.put("FORM FEED", "\f");
        STRINGS.put("CARRIAGE RETURN", "\r");
        STRINGS.put("FILE SEPARATOR", "\u001C");
        STRINGS.put("GROUP SEPARATOR", "\u001D");
        STRINGS.put("RECORD SEPARATOR", "\u001E");
        STRINGS.put("UNIT SEPARATOR", "\u001F");
        STRINGS.put("LINE SEPARATOR", "\u2028");
        STRINGS.put("PARAGRAPH SEPARATOR", "\u2029");
    }

    /**
     * Provides a {@link Stream} of named {@code Arguments} containing
     * each an UTF-8 character string.
     *
     * @param context the current extension context
     * @return a stream of named {@link Arguments} containing an UTF-8
     * char
     * @since 0.1.0
     */
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        return STRINGS.entrySet().stream()
                .map(entry -> Named.of(entry.getKey(), entry.getValue()))
                .map(Arguments::of);
    }
}
