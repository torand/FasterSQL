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
package io.github.torand.fastersql.expression;

import io.github.torand.fastersql.Field;
import io.github.torand.fastersql.Sql;
import io.github.torand.fastersql.expression.comparison.ComparisonExpressions;
import io.github.torand.fastersql.statement.SelectStatement;
import io.github.torand.fastersql.subquery.Subquery;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.Objects.requireNonNull;
import static io.github.torand.fastersql.util.contract.Requires.requireNonEmpty;

public interface LeftOperand extends Sql {
    Stream<Field> fields();

    default Expression eq(Object value) {
        return ComparisonExpressions.eq(this, value);
    }

    default OptionalExpression eq(Optional<?> value) {
        requireNonNull(value, "No value specified");
        return OptionalExpression.ofNullable(value.map(v -> ComparisonExpressions.eq(this, v)).orElse(null));
    }

    default Expression eq(Field other) {
        return ComparisonExpressions.eq(this, other);
    }

    default Expression eq(SelectStatement inner) {
        return ComparisonExpressions.eq(this, new Subquery(inner));
    }

    default Expression lt(Object value) {
        return ComparisonExpressions.lt(this, value);
    }

    default OptionalExpression lt(Optional<?> value) {
        requireNonNull(value, "No value specified");
        return OptionalExpression.ofNullable(value.map(v -> ComparisonExpressions.lt(this, v)).orElse(null));
    }

    default Expression lt(Field other) {
        return ComparisonExpressions.lt(this, other);
    }

    default Expression le(Object value) {
        return ComparisonExpressions.le(this, value);
    }

    default OptionalExpression le(Optional<?> value) {
        requireNonNull(value, "No value specified");
        return OptionalExpression.ofNullable(value.map(v -> ComparisonExpressions.le(this, v)).orElse(null));
    }

    default Expression le(Field other) {
        return ComparisonExpressions.le(this, other);
    }

    default Expression gt(Object value) {
        return ComparisonExpressions.gt(this, value);
    }

    default OptionalExpression gt(Optional<?> value) {
        requireNonNull(value, "No value specified");
        return OptionalExpression.ofNullable(value.map(v -> ComparisonExpressions.gt(this, v)).orElse(null));
    }

    default Expression gt(Field other) {
        return ComparisonExpressions.gt(this, other);
    }

    default Expression ge(Object value) {
        return ComparisonExpressions.ge(this, value);
    }

    default OptionalExpression ge(Optional<?> value) {
        requireNonNull(value, "No value specified");
        return OptionalExpression.ofNullable(value.map(v -> ComparisonExpressions.ge(this, v)).orElse(null));
    }

    default Expression ge(Field other) {
        return ComparisonExpressions.ge(this, other);
    }

    default Expression in(Object... values) {
        requireNonEmpty(values, "No values specified");
        if (values.length == 1) {
            return ComparisonExpressions.eq(this, values[0]);
        } else {
            return ComparisonExpressions.in(this, asList(values));
        }
    }

    default Expression in(Collection<?> values) {
        requireNonEmpty(values, "No values specified");
        if (values.size() == 1) {
            return ComparisonExpressions.eq(this, values.iterator().next());
        } else {
            return ComparisonExpressions.in(this, values);
        }
    }

    default OptionalExpression in(Optional<? extends Collection<?>> values) {
        requireNonNull(values, "No values specified");
        return OptionalExpression.ofNullable(values.map(v -> {
            if (v.size() == 1) {
                return ComparisonExpressions.eq(this, v.iterator().next());
            } else {
                return ComparisonExpressions.in(this, v);
            }
        }).orElse(null));
    }

    default Expression like(String pattern) {
        return ComparisonExpressions.like(this, pattern);
    }

    default OptionalExpression like(Optional<String> pattern) {
        requireNonNull(pattern, "No pattern specified");
        return OptionalExpression.ofNullable(pattern.map(p -> ComparisonExpressions.like(this, p)).orElse(null));
    }
}
