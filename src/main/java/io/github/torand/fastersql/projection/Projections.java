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
package io.github.torand.fastersql.projection;

import io.github.torand.fastersql.statement.SelectStatement;
import io.github.torand.fastersql.subquery.ExpressionSubquery;

/**
 * Provides factory methods for projections.
 */
public final class Projections {
    private Projections() {}

    /**
     * Creates a numeric reference to a projected column in a query result.
     * @param position the position, 1-based
     * @return the column position.
     */
    public static ColumnPosition colPos(int position) {
        return new ColumnPosition(position);
    }

    /**
     * Creates a subquery expression to be used as a projection.
     * @param query the query.
     * @return the subquery expression.
     */
    public static ExpressionSubquery subquery(SelectStatement query) {
        return new ExpressionSubquery(query);
    }
}
