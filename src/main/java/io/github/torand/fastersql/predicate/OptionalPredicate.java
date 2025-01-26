/*
 * Copyright (c) 2024 Tore Eide Andersen
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

import static io.github.torand.fastersql.util.collection.CollectionHelper.streamSafely;
import static io.github.torand.fastersql.util.contract.Requires.precondition;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

public class OptionalPredicate {
    private final Predicate predicate;

    public static OptionalPredicate of(Predicate predicate) {
        requireNonNull(predicate, "Predicate not specified");
        return new OptionalPredicate(predicate);
    }

    public static OptionalPredicate ofNullable(Predicate predicate) {
        return new OptionalPredicate(predicate);
    }

    public static OptionalPredicate empty() {
        return new OptionalPredicate(null);
    }

    private OptionalPredicate(Predicate predicate) {
        this.predicate = predicate;
    }

    public boolean isPresent() {
        return nonNull(predicate);
    }

    public Predicate get() {
        precondition(() -> nonNull(predicate), "Predicate is not present");
        return predicate;
    }

    public Stream<Predicate> stream() {
        return nonNull(predicate) ? Stream.of(predicate) : Stream.empty();
    }

    public OptionalPredicate or(Predicate other) {
        requireNonNull(other, "other");

        if (nonNull(this.predicate)) {
            return new OptionalPredicate(CompoundPredicates.or(this.predicate, other));
        } else {
            return new OptionalPredicate(other);
        }
    }

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

    public OptionalPredicate and(Predicate other) {
        requireNonNull(other, "other");

        if (nonNull(this.predicate)) {
            return new OptionalPredicate(CompoundPredicates.and(this.predicate, other));
        } else {
            return new OptionalPredicate(other);
        }
    }

    public static Collection<Predicate> unwrap(OptionalPredicate... maybePredicates) {
        return streamSafely(maybePredicates).flatMap(OptionalPredicate::stream).toList();
    }
}
