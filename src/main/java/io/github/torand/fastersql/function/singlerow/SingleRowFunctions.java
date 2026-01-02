/*
 * Copyright (c) 2024-2026 Tore Eide Andersen
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
import io.github.torand.fastersql.function.singlerow.cast.CastBuilder;

import java.util.ArrayList;
import java.util.List;

import static io.github.torand.fastersql.constant.Constants.$;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

/**
 * Provides factory methods for single row functions.
 */
public final class SingleRowFunctions {
    private SingleRowFunctions() {}

    /**
     * Creates the UPPER function for a string expression.
     * @param expression the string expression.
     * @return the string function.
     */
    public static Upper upper(Expression expression) {
        return new Upper(expression, null);
    }

    /**
     * Creates the UPPER function for a string constant value.
     * @param constantValue the string value.
     * @return the string function.
     */
    public static Upper upper(String constantValue) {
        return upper($(constantValue));
    }

    /**
     * Creates the LOWER function for a string expression.
     * @param expression the string expression.
     * @return the string function.
     */
    public static Lower lower(Expression expression) {
        return new Lower(expression, null);
    }

    /**
     * Creates the LOWER function for a string constant value.
     * @param constantValue the string value.
     * @return the string function.
     */
    public static Lower lower(String constantValue) {
        return lower($(constantValue));
    }

    /**
     * Creates the TO_NUMBER function converting from a string expression to decimal number.
     * @param expression the string expression.
     * @param precision the total number of digits (including decimals).
     * @param scale the number of decimals.
     * @return the conversion function.
     */
    public static ToNumber toNumber(Expression expression, int precision, int scale) {
        return new ToNumber(expression, precision, scale, null);
    }

    /**
     * Creates the TO_NUMBER function converting from a string constant value to decimal number.
     * @param constantValue the string value.
     * @param precision the total number of digits (including decimals).
     * @param scale the number of decimals.
     * @return the conversion function.
     */
    public static ToNumber toNumber(String constantValue, int precision, int scale) {
        return toNumber($(constantValue), precision, scale);
    }

    /**
     * Creates the TO_NUMBER function converting from a string expression to integer.
     * @param expression the string expression.
     * @param precision the total number of digits.
     * @return the conversion function.
     */
    public static ToNumber toNumber(Expression expression, int precision) {
        return new ToNumber(expression, precision, 0, null);
    }

    /**
     * Creates the TO_NUMBER function converting from a string constant value to integer.
     * @param constantValue the string value.
     * @param precision the total number of digits.
     * @return the conversion function.
     */
    public static ToNumber toNumber(String constantValue, int precision) {
        return toNumber($(constantValue), precision);
    }

    /**
     * Creates the SUBSTRING function for a string expression.
     * @param expression the string expression.
     * @param startPos the substring start position, 1-based.
     * @param length the substring length.
     * @return the string function.
     */
    public static Substring substring(Expression expression, int startPos, int length) {
        return new Substring(expression, startPos, length, null);
    }

    /**
     * Creates the SUBSTRING function for a string constant value.
     * @param constantValue the string value.
     * @param startPos the substring start position, 1-based.
     * @param length the substring length.
     * @return the string function.
     */
    public static Substring substring(String constantValue, int startPos, int length) {
        return substring($(constantValue), startPos, length);
    }

    /**
     * Creates the TO_CHAR function converting from a timestamp/number expression to string.
     * @param expression the timestamp or number expression.
     * @param format the format specifier.
     * @return the conversion function.
     */
    public static ToChar toChar(Expression expression, String format) {
        return new ToChar(expression, format, null);
    }

    /**
     * Creates the CONCAT function for two or more string expressions.
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
     * Creates the LENGTH function for a string expression.
     * @param expression the string expression.
     * @return the string function.
     */
    public static Length length(Expression expression) {
        return new Length(expression, null);
    }

    /**
     * Creates the LENGTH function for a string constant value.
     * @param constantValue the string value.
     * @return the string function.
     */
    public static Length length(String constantValue) {
        return length($(constantValue));
    }

    /**
     * Creates the ROUND function for a numeric expression.
     * @param expression the numeric expression.
     * @return the numeric function.
     */
    public static Round round(Expression expression) {
        return new Round(expression, null);
    }

    /**
     * Creates the ROUND function for a numeric constant value.
     * @param constantValue the numeric value.
     * @return the numeric function.
     */
    public static Round round(Number constantValue) {
        return round($(constantValue));
    }

    /**
     * Creates the ABS function for a numeric expression.
     * @param expression the numeric expression.
     * @return the numeric function.
     */
    public static Abs abs(Expression expression) {
        return new Abs(expression, null);
    }

    /**
     * Creates the ABS function for a numeric constant value.
     * @param constantValue the numeric value.
     * @return the numeric function.
     */
    public static Abs abs(Number constantValue) {
        return abs($(constantValue));
    }

    /**
     * Creates the CEIL function for a numeric expression.
     * @param expression the numeric expression.
     * @return the numeric function.
     */
    public static Ceil ceil(Expression expression) {
        return new Ceil(expression, null);
    }

    /**
     * Creates the CEIL function for a numeric constant value.
     * @param constantValue the numeric value.
     * @return the numeric function.
     */
    public static Ceil ceil(Number constantValue) {
        return ceil($(constantValue));
    }

    /**
     * Creates the FLOOR function for a numeric expression.
     * @param expression the numeric expression.
     * @return the numeric function.
     */
    public static Floor floor(Expression expression) {
        return new Floor(expression, null);
    }

    /**
     * Creates the FLOOR function for a numeric constant value.
     * @param constantValue the numeric value.
     * @return the numeric function.
     */
    public static Floor floor(Number constantValue) {
        return floor($(constantValue));
    }

    /**
     * Creates the LN (natural logarithm) function for a numeric expression.
     * @param expression the numeric expression.
     * @return the numeric function.
     */
    public static Ln ln(Expression expression) {
        return new Ln(expression, null);
    }

    /**
     * Creates the LN (natural logarithm) function for a numeric constant value.
     * @param constantValue the numeric value.
     * @return the numeric function.
     */
    public static Ln ln(Number constantValue) {
        return ln($(constantValue));
    }

    /**
     * Creates the EXP (natural exponential) function for a numeric expression.
     * @param expression the numeric expression.
     * @return the numeric function.
     */
    public static Exp exp(Expression expression) {
        return new Exp(expression, null);
    }

    /**
     * Creates the EXP (natural exponential) function for a numeric constant value.
     * @param constantValue the numeric value.
     * @return the numeric function.
     */
    public static Exp exp(Number constantValue) {
        return exp($(constantValue));
    }

    /**
     * Creates the SQRT (square root) function for a numeric expression.
     * @param expression the numeric expression.
     * @return the numeric function.
     */
    public static Sqrt sqrt(Expression expression) {
        return new Sqrt(expression, null);
    }

    /**
     * Creates the SQRT (square root) function for a numeric constant value.
     * @param constantValue the numeric value.
     * @return the numeric function.
     */
    public static Sqrt sqrt(Number constantValue) {
        return sqrt($(constantValue));
    }

    /**
     * Creates the POW (exponentiation) function for a numeric expression.
     * @param base the numeric base expression.
     * @param exponent the numeric exponent expression.
     * @return the numeric function.
     */
    public static Power pow(Expression base, Expression exponent) {
        return new Power(base, exponent, null);
    }

    /**
     * Creates the POW (exponentiation) function for a numeric expression with constant value exponent.
     * @param base the numeric base expression.
     * @param exponent the numeric exponent value.
     * @return the numeric function.
     */
    public static Power pow(Expression base, Number exponent) {
        return pow(base, $(exponent));
    }

    /**
     * Creates the CAST function for an expression.
     * @param expression the expression.
     * @return the CAST function builder.
     */
    public static CastBuilder cast(Expression expression) {
        return new CastBuilder(expression);
    }

    /**
     * Creates the CAST function for a constant value.
     * @param value the constant value.
     * @return the CAST function builder.
     */
    public static CastBuilder cast(String value) {
        return cast($(value));
    }

    /**
     * Creates the CAST function for a constant value.
     * @param value the constant value.
     * @return the CAST function builder.
     */
    public static CastBuilder cast(Number value) {
        return cast($(value));
    }
}
