/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/common.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.common.result;

import io.carbynestack.common.function.AnyThrowingConsumer;
import io.carbynestack.common.function.AnyThrowingFunction;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

/**
 * Represents a successful result value.
 *
 * @param <S> the success value type
 * @param <F> the failure reason type
 * @version JDK 17
 * @see Result
 * @see Failure
 * @since 0.1.0
 */
public record Success<S, F>(S value) implements Result<S, F> {
    /**
     * {@inheritDoc}
     *
     * @return true
     * @version JDK 17
     * @see #isFailure()
     * @since 0.1.0
     */
    @Override
    public boolean isSuccess() {
        return true;
    }

    /**
     * {@inheritDoc}
     *
     * @param function the mapping function to apply to a {@link Success#value()}
     * @param <N>      the success type of the value returned from the mapping
     *                 function
     * @return the {@code Result} of mapping the given function to the value
     * from this {@link Success}
     * @throws NullPointerException if the mapping function is {@code null}
     * @version JDK 17
     * @see #recover(Function)
     * @see #peek(Consumer)
     * @since 0.1.0
     */
    @Override
    @SuppressWarnings("unchecked")
    public <N> Result<N, F> map(Function<? super S, ? super N> function) {
        return new Success<>((N) function.apply(this.value()));
    }

    /**
     * {@inheritDoc}
     *
     * @param function the mapping function to apply to a {@link Success#value()}
     * @param reason   the failure reason in case of the function throwing
     *                 a {@code Throwable}
     * @param <N>      the success type of the value returned from the mapping
     *                 function
     * @return the {@code Result} of mapping the given function to the value
     * from this {@link Success} or this {@link Failure}
     * @throws NullPointerException if the mapping function is {@code null}
     * @version JDK 17
     * @see #recover(Function)
     * @see #peek(Consumer)
     * @since 0.2.0
     */
    @Override
    @SuppressWarnings("unchecked")
    public <N> Result<N, F> tryMap(AnyThrowingFunction<? super S, ? super N> function, F reason) {
        requireNonNull(function);
        try {
            return new Success<>((N) function.apply(this.value()));
        } catch (Throwable throwable) {
            return new Failure<>(reason);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param consumer the consumer of {@link Success#value()}
     * @return {@code this}
     * @throws NullPointerException if the consumer is {@code null}
     * @version JDK 17
     * @see #map(Function)
     * @since 0.1.0
     */
    @Override
    public Result<S, F> peek(Consumer<? super S> consumer) {
        consumer.accept(this.value());
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @param consumer the consumer of {@link Success#value()}
     * @param reason   the failure reason in case of the consumer throwing
     *                 a {@code Throwable}
     * @return {@code this}
     * @throws NullPointerException if the consumer is {@code null}
     * @version JDK 17
     * @see #map(Function)
     * @since 0.1.0
     */
    @Override
    public Result<S, F> tryPeek(AnyThrowingConsumer<? super S> consumer, F reason) {
        requireNonNull(consumer);
        try {
            consumer.accept(this.value());
            return this;
        } catch (Throwable throwable) {
            return new Failure<>(reason);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param function the mapping function to apply to a {@link Failure#reason()}
     * @return this {@link Success}
     * @throws NullPointerException if the mapping function is {@code null}
     * @apiNote This method is the {@link Failure} equivalent of
     * {@link Result#map(Function)}.
     * @version JDK 17
     * @since 0.1.0
     */
    @Override
    public Result<S, F> recover(Function<? super F, ? super S> function) {
        requireNonNull(function);
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @param function the mapping function to apply to a {@link Success#value()}
     * @param <N>      the success type of the value returned from the mapping
     *                 function
     * @return the {@code Result} of mapping the given {@code Result}-bearing
     * function to the value from this {@link Success}
     * @throws NullPointerException if the mapping function is {@code null} or
     *                              returns a {@code null} result
     * @version JDK 17
     * @since 0.1.0
     */
    @Override
    @SuppressWarnings("unchecked")
    public <N> Result<N, F> flatMap(Function<? super S, ? extends Result<? extends N, F>> function) {
        return requireNonNull((Result<N, F>) function.apply(this.value()));
    }

    /**
     * {@inheritDoc}
     *
     * @param <V> the new success value type
     * @param <R> the new failure reason type
     * @return a one level flattened {@code Result}
     * @throws ClassCastException if the nested {@code Result} type parameters
     *                            do not align with {@code V} and {@code R}
     * @version JDK 17
     * @since 0.1.0
     */
    @Override
    @SuppressWarnings("unchecked")
    public <V, R> Result<V, R> unsafeFlatten() {
        return (Result<V, R>) (value() instanceof Result nested ? nested : this);
    }

    /**
     * {@inheritDoc}
     *
     * @param successFunction the success mapping function to apply to a
     *                        {@link Success#value()}
     * @param failureFunction the mapping function to apply to a
     *                        {@link Failure#reason()}
     * @param <N>             the type of the value returned from the
     *                        mapping functions
     * @return the folded value of mapping this {@link Success} value
     * to the success mapping function bearing a value of type N
     * @throws NullPointerException if the failure or success mapping function
     *                              is {@code null}
     * @version JDK 17
     * @see Failure#reason()
     * @see Success#value()
     * @since 0.1.0
     */
    @Override
    @SuppressWarnings("unchecked")
    public <N> N fold(Function<? super S, ? super N> successFunction, Function<? super F, ? super N> failureFunction) {
        requireNonNull(failureFunction);
        return (N) successFunction.apply(this.value());
    }

    /**
     * {@inheritDoc}
     *
     * @param predicate the predicate to apply to a {@link Success#value()}
     * @param reason    the failure reason for the value mismatch
     * @return {@code this}
     * @throws NullPointerException if the predicate is {@code null}
     * @version JDK 17
     * @since 0.1.0
     */
    @Override
    public Result<S, F> filter(Predicate<? super S> predicate, F reason) {
        return predicate.test(this.value()) ? this : new Failure<>(reason);
    }

    /**
     * {@inheritDoc}
     *
     * @param supplier the supplying function that produces an {@code Result}
     *                 to be returned
     * @return {@code this}
     * @throws NullPointerException if the supplying function is {@code null}
     *                              or returns a {@code null} result
     * @version JDK 17
     * @since 0.1.0
     */
    @Override
    public Result<S, F> or(Supplier<? extends Result<? extends S, F>> supplier) {
        requireNonNull(supplier);
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @return a {@code Result} with swapped content
     * @since 0.1.0
     */
    @Override
    public Result<F, S> swap() {
        return new Failure<>(this.value());
    }

    /**
     * {@inheritDoc}
     *
     * @return an {@code Optional} with the {@link Success#value()}
     * @version JDK 17
     * @since 0.1.0
     */
    @Override
    public Optional<S> toOptional() {
        return Optional.ofNullable(this.value());
    }

    /**
     * {@inheritDoc}
     *
     * @return a {@code Stream} with the {@link Success#value()}
     * @version JDK 17
     * @since 0.1.0
     */
    @Override
    public Stream<S> stream() {
        return Stream.of(this.value());
    }
}
