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
package io.github.torand.fastersql.expression;

import io.github.torand.fastersql.expression.arithmetic.ArithmeticExpressions;
import io.github.torand.fastersql.projection.Projection;

import static io.github.torand.fastersql.constant.Constants.$;

/**
 * Defines an expression resulting in a scalar value or a table with rows and columns.
 */
public interface Expression extends Projection {

    /**
     * Creates the expression of adding a constant value to this expression.
     * @param value the constant value.
     * @return the addition expression.
     */
    default Expression plus(Number value) {
        return plus($(value));
    }

    /**
     * Creates the expression of adding another expression to this expression.
     * @param expression the other expression.
     * @return the addition expression.
     */
    default Expression plus(Expression expression) {
        return ArithmeticExpressions.add(this, expression);
    }

    /**
     * Creates the expression of subtracting a constant value from this expression.
     * @param value the constant value.
     * @return the subtraction expression.
     */
    default Expression minus(Number value) {
        return minus($(value));
    }

    /**
     * Creates the expression of subtracting another expression from this expression.
     * @param expression the other expression.
     * @return the subtraction expression.
     */
    default Expression minus(Expression expression) {
        return ArithmeticExpressions.subtract(this, expression);
    }

    /**
     * Creates the expression of multiplying a constant value with this expression.
     * @param value the constant value.
     * @return the multiplication expression.
     */
    default Expression times(Number value) {
        return times($(value));
    }

    /**
     * Creates the expression of multiplying another expression with this expression.
     * @param expression the other expression.
     * @return the multiplication expression.
     */
    default Expression times(Expression expression) {
        return ArithmeticExpressions.multiply(this, expression);
    }

    /**
     * Creates the expression of dividing this expression by a constant value.
     * @param value the constant value.
     * @return the division expression.
     */
    default Expression dividedBy(Number value) {
        return dividedBy($(value));
    }

    /**
     * Creates the expression of dividing this expression by another expression.
     * @param expression the other expression.
     * @return the division expression.
     */
    default Expression dividedBy(Expression expression) {
        return ArithmeticExpressions.divide(this, expression);
    }

    /**
     * Creates the expression of calculating the remainder after dividing this expression by a constant value.
     * @param value the constant value.
     * @return the modulo expression.
     */
    default Expression mod(Number value) {
        return mod($(value));
    }

    /**
     * Creates the expression of calculating the remainder after dividing this expression by another expression.
     * @param expression the other expression.
     * @return the modulo expression.
     */
    default Expression mod(Expression expression) {
        return ArithmeticExpressions.mod(this, expression);
    }
}
