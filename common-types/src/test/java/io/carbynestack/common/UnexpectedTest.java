/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/common.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.common;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UnexpectedTest {
    private final Unexpected unknown = new Unexpected(new IOException("test"));

    @Test
    void constructor() {
        assertThatThrownBy(() -> new Unexpected(null)).isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    void givenUnexpectedWithThrowableWhenCallingSynopsisOnUnexpectedThenReturnExpectedSynopsis() {
        assertThat(unknown.synopsis()).isEqualTo("An unknown exception has occurred.");
    }

    @Test
    void givenUnexpectedWithThrowableWhenCallingDescriptionOnUnexpectedThenReturnExpectedDescription() {
        assertThat(unknown.description()).isEqualTo("test");
    }

    @Test
    void givenUnexpectedWithThrowableWhenCallingStackTraceOnUnexpectedThenReturnExpectedStackTrace() {
        assertThat(unknown.stackTrace()).isNotEmpty();
    }

    @Test
    void givenUnexpectedWithThrowableWhenCallingReportIssueOnUnexpectedThenReturnExpectedReportIssue() {
        assertThat(unknown.reportIssue()).isTrue();
    }
}
