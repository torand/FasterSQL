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
import io.github.torand.fastersql.expression.arithmetic.ArithmeticExpressions;
import io.github.torand.fastersql.projection.Projection;

import java.util.stream.Stream;

import static io.github.torand.fastersql.constant.Constants.constant;

/**
 * Represents an expression resulting in a scalar value or a table with rows and columns.
 */
public interface Expression extends Sql, Projection {

    Stream<Field> fieldRefs();

    default Expression plus(Object value) {
        return ArithmeticExpressions.plus(this, constant(value));
    }

    default Expression plus(Expression expression) {
        return ArithmeticExpressions.plus(this, expression);
    }

    default Expression minus(Object value) {
        return ArithmeticExpressions.minus(this, constant(value));
    }

    default Expression minus(Expression expression) {
        return ArithmeticExpressions.minus(this, expression);
    }

    default Expression times(Object value) {
        return ArithmeticExpressions.times(this, constant(value));
    }

    default Expression times(Expression expression) {
        return ArithmeticExpressions.times(this, expression);
    }

    default Expression divideBy(Object value) {
        return ArithmeticExpressions.divideBy(this, constant(value));
    }

    default Expression divideBy(Expression expression) {
        return ArithmeticExpressions.divideBy(this, expression);
    }
}
