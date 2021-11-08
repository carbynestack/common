/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/common.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.testing.result;

import io.carbynestack.common.result.Failure;
import io.carbynestack.common.result.Result;
import io.carbynestack.common.result.Success;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import static io.carbynestack.testing.result.ResultAssert.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ResultAssertTest {
    public final int value = 12;
    public final int reason = 21;
    public final String some = "some";
    public final String none = "none";
    public final Result<Integer, Integer> success = new Success<>(value);
    public final Result<Integer, Integer> failure = new Failure<>(reason);

    @Test
    public void isSuccess() {
        assertThat(success).isSuccess();
        assertThatThrownBy(() -> assertThat(failure).isSuccess())
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("Expecting a success result but was: Failure[reason=%s]", reason);
    }

    @Test
    public void isFailure() {
        assertThat(failure).isFailure();
        assertThatThrownBy(() -> assertThat(success).isFailure())
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("Expecting a failure result but was: Success[value=%s]", value);
    }

    @Test
    public void hasValue() {
        assertThat(success).hasValue(value);
        assertThatThrownBy(() -> assertThat(failure).hasValue(value))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("Expecting a success result but was: Failure[reason=%s]", reason);
        assertThatThrownBy(() -> assertThat(success).hasValue(reason))
                .isExactlyInstanceOf(AssertionFailedError.class)
                .hasMessage("Expecting result success value to equal %s but was: %s", reason, value);
    }

    @Test
    public void hasStringValue() {
        assertThatThrownBy(() -> assertThat(new Success<>(some)).hasValue(none))
                .isExactlyInstanceOf(AssertionFailedError.class)
                .hasMessage("Expecting result success value to equal '%s' but was: '%s'", none, some);
    }

    @Test
    public void hasStringValueWithSingleQuotes() {
        assertThatThrownBy(() -> assertThat(new Success<>("'first'")).hasValue("second"))
                .isExactlyInstanceOf(AssertionFailedError.class)
                .hasMessage("Expecting result success value to equal 'second' but was: '%s'", "'first'");
    }

    @Test
    public void hasStringValueWithLineBreaks() {
        assertThatThrownBy(() -> assertThat(new Success<>("first\nsecond")).hasValue("third"))
                .isExactlyInstanceOf(AssertionFailedError.class)
                .hasMessage("Expecting result success value to equal 'third' but was: '%s'", "first\\nsecond");
    }

    @Test
    public void hasReason() {
        assertThat(failure).hasReason(reason);
        assertThatThrownBy(() -> assertThat(success).hasReason(reason))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("Expecting a failure result but was: Success[value=%s]", value);
        assertThatThrownBy(() -> assertThat(failure).hasReason(value))
                .isExactlyInstanceOf(AssertionFailedError.class)
                .hasMessage("Expecting result failure reason to equal %s but was: %s", value, reason);
    }

    @Test
    public void hasStringReason() {
        assertThatThrownBy(() -> assertThat(new Failure<>(some)).hasReason(none))
                .isExactlyInstanceOf(AssertionFailedError.class)
                .hasMessage("Expecting result failure reason to equal '%s' but was: '%s'", none, some);
    }

    @Test
    public void hasStringReasonWithSingleQuotes() {
        assertThatThrownBy(() -> assertThat(new Failure<>("'first'")).hasReason("second"))
                .isExactlyInstanceOf(AssertionFailedError.class)
                .hasMessage("Expecting result failure reason to equal 'second' but was: '%s'", "'first'");
    }

    @Test
    public void hasStringReasonWithLineBreaks() {
        assertThatThrownBy(() -> assertThat(new Failure<>("first\nsecond")).hasReason("third"))
                .isExactlyInstanceOf(AssertionFailedError.class)
                .hasMessage("Expecting result failure reason to equal 'third' but was: '%s'", "first\\nsecond");
    }
}
