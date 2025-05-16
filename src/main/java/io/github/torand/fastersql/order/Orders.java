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
package io.github.torand.fastersql.order;

/**
 * Provides factory methods for order clauses.
 */
public final class Orders {
    private Orders() {}

    /**
     * Creates an ascending ordering of an expression.
     * @param expression the expression.
     * @return the order clause.
     */
    public static Ascending asc(OrderExpression expression) {
        return new Ascending(expression);
    }

    /**
     * Creates a descending ordering of an expression.
     * @param expression the expression.
     * @return the order clause.
     */
    public static Descending desc(OrderExpression expression) {
        return new Descending(expression);
    }
}
