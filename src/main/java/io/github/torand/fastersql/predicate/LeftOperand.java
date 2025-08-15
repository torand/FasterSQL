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

import static io.github.torand.fastersql.constant.Constants.$;
import static io.github.torand.fastersql.predicate.compound.CompoundPredicates.not;
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
        return eq($(value));
    }

    /**
     * Creates an optional equivalence predicate from this left operand and the specified optional value.
     * @param value the optional value.
     * @return the optional predicate.
     */
    default OptionalPredicate eq(Optional<?> value) {
        requireNonNull(value, "No value specified");
        return OptionalPredicate.ofNullable(value.map(this::eq).orElse(null));
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
        return eq(new ExpressionSubquery(query));
    }


    /**
     * Creates a non-equivalence predicate from this left operand and the specified constant value.
     * @param value the constant value.
     * @return the predicate.
     */
    default Predicate ne(Object value) {
        return ne($(value));
    }

    /**
     * Creates an optional non-equivalence predicate from this left operand and the specified optional value.
     * @param value the optional value.
     * @return the optional predicate.
     */
    default OptionalPredicate ne(Optional<?> value) {
        requireNonNull(value, "No value specified");
        return OptionalPredicate.ofNullable(value.map(this::ne).orElse(null));
    }

    /**
     * Creates a non-equivalence predicate from this left operand and the specified expression.
     * @param expression the expression.
     * @return the predicate.
     */
    default Predicate ne(Expression expression) {
        return ComparisonPredicates.ne(this, expression);
    }

    /**
     * Creates a non-equivalence predicate from this left operand and the specified subquery.
     * @param query the subquery.
     * @return the predicate.
     */
    default Predicate ne(SelectStatement query) {
        return ne(new ExpressionSubquery(query));
    }

    /**
     * Creates a 'less than' predicate from this left operand and the specified constant value.
     * @param value the constant value.
     * @return the predicate.
     */
    default Predicate lt(Object value) {
        return lt($(value));
    }

    /**
     * Creates an optional 'less than' predicate from this left operand and the specified optional value.
     * @param value the optional value.
     * @return the optional predicate.
     */
    default OptionalPredicate lt(Optional<?> value) {
        requireNonNull(value, "No value specified");
        return OptionalPredicate.ofNullable(value.map(this::lt).orElse(null));
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
        return lt(new ExpressionSubquery(query));
    }

    /**
     * Creates a 'less than or equal' predicate from this left operand and the specified constant value.
     * @param value the constant value.
     * @return the predicate.
     */
    default Predicate le(Object value) {
        return le($(value));
    }

    /**
     * Creates an optional 'less than or equal' predicate from this left operand and the specified optional value.
     * @param value the optional value.
     * @return the optional predicate.
     */
    default OptionalPredicate le(Optional<?> value) {
        requireNonNull(value, "No value specified");
        return OptionalPredicate.ofNullable(value.map(this::le).orElse(null));
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
        return le(new ExpressionSubquery(query));
    }

    /**
     * Creates a 'greater than' predicate from this left operand and the specified constant value.
     * @param value the constant value.
     * @return the predicate.
     */
    default Predicate gt(Object value) {
        return gt($(value));
    }

    /**
     * Creates an optional 'greater than' predicate from this left operand and the specified optional value.
     * @param value the optional value.
     * @return the optional predicate.
     */
    default OptionalPredicate gt(Optional<?> value) {
        requireNonNull(value, "No value specified");
        return OptionalPredicate.ofNullable(value.map(this::gt).orElse(null));
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
        return gt(new ExpressionSubquery(query));
    }

    /**
     * Creates a 'greater than or equal' predicate from this left operand and the specified constant value.
     * @param value the constant value.
     * @return the predicate.
     */
    default Predicate ge(Object value) {
        return ge($(value));
    }

    /**
     * Creates an optional 'greater than or equal' predicate from this left operand and the specified optional value.
     * @param value the optional value.
     * @return the optional predicate.
     */
    default OptionalPredicate ge(Optional<?> value) {
        requireNonNull(value, "No value specified");
        return OptionalPredicate.ofNullable(value.map(this::ge).orElse(null));
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
        return ge(new ExpressionSubquery(query));
    }

    /**
     * Creates a 'between' predicate from this left operand and the specified lower and upper bound constant values.
     * @param lowerBound the lower bound constant value.
     * @param upperBound the upper bound constant value.
     * @return the predicate.
     */
    default Predicate between(Object lowerBound, Object upperBound) {
        return between($(lowerBound), $(upperBound));
    }

    /**
     * Creates a 'between' predicate from this left operand and the specified lower and upper bound expressions.
     * @param lowerBound the lower bound expression.
     * @param upperBound the upper bound expression.
     * @return the predicate.
     */
    default Predicate between(Expression lowerBound, Expression upperBound) {
        return ComparisonPredicates.between(this, lowerBound, upperBound);
    }

    /**
     * Creates a 'not between' predicate from this left operand and the specified lower and upper bound constant values.
     * @param lowerBound the lower bound constant value.
     * @param upperBound the upper bound constant value.
     * @return the predicate.
     */
    default Predicate notBetween(Object lowerBound, Object upperBound) {
        return not(between(lowerBound, upperBound));
    }

    /**
     * Creates a 'not between' predicate from this left operand and the specified lower and upper bound expressions.
     * @param lowerBound the lower bound expression.
     * @param upperBound the upper bound expression.
     * @return the predicate.
     */
    default Predicate notBetween(Expression lowerBound, Expression upperBound) {
        return not(between(lowerBound, upperBound));
    }

    /**
     * Creates a 'member of set' predicate from this left operand and the specified array of constant values.
     * @param values the constant values.
     * @return the predicate.
     */
    default Predicate in(Object... values) {
        requireNonEmpty(values, "No values specified");
        if (values.length == 1) {
            return eq(values[0]);
        } else {
            return in(asList(values));
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
            return eq(values.iterator().next());
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
                return eq(v.iterator().next());
            } else {
                return in(v);
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
     * Creates a 'not member of set' predicate from this left operand and the specified array of constant values.
     * @param values the constant values.
     * @return the predicate.
     */
    default Predicate notIn(Object... values) {
        return not(in(values));
    }

    /**
     * Creates a 'not member of set' predicate from this left operand and the specified collection of constant values.
     * @param values the constant values.
     * @return the predicate.
     */
    default Predicate notIn(Collection<?> values) {
        return not(in(values));
    }

    /**
     * Creates a 'not member of set' predicate from this left operand and the specified subquery.
     * @param query the subquery.
     * @return the predicate.
     */
    default Predicate notIn(SelectStatement query) {
        return not(in(query));
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
        return OptionalPredicate.ofNullable(pattern.map(this::like).orElse(null));
    }

    /**
     * Creates a negated pattern matching predicate from this left operand and the specified string pattern.
     * @param pattern the string pattern.
     * @return the predicate.
     */
    default Predicate notLike(String pattern) {
        return not(like(pattern));
    }

    /**
     * Creates an 'is null' predicate from this left operand.
     * @return the predicate.
     */
    default Predicate isNull() {
        return Predicates.isNull(this);
    }

    /**
     * Creates an 'is not null' predicate from this left operand.
     * @return the predicate.
     */
    default Predicate isNotNull() {
        return not(isNull());
    }
}
