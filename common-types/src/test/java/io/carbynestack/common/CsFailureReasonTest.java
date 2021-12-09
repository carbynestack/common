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
    private static final String SYNOPSIS = "synopsis";
    private final CsFailureReason reason = new TestReason(SYNOPSIS);

    @Test
    void description() {
        assertThat(reason.description()).isEqualTo(SYNOPSIS);
    }

    @Test
    void stackTrace() {
        assertThat(reason.stackTrace()).isEmpty();
    }

    @Test
    void reportIssue() {
        assertThat(reason.reportIssue()).isFalse();
    }

    @Test
    void toFailure() {
        assertThat(new TestReason("synopsis").toFailure()).isExactlyInstanceOf(Failure.class);
    }

    record TestReason(String synopsis) implements CsFailureReason {
    }
}

