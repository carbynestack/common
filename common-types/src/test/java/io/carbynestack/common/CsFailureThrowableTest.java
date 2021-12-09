/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/common.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.common;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CsFailureThrowableTest {
    @Test
    void throwableSynopsis() {
        assertThat(new SomeThrowable().synopsis()).isEqualTo("Some message.");
    }

    @Test
    void nonThrowableSynopsis() {
        assertThat(new NonThrowable().synopsis()).isEqualTo("Throwable NonThrowable[] has been caught.");
    }

    @Test
    void throwableStackTrace() {
        try {
            throw new SomeThrowable();
        } catch (SomeThrowable throwable) {
            assertThat(throwable.stackTrace()).isNotEmpty();
        }
    }

    @Test
    void nonThrowableStackTrace() {
        assertThat(new NonThrowable().stackTrace()).isEmpty();
    }

    static final class NonThrowable implements CsFailureThrowable {
        @Override
        public String toString() {
            return "NonThrowable[]";
        }
    }

    static final class SomeThrowable extends Exception implements CsFailureThrowable {
        @Override
        public String getMessage() {
            return "Some message.";
        }

        @Override
        public String toString() {
            return "SomeThrowable[]";
        }
    }
}
