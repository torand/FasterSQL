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
package io.github.torand.fastersql.condition;

import io.github.torand.fastersql.condition.logical.LogicalConditions;

import java.util.Collection;
import java.util.stream.Stream;

import static io.github.torand.fastersql.util.collection.CollectionHelper.streamSafely;
import static io.github.torand.fastersql.util.contract.Requires.precondition;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

public class OptionalCondition {
    private final Condition condition;

    public static OptionalCondition of(Condition condition) {
        requireNonNull(condition, "condition");
        return new OptionalCondition(condition);
    }

    public static OptionalCondition ofNullable(Condition condition) {
        return new OptionalCondition(condition);
    }

    public static OptionalCondition empty() {
        return new OptionalCondition(null);
    }

    private OptionalCondition(Condition condition) {
        this.condition = condition;
    }

    public boolean isPresent() {
        return nonNull(condition);
    }

    public Condition get() {
        precondition(() -> nonNull(condition), "Condition is not present");
        return condition;
    }

    public Stream<Condition> stream() {
        return nonNull(condition) ? Stream.of(condition) : Stream.empty();
    }

    public OptionalCondition or(Condition other) {
        requireNonNull(other, "other");

        if (nonNull(this.condition)) {
            return new OptionalCondition(LogicalConditions.or(this.condition, other));
        } else {
            return new OptionalCondition(other);
        }
    }

    public OptionalCondition or(OptionalCondition other) {
        if (nonNull(this.condition) && nonNull(other.condition)) {
            return new OptionalCondition(LogicalConditions.or(this.condition, other.condition));
        } else if (nonNull(this.condition)) {
            return new OptionalCondition(this.condition);
        } else if (nonNull(other.condition)) {
            return new OptionalCondition(other.condition);
        } else {
            return new OptionalCondition(null);
        }
    }

    public OptionalCondition and(OptionalCondition other) {
        if (nonNull(this.condition) && nonNull(other.condition)) {
            return new OptionalCondition(LogicalConditions.and(this.condition, other.condition));
        } else if (nonNull(this.condition)) {
            return new OptionalCondition(this.condition);
        } else if (nonNull(other.condition)) {
            return new OptionalCondition(other.condition);
        } else {
            return new OptionalCondition(null);
        }
    }

    public OptionalCondition and(Condition other) {
        requireNonNull(other, "other");

        if (nonNull(this.condition)) {
            return new OptionalCondition(LogicalConditions.and(this.condition, other));
        } else {
            return new OptionalCondition(other);
        }
    }

    public static Collection<Condition> unwrap(OptionalCondition... maybeConditions) {
        return streamSafely(maybeConditions).flatMap(OptionalCondition::stream).toList();
    }
}
