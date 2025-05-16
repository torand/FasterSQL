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
package io.github.torand.fastersql.function.aggregate;

import io.github.torand.fastersql.expression.Expression;

/**
 * Provides factory methods for aggregate functions.
 */
public final class AggregateFunctions {
    private AggregateFunctions() {}

    /**
     * Creates the maximum aggregate of an expression.
     * @param expression the expression.
     * @return the aggregate function.
     */
    public static Max max(Expression expression) {
        return new Max(expression, null);
    }

    /**
     * Creates the minimum aggregate of an expression.
     * @param expression the expression.
     * @return the aggregate function.
     */
    public static Min min(Expression expression) {
        return new Min(expression, null);
    }

    /**
     * Creates the count aggregate of an expression.
     * @param expression the expression.
     * @return the aggregate function.
     */
    public static Count count(Expression expression) {
        return new Count(expression, null);
    }

    /**
     * Creates the count aggregate of all rows.
     * @return the aggregate function.
     */
    public static CountAll count() {
        return new CountAll(null);
    }

    /**
     * Creates the sum aggregate of an expression.
     * @param expression the expression.
     * @return the aggregate function.
     */
    public static Sum sum(Expression expression) {
        return new Sum(expression, null);
    }

    /**
     * Creates the average aggregate of an expression.
     * @param expression the expression.
     * @return the aggregate function.
     */
    public static Avg avg(Expression expression) {
        return new Avg(expression, null);
    }
}
