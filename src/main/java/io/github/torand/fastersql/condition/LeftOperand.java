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

import io.github.torand.fastersql.Field;
import io.github.torand.fastersql.Sql;
import io.github.torand.fastersql.condition.comparison.ComparisonConditions;
import io.github.torand.fastersql.expression.Expression;
import io.github.torand.fastersql.statement.SelectStatement;
import io.github.torand.fastersql.subquery.Subquery;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

import static io.github.torand.fastersql.util.contract.Requires.requireNonEmpty;
import static java.util.Arrays.asList;
import static java.util.Objects.requireNonNull;

public interface LeftOperand extends Sql {
    Stream<Field> fieldRefs();

    default Condition eq(Object value) {
        return ComparisonConditions.eq(this, value);
    }

    default OptionalCondition eq(Optional<?> value) {
        requireNonNull(value, "No value specified");
        return OptionalCondition.ofNullable(value.map(v -> ComparisonConditions.eq(this, v)).orElse(null));
    }

    default Condition eq(Expression expression) {
        return ComparisonConditions.eq(this, expression);
    }

    default Condition eq(SelectStatement inner) {
        return ComparisonConditions.eq(this, new Subquery(inner));
    }

    default Condition lt(Object value) {
        return ComparisonConditions.lt(this, value);
    }

    default OptionalCondition lt(Optional<?> value) {
        requireNonNull(value, "No value specified");
        return OptionalCondition.ofNullable(value.map(v -> ComparisonConditions.lt(this, v)).orElse(null));
    }

    default Condition lt(Expression other) {
        return ComparisonConditions.lt(this, other);
    }

    default Condition le(Object value) {
        return ComparisonConditions.le(this, value);
    }

    default OptionalCondition le(Optional<?> value) {
        requireNonNull(value, "No value specified");
        return OptionalCondition.ofNullable(value.map(v -> ComparisonConditions.le(this, v)).orElse(null));
    }

    default Condition le(Expression other) {
        return ComparisonConditions.le(this, other);
    }

    default Condition gt(Object value) {
        return ComparisonConditions.gt(this, value);
    }

    default OptionalCondition gt(Optional<?> value) {
        requireNonNull(value, "No value specified");
        return OptionalCondition.ofNullable(value.map(v -> ComparisonConditions.gt(this, v)).orElse(null));
    }

    default Condition gt(Expression other) {
        return ComparisonConditions.gt(this, other);
    }

    default Condition ge(Object value) {
        return ComparisonConditions.ge(this, value);
    }

    default OptionalCondition ge(Optional<?> value) {
        requireNonNull(value, "No value specified");
        return OptionalCondition.ofNullable(value.map(v -> ComparisonConditions.ge(this, v)).orElse(null));
    }

    default Condition ge(Expression other) {
        return ComparisonConditions.ge(this, other);
    }

    default Condition in(Object... values) {
        requireNonEmpty(values, "No values specified");
        if (values.length == 1) {
            return ComparisonConditions.eq(this, values[0]);
        } else {
            return ComparisonConditions.in(this, asList(values));
        }
    }

    default Condition in(Collection<?> values) {
        requireNonEmpty(values, "No values specified");
        if (values.size() == 1) {
            return ComparisonConditions.eq(this, values.iterator().next());
        } else {
            return ComparisonConditions.in(this, values);
        }
    }

    default OptionalCondition in(Optional<? extends Collection<?>> values) {
        requireNonNull(values, "No values specified");
        return OptionalCondition.ofNullable(values.map(v -> {
            if (v.size() == 1) {
                return ComparisonConditions.eq(this, v.iterator().next());
            } else {
                return ComparisonConditions.in(this, v);
            }
        }).orElse(null));
    }

    default Condition like(String pattern) {
        return ComparisonConditions.like(this, pattern);
    }

    default OptionalCondition like(Optional<String> pattern) {
        requireNonNull(pattern, "No pattern specified");
        return OptionalCondition.ofNullable(pattern.map(p -> ComparisonConditions.like(this, p)).orElse(null));
    }
}
