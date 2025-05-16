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

import static io.github.torand.fastersql.constant.Constants.$;

/**
 * Provides factory methods for comparison predicates.
 */
public final class ComparisonPredicates {
    private ComparisonPredicates() {}

    /**
     * Creates an equivalence predicate taking a scalar value as right operand.
     * @param left the left operand.
     * @param right the right operand.
     * @return the predicate.
     */
    public static Eq eq(LeftOperand left, Object right) {
        return new Eq(left, $(right));
    }

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
     * Creates a less than predicate taking a scalar value as right operand.
     * @param left the left operand.
     * @param right the right operand.
     * @return the predicate.
     */
    public static Lt lt(LeftOperand left, Object right) {
        return new Lt(left, $(right));
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
     * Creates a less than or equal predicate taking a scalar value as right operand.
     * @param left the left operand.
     * @param right the right operand.
     * @return the predicate.
     */
    public static Le le(LeftOperand left, Object right) {
        return new Le(left, $(right));
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
     * Creates a greater than predicate taking a scalar value as right operand.
     * @param left the left operand.
     * @param right the right operand.
     * @return the predicate.
     */
    public static Gt gt(LeftOperand left, Object right) {
        return new Gt(left, $(right));
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
     * Creates a greater than or equal predicate taking a scalar value as right operand.
     * @param left the left operand.
     * @param right the right operand.
     * @return the predicate.
     */
    public static Ge ge(LeftOperand left, Object right) {
        return new Ge(left, $(right));
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
}
