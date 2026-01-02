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
package io.github.torand.fastersql.order;

import io.github.torand.fastersql.sql.Sql;

/**
 * Defines an expression specifying the ordering of rows from a query.
 */
public interface OrderExpression extends Sql {

    /**
     * Creates an ascending ordering of this expression.
     * @return the order clause.
     */
    default Order asc() {
        return Orders.asc(this);
    }

    /**
     * Creates a descending ordering of this expression.
     * @return the order clause.
     */
    default Order desc() {
        return Orders.desc(this);
    }

    /**
     * Creates an ordering of this expression, based on a condition.
     * If condition is true, an ascending ordering is created.
     * If condition is false, a descending ordering is created.
     * @param condition the condition.
     * @return the order clause.
     */
    default Order ascIf(boolean condition) {
        return condition ? asc() : desc();
    }
}
