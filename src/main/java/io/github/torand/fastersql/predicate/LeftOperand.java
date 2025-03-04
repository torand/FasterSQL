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

import io.github.torand.fastersql.Sql;
import io.github.torand.fastersql.expression.Expression;
import io.github.torand.fastersql.predicate.comparison.ComparisonPredicates;
import io.github.torand.fastersql.statement.SelectStatement;
import io.github.torand.fastersql.subquery.ExpressionSubquery;

import java.util.Collection;
import java.util.Optional;

import static io.github.torand.fastersql.util.contract.Requires.requireNonEmpty;
import static java.util.Arrays.asList;
import static java.util.Objects.requireNonNull;

public interface LeftOperand extends Sql {

    default Predicate eq(Object value) {
        return ComparisonPredicates.eq(this, value);
    }

    default OptionalPredicate eq(Optional<?> value) {
        requireNonNull(value, "No value specified");
        return OptionalPredicate.ofNullable(value.map(v -> ComparisonPredicates.eq(this, v)).orElse(null));
    }

    default Predicate eq(Expression expression) {
        return ComparisonPredicates.eq(this, expression);
    }

    default Predicate eq(SelectStatement inner) {
        return ComparisonPredicates.eq(this, new ExpressionSubquery(inner));
    }

    default Predicate lt(Object value) {
        return ComparisonPredicates.lt(this, value);
    }

    default OptionalPredicate lt(Optional<?> value) {
        requireNonNull(value, "No value specified");
        return OptionalPredicate.ofNullable(value.map(v -> ComparisonPredicates.lt(this, v)).orElse(null));
    }

    default Predicate lt(Expression other) {
        return ComparisonPredicates.lt(this, other);
    }

    default Predicate lt(SelectStatement inner) {
        return ComparisonPredicates.lt(this, new ExpressionSubquery(inner));
    }

    default Predicate le(Object value) {
        return ComparisonPredicates.le(this, value);
    }

    default OptionalPredicate le(Optional<?> value) {
        requireNonNull(value, "No value specified");
        return OptionalPredicate.ofNullable(value.map(v -> ComparisonPredicates.le(this, v)).orElse(null));
    }

    default Predicate le(Expression other) {
        return ComparisonPredicates.le(this, other);
    }

    default Predicate le(SelectStatement inner) {
        return ComparisonPredicates.le(this, new ExpressionSubquery(inner));
    }

    default Predicate gt(Object value) {
        return ComparisonPredicates.gt(this, value);
    }

    default OptionalPredicate gt(Optional<?> value) {
        requireNonNull(value, "No value specified");
        return OptionalPredicate.ofNullable(value.map(v -> ComparisonPredicates.gt(this, v)).orElse(null));
    }

    default Predicate gt(Expression other) {
        return ComparisonPredicates.gt(this, other);
    }

    default Predicate gt(SelectStatement inner) {
        return ComparisonPredicates.gt(this, new ExpressionSubquery(inner));
    }

    default Predicate ge(Object value) {
        return ComparisonPredicates.ge(this, value);
    }

    default OptionalPredicate ge(Optional<?> value) {
        requireNonNull(value, "No value specified");
        return OptionalPredicate.ofNullable(value.map(v -> ComparisonPredicates.ge(this, v)).orElse(null));
    }

    default Predicate ge(Expression other) {
        return ComparisonPredicates.ge(this, other);
    }

    default Predicate ge(SelectStatement inner) {
        return ComparisonPredicates.ge(this, new ExpressionSubquery(inner));
    }

    default Predicate in(Object... values) {
        requireNonEmpty(values, "No values specified");
        if (values.length == 1) {
            return ComparisonPredicates.eq(this, values[0]);
        } else {
            return Predicates.in(this, asList(values));
        }
    }

    default Predicate in(Collection<?> values) {
        requireNonEmpty(values, "No values specified");
        if (values.size() == 1) {
            return ComparisonPredicates.eq(this, values.iterator().next());
        } else {
            return Predicates.in(this, values);
        }
    }

    default OptionalPredicate in(Optional<? extends Collection<?>> values) {
        requireNonNull(values, "No values specified");
        return OptionalPredicate.ofNullable(values.map(v -> {
            if (v.size() == 1) {
                return ComparisonPredicates.eq(this, v.iterator().next());
            } else {
                return Predicates.in(this, v);
            }
        }).orElse(null));
    }

    default Predicate in(SelectStatement inner) {
        return Predicates.in(this, new ExpressionSubquery(inner));
    }

    default Predicate like(String pattern) {
        return Predicates.like(this, pattern);
    }

    default OptionalPredicate like(Optional<String> pattern) {
        requireNonNull(pattern, "No pattern specified");
        return OptionalPredicate.ofNullable(pattern.map(p -> Predicates.like(this, p)).orElse(null));
    }

    default Predicate isNull() {
        return Predicates.isNull(this);
    }
}
