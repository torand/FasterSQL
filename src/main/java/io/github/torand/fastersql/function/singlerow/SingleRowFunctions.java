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
package io.github.torand.fastersql.function.singlerow;

import io.github.torand.fastersql.expression.Expression;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

/**
 * Provides factory methods for single row functions.
 */
public final class SingleRowFunctions {
    private SingleRowFunctions() {}

    /**
     * Creates the upper case function for a string expression.
     * @param expression the string expression.
     * @return the string function.
     */
    public static Upper upper(Expression expression) {
        return new Upper(expression, null);
    }

    /**
     * Creates the lower case function for a string expression.
     * @param expression the string expression.
     * @return the string function.
     */
    public static Lower lower(Expression expression) {
        return new Lower(expression, null);
    }

    /**
     * Creates the string to decimal number conversion function for an expression.
     * @param expression the string expression.
     * @param precision the total number of digits (including decimals).
     * @param scale the number of decimals.
     * @return the conversion function.
     */
    public static ToNumber toNumber(Expression expression, int precision, int scale) {
        return new ToNumber(expression, precision, scale, null);
    }

    /**
     * Creates the string to integer number conversion function for an expression.
     * @param expression the string expression.
     * @param precision the total number of digits.
     * @return the conversion function.
     */
    public static ToNumber toNumber(Expression expression, int precision) {
        return new ToNumber(expression, precision, 0, null);
    }

    /**
     * Creates the substring function for a string expression.
     * @param expression the string expression.
     * @return the string function.
     */
    public static Substring substring(Expression expression, int startPos, int length) {
        return new Substring(expression, startPos, length, null);
    }

    /**
     * Creates the timestamp/number to string conversion function for an expression.
     * @param expression the timestamp or number expression.
     * @param format the format specifier.
     * @return the conversion function.
     */
    public static ToChar toChar(Expression expression, String format) {
        return new ToChar(expression, format, null);
    }

    /**
     * Creates the concatenation function for string expressions.
     * @param expression1 the first string expression.
     * @param expression2 the second string expression.
     * @param otherExpressions the additional string expressions.
     * @return the string function.
     */
    public static Concat concat(Expression expression1, Expression expression2, Expression... otherExpressions) {
        List<Expression> expressions = new ArrayList<>();
        expressions.add(requireNonNull(expression1, "First expression is null"));
        expressions.add(requireNonNull(expression2, "Second expression is null"));
        if (nonNull(otherExpressions)) {
            expressions.addAll(List.of(otherExpressions));
        }

        return new Concat(expressions, null);
    }

    /**
     * Creates the length function for a string expression.
     * @param expression the string expression.
     * @return the string function.
     */
    public static Length length(Expression expression) {
        return new Length(expression, null);
    }

    /**
     * Creates the round function for a numeric expression.
     * @param expression the numeric expression.
     * @return the numeric function.
     */
    public static Round round(Expression expression) {
        return new Round(expression, null);
    }

    /**
     * Creates the absolute value function for a numeric expression.
     * @param expression the numeric expression.
     * @return the numeric function.
     */
    public static Abs abs(Expression expression) {
        return new Abs(expression, null);
    }

    /**
     * Creates the ceiling function for a numeric expression.
     * @param expression the numeric expression.
     * @return the numeric function.
     */
    public static Ceil ceil(Expression expression) {
        return new Ceil(expression, null);
    }

    /**
     * Creates the floor function for a numeric expression.
     * @param expression the numeric expression.
     * @return the numeric function.
     */
    public static Floor floor(Expression expression) {
        return new Floor(expression, null);
    }
}
