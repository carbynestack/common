/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/common.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.common;

import io.carbynestack.common.result.Failure;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CsFailureReasonTest {
    @Test
    void toFailure() {
        assertThat(new TestReason("synopsis", "description").toFailure())
                .isExactlyInstanceOf(Failure.class);
    }

    private static final record TestReason(String synopsis, String description) implements CsFailureReason {
    }
}

