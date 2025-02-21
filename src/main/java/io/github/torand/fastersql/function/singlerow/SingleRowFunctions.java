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
package io.github.torand.fastersql.function.singlerow;

import io.github.torand.fastersql.expression.Expression;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

public final class SingleRowFunctions {
    private SingleRowFunctions() {}

    public static Upper upper(Expression expression) {
        return new Upper(expression, null);
    }

    public static Lower lower(Expression expression) {
        return new Lower(expression, null);
    }

    public static ToNumber to_number(Expression expression, int precision, int scale) {
        return new ToNumber(expression, precision, scale, null);
    }

    public static ToNumber to_number(Expression expression, int precision) {
        return new ToNumber(expression, precision, 0, null);
    }

    public static Substring substring(Expression expression, int startPos, int length) {
        return new Substring(expression, startPos, length, null);
    }

    public static ToChar toChar(Expression expression, String format) {
        return new ToChar(expression, format, null);
    }

    public static Concat concat(Expression expression1, Expression expression2, Expression... otherExpressions) {
        List<Expression> expressions = new ArrayList<>();
        expressions.add(requireNonNull(expression1, "First expression is null"));
        expressions.add(requireNonNull(expression2, "Second expression is null"));
        if (nonNull(otherExpressions)) {
            expressions.addAll(List.of(otherExpressions));
        }

        return new Concat(expressions, null);
    }

    public static Length length(Expression expression) {
        return new Length(expression, null);
    }

    public static Round round(Expression expression) {
        return new Round(expression, null);
    }

    public static Abs abs(Expression expression) {
        return new Abs(expression, null);
    }

    public static Ceil ceil(Expression expression) {
        return new Ceil(expression, null);
    }

    public static Floor floor(Expression expression) {
        return new Floor(expression, null);
    }
}
