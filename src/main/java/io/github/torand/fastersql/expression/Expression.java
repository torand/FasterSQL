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
 * Represents an expression resulting in a scalar value or a table with rows and columns.
 */
public interface Expression extends Projection {

    default Expression plus(Object value) {
        return ArithmeticExpressions.add(this, $(value));
    }

    default Expression plus(Expression expression) {
        return ArithmeticExpressions.add(this, expression);
    }

    default Expression minus(Object value) {
        return ArithmeticExpressions.subtract(this, $(value));
    }

    default Expression minus(Expression expression) {
        return ArithmeticExpressions.subtract(this, expression);
    }

    default Expression times(Object value) {
        return ArithmeticExpressions.multiply(this, $(value));
    }

    default Expression times(Expression expression) {
        return ArithmeticExpressions.multiply(this, expression);
    }

    default Expression dividedBy(Object value) {
        return ArithmeticExpressions.divide(this, $(value));
    }

    default Expression dividedBy(Expression expression) {
        return ArithmeticExpressions.divide(this, expression);
    }

    default Expression mod(Object value) {
        return ArithmeticExpressions.mod(this, $(value));
    }

    default Expression mod(Expression expression) {
        return ArithmeticExpressions.mod(this, expression);
    }
}
