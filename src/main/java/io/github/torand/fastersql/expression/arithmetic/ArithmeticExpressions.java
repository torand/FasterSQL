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
package io.github.torand.fastersql.expression.arithmetic;

import io.github.torand.fastersql.expression.Expression;

/**
 * Provides factory methods for arithmetic expressions.
 */
public final class ArithmeticExpressions {
    private ArithmeticExpressions() {}

    /**
     * Creates an addition (sum) expression.
     * @param firstTerm the first term.
     * @param secondTerm the second term.
     * @return the addition expression.
     */
    public static Addition add(Expression firstTerm, Expression secondTerm) {
        return new Addition(firstTerm, secondTerm, null);
    }

    /**
     * Creates a subtraction (difference) expression.
     * @param minuend the minuend.
     * @param subtrahend the subtrahend.
     * @return the subtraction expression.
     */
    public static Subtraction subtract(Expression minuend, Expression subtrahend) {
        return new Subtraction(minuend, subtrahend, null);
    }

    /**
     * Creates a multiplication (product) expression.
     * @param firstFactor the first factor.
     * @param secondFactor the second factor.
     * @return the multiplication expression.
     */
    public static Multiplication multiply(Expression firstFactor, Expression secondFactor) {
        return new Multiplication(firstFactor, secondFactor, null);
    }

    /**
     * Creates a division (quotient) expression.
     * @param dividend the dividend.
     * @param divisor the divisor.
     * @return the division expression.
     */
    public static Division divide(Expression dividend, Expression divisor) {
        return new Division(dividend, divisor, null);
    }

    /**
     * Creates a modulo (division remainder) expression.
     * @param dividend the dividend.
     * @param divisor the divisor.
     * @return the modulo expression.
     */
    public static Modulo mod(Expression dividend, Expression divisor) {
        return new Modulo(dividend, divisor, null);
    }

    /**
     * Creates a negatation expression.
     * @param expression the numeric expression.
     * @return the negation expression.
     */
    public static Negate neg(Expression expression) {
        return new Negate(expression, null);
    }
}
