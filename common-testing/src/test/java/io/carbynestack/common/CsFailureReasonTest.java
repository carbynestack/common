/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/common.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.common;

import org.junit.jupiter.api.Test;

import static io.carbynestack.testing.result.ResultAssert.assertThat;

class CsFailureReasonTest {
    @Test
    void whenCallingToFailureOnCsFailureReasonThenReturnReasonWrappedInFailureInstance() {
        assertThat(new TestReason().toFailure()).isFailure();
    }

    private static final class TestReason implements CsFailureReason {
        @Override
        public String synopsis() {
            return "synopsis";
        }

        @Override
        public String description() {
            return "description";
        }
    }
}
