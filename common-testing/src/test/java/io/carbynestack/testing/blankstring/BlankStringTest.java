/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/common.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.testing.blankstring;

import org.junit.jupiter.params.ParameterizedTest;

import static org.assertj.core.api.Assertions.assertThat;

class BlankStringTest {
    @ParameterizedTest
    @BlankStringSource
    void verify(String blank) {
        assertThat(blank).isBlank();
    }
}
