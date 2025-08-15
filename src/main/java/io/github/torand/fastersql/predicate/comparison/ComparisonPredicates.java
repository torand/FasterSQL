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
package io.github.torand.fastersql.predicate.comparison;

import io.github.torand.fastersql.expression.Expression;
import io.github.torand.fastersql.predicate.LeftOperand;

/**
 * Provides factory methods for comparison predicates.
 */
public final class ComparisonPredicates {
    private ComparisonPredicates() {}

    /**
     * Creates an equivalence predicate taking an expression as right operand.
     * @param left the left operand.
     * @param right the right operand.
     * @return the predicate.
     */
    public static Eq eq(LeftOperand left, Expression right) {
        return new Eq(left, right);
    }

    /**
     * Creates a non-equivalence predicate taking an expression as right operand.
     * @param left the left operand.
     * @param right the right operand.
     * @return the predicate.
     */
    public static Ne ne(LeftOperand left, Expression right) {
        return new Ne(left, right);
    }

    /**
     * Creates a less than predicate taking an expression as right operand.
     * @param left the left operand.
     * @param right the right operand.
     * @return the predicate.
     */
    public static Lt lt(LeftOperand left, Expression right) {
        return new Lt(left, right);
    }

    /**
     * Creates a less than or equal predicate taking an expression as right operand.
     * @param left the left operand.
     * @param right the right operand.
     * @return the predicate.
     */
    public static Le le(LeftOperand left, Expression right) {
        return new Le(left, right);
    }

    /**
     * Creates a greater than predicate taking an expression as right operand.
     * @param left the left operand.
     * @param right the right operand.
     * @return the predicate.
     */
    public static Gt gt(LeftOperand left, Expression right) {
        return new Gt(left, right);
    }

    /**
     * Creates a greater than or equal predicate taking an expression as right operand.
     * @param left the left operand.
     * @param right the right operand.
     * @return the predicate.
     */
    public static Ge ge(LeftOperand left, Expression right) {
        return new Ge(left, right);
    }

    /**
     * Creates a between predicate taking expressions as lower and upper bound operands.
     * @param left the left operand.
     * @param lowerBound the lower bound operand.
     * @param upperBound the upper bound operand.
     * @return the predicate.
     */
    public static Between between(LeftOperand left, Expression lowerBound, Expression upperBound) {
        return new Between(left, lowerBound, upperBound);
    }
}
