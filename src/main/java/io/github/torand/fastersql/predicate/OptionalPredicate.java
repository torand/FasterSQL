/*
 * Copyright (c) 2024-2025 Tore Eide Andersen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.torand.fastersql.predicate;

import io.github.torand.fastersql.predicate.compound.CompoundPredicates;

import java.util.Collection;
import java.util.stream.Stream;

import static io.github.torand.javacommons.contract.Requires.precondition;
import static io.github.torand.javacommons.stream.StreamHelper.streamSafely;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

/**
 * Implements an optional predicate.
 * The predicate is only executed if the wrapped predicate is present.
 */
public class OptionalPredicate {
    private final Predicate predicate;

    /**
     * Creates an optional predicate wrapping specified non-null predicate.
     * @param predicate the predicate.
     * @return the optional predicate.
     */
    public static OptionalPredicate of(Predicate predicate) {
        requireNonNull(predicate, "Predicate not specified");
        return new OptionalPredicate(predicate);
    }

    /**
     * Creates an optional predicate wrapping specified null or non-null predicate.
     * @param predicate the predicate.
     * @return the optional predicate.
     */
    public static OptionalPredicate ofNullable(Predicate predicate) {
        return new OptionalPredicate(predicate);
    }

    /**
     * Creates an optional predicate with no wrapped predicate.
     * @return the optional predicate.
     */
    public static OptionalPredicate empty() {
        return new OptionalPredicate(null);
    }

    private OptionalPredicate(Predicate predicate) {
        this.predicate = predicate;
    }

    /**
     * Indicates whether there is a wrapped predicate.
     * @return true if there is wrapped predicate; else false.
     */
    public boolean isPresent() {
        return nonNull(predicate);
    }

    /**
     * Gets the wrapped predicate if present. Throws if not present.
     * @return the wrapped predicate.
     */
    public Predicate get() {
        precondition(() -> nonNull(predicate), "Predicate is not present");
        return predicate;
    }

    /**
     * Gets a stream containing the wrapped predicate, if any.
     * @return the stream.
     */
    public Stream<Predicate> stream() {
        return nonNull(predicate) ? Stream.of(predicate) : Stream.empty();
    }

    /**
     * Creates an optional compound predicate of performing the boolean operator OR on this optional predicate and the specified predicate.
     * @param other the other predicate.
     * @return the optional compound predicate.
     */
    public OptionalPredicate or(Predicate other) {
        requireNonNull(other, "other");

        if (nonNull(this.predicate)) {
            return new OptionalPredicate(CompoundPredicates.or(this.predicate, other));
        } else {
            return new OptionalPredicate(other);
        }
    }

    /**
     * Creates an optional compound predicate of performing the boolean operator OR on this optional predicate and the specified optional predicate.
     * @param other the other optional predicate.
     * @return the optional compound predicate.
     */
    public OptionalPredicate or(OptionalPredicate other) {
        if (nonNull(this.predicate) && nonNull(other.predicate)) {
            return new OptionalPredicate(CompoundPredicates.or(this.predicate, other.predicate));
        } else if (nonNull(this.predicate)) {
            return new OptionalPredicate(this.predicate);
        } else if (nonNull(other.predicate)) {
            return new OptionalPredicate(other.predicate);
        } else {
            return new OptionalPredicate(null);
        }
    }

    /**
     * Creates an optional compound predicate of performing the boolean operator AND on this optional predicate and the specified predicate.
     * @param other the other predicate.
     * @return the optional compound predicate.
     */
    public OptionalPredicate and(Predicate other) {
        requireNonNull(other, "other");

        if (nonNull(this.predicate)) {
            return new OptionalPredicate(CompoundPredicates.and(this.predicate, other));
        } else {
            return new OptionalPredicate(other);
        }
    }

    /**
     * Creates an optional compound predicate of performing the boolean operator AND on this optional predicate and the specified optional predicate.
     * @param other the other optional predicate.
     * @return the optional compound predicate.
     */
    public OptionalPredicate and(OptionalPredicate other) {
        if (nonNull(this.predicate) && nonNull(other.predicate)) {
            return new OptionalPredicate(CompoundPredicates.and(this.predicate, other.predicate));
        } else if (nonNull(this.predicate)) {
            return new OptionalPredicate(this.predicate);
        } else if (nonNull(other.predicate)) {
            return new OptionalPredicate(other.predicate);
        } else {
            return new OptionalPredicate(null);
        }
    }

    /**
     * Gets a list of present wrapped predicates from the specified array of optional predicates.
     * @param optionalPredicates the optional predicates-
     * @return the collection of present predicates.
     */
    public static Collection<Predicate> unwrap(OptionalPredicate... optionalPredicates) {
        return streamSafely(optionalPredicates).flatMap(OptionalPredicate::stream).toList();
    }
}
