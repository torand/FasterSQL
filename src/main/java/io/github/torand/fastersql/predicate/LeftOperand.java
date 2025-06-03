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

import io.github.torand.fastersql.expression.Expression;
import io.github.torand.fastersql.predicate.comparison.ComparisonPredicates;
import io.github.torand.fastersql.sql.Sql;
import io.github.torand.fastersql.statement.SelectStatement;
import io.github.torand.fastersql.subquery.ExpressionSubquery;

import java.util.Collection;
import java.util.Optional;

import static io.github.torand.javacommons.contract.Requires.requireNonEmpty;
import static java.util.Arrays.asList;
import static java.util.Objects.requireNonNull;

/**
 * Defines the left operand of an expression.
 */
public interface LeftOperand extends Sql {

    /**
     * Creates an equivalence predicate from this left operand and the specified constant value.
     * @param value the constant value.
     * @return the predicate.
     */
    default Predicate eq(Object value) {
        return ComparisonPredicates.eq(this, value);
    }

    /**
     * Creates an optional equivalence predicate from this left operand and the specified optional value.
     * @param value the optional value.
     * @return the optional predicate.
     */
    default OptionalPredicate eq(Optional<?> value) {
        requireNonNull(value, "No value specified");
        return OptionalPredicate.ofNullable(value.map(v -> ComparisonPredicates.eq(this, v)).orElse(null));
    }

    /**
     * Creates an equivalence predicate from this left operand and the specified expression.
     * @param expression the expression.
     * @return the predicate.
     */
    default Predicate eq(Expression expression) {
        return ComparisonPredicates.eq(this, expression);
    }

    /**
     * Creates an equivalence predicate from this left operand and the specified subquery.
     * @param query the subquery.
     * @return the predicate.
     */
    default Predicate eq(SelectStatement query) {
        return ComparisonPredicates.eq(this, new ExpressionSubquery(query));
    }

    /**
     * Creates a 'less than' predicate from this left operand and the specified constant value.
     * @param value the constant value.
     * @return the predicate.
     */
    default Predicate lt(Object value) {
        return ComparisonPredicates.lt(this, value);
    }

    /**
     * Creates an optional 'less than' predicate from this left operand and the specified optional value.
     * @param value the optional value.
     * @return the optional predicate.
     */
    default OptionalPredicate lt(Optional<?> value) {
        requireNonNull(value, "No value specified");
        return OptionalPredicate.ofNullable(value.map(v -> ComparisonPredicates.lt(this, v)).orElse(null));
    }

    /**
     * Creates a 'less than' predicate from this left operand and the specified expression.
     * @param expression the expression.
     * @return the predicate.
     */
    default Predicate lt(Expression expression) {
        return ComparisonPredicates.lt(this, expression);
    }

    /**
     * Creates a 'less than' predicate from this left operand and the specified subquery.
     * @param query the subquery.
     * @return the predicate.
     */
    default Predicate lt(SelectStatement query) {
        return ComparisonPredicates.lt(this, new ExpressionSubquery(query));
    }

    /**
     * Creates a 'less than or equal' predicate from this left operand and the specified constant value.
     * @param value the constant value.
     * @return the predicate.
     */
    default Predicate le(Object value) {
        return ComparisonPredicates.le(this, value);
    }

    /**
     * Creates an optional 'less than or equal' predicate from this left operand and the specified optional value.
     * @param value the optional value.
     * @return the optional predicate.
     */
    default OptionalPredicate le(Optional<?> value) {
        requireNonNull(value, "No value specified");
        return OptionalPredicate.ofNullable(value.map(v -> ComparisonPredicates.le(this, v)).orElse(null));
    }

    /**
     * Creates a 'less than or equal' predicate from this left operand and the specified expression.
     * @param expression the expression.
     * @return the predicate.
     */
    default Predicate le(Expression expression) {
        return ComparisonPredicates.le(this, expression);
    }

    /**
     * Creates a 'less than or equal' predicate from this left operand and the specified subquery.
     * @param query the subquery.
     * @return the predicate.
     */
    default Predicate le(SelectStatement query) {
        return ComparisonPredicates.le(this, new ExpressionSubquery(query));
    }

    /**
     * Creates a 'greater than' predicate from this left operand and the specified constant value.
     * @param value the constant value.
     * @return the predicate.
     */
    default Predicate gt(Object value) {
        return ComparisonPredicates.gt(this, value);
    }

    /**
     * Creates an optional 'greater than' predicate from this left operand and the specified optional value.
     * @param value the optional value.
     * @return the optional predicate.
     */
    default OptionalPredicate gt(Optional<?> value) {
        requireNonNull(value, "No value specified");
        return OptionalPredicate.ofNullable(value.map(v -> ComparisonPredicates.gt(this, v)).orElse(null));
    }

    /**
     * Creates a 'greater than' predicate from this left operand and the specified expression.
     * @param expression the expression.
     * @return the predicate.
     */
    default Predicate gt(Expression expression) {
        return ComparisonPredicates.gt(this, expression);
    }

    /**
     * Creates a 'greater than' predicate from this left operand and the specified subquery.
     * @param query the subquery.
     * @return the predicate.
     */
    default Predicate gt(SelectStatement query) {
        return ComparisonPredicates.gt(this, new ExpressionSubquery(query));
    }

    /**
     * Creates a 'greater than or equal' predicate from this left operand and the specified constant value.
     * @param value the constant value.
     * @return the predicate.
     */
    default Predicate ge(Object value) {
        return ComparisonPredicates.ge(this, value);
    }

    /**
     * Creates an optional 'greater than or equal' predicate from this left operand and the specified optional value.
     * @param value the optional value.
     * @return the optional predicate.
     */
    default OptionalPredicate ge(Optional<?> value) {
        requireNonNull(value, "No value specified");
        return OptionalPredicate.ofNullable(value.map(v -> ComparisonPredicates.ge(this, v)).orElse(null));
    }

    /**
     * Creates a 'greater than or equal' predicate from this left operand and the specified expression.
     * @param expression the expression.
     * @return the predicate.
     */
    default Predicate ge(Expression expression) {
        return ComparisonPredicates.ge(this, expression);
    }

    /**
     * Creates a 'greater than or equal' predicate from this left operand and the specified subquery.
     * @param query the subquery.
     * @return the predicate.
     */
    default Predicate ge(SelectStatement query) {
        return ComparisonPredicates.ge(this, new ExpressionSubquery(query));
    }

    /**
     * Creates a 'member of set' predicate from this left operand and the specified array of constant values.
     * @param values the constant values.
     * @return the predicate.
     */
    default Predicate in(Object... values) {
        requireNonEmpty(values, "No values specified");
        if (values.length == 1) {
            return ComparisonPredicates.eq(this, values[0]);
        } else {
            return Predicates.in(this, asList(values));
        }
    }

    /**
     * Creates a 'member of set' predicate from this left operand and the specified collection of constant values.
     * @param values the constant values.
     * @return the predicate.
     */
    default Predicate in(Collection<?> values) {
        requireNonEmpty(values, "No values specified");
        if (values.size() == 1) {
            return ComparisonPredicates.eq(this, values.iterator().next());
        } else {
            return Predicates.in(this, values);
        }
    }

    /**
     * Creates an optional 'member of set' predicate from this left operand and the specified optional collection of constant values.
     * @param values the optional constant values.
     * @return the optional predicate.
     */
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

    /**
     * Creates a 'member of set' predicate from this left operand and the specified subquery.
     * @param query the subquery.
     * @return the predicate.
     */
    default Predicate in(SelectStatement query) {
        return Predicates.in(this, new ExpressionSubquery(query));
    }

    /**
     * Creates a pattern matching predicate from this left operand and the specified string pattern.
     * @param pattern the string pattern.
     * @return the predicate.
     */
    default Predicate like(String pattern) {
        return Predicates.like(this, pattern);
    }

    /**
     * Creates an optional pattern matching predicate from this left operand and the specified optional string pattern.
     * @param pattern the optional string pattern.
     * @return the optional predicate.
     */
    default OptionalPredicate like(Optional<String> pattern) {
        requireNonNull(pattern, "No pattern specified");
        return OptionalPredicate.ofNullable(pattern.map(p -> Predicates.like(this, p)).orElse(null));
    }

    /**
     * Creates an 'is null' predicate from this left operand.
     * @return the predicate.
     */
    default Predicate isNull() {
        return Predicates.isNull(this);
    }
}
