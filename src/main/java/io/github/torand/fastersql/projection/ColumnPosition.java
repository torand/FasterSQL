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

import io.github.torand.fastersql.alias.ColumnAlias;
import io.github.torand.fastersql.model.Column;
import io.github.torand.fastersql.order.OrderExpression;
import io.github.torand.fastersql.sql.Context;
import io.github.torand.fastersql.sql.Sql;

import java.util.stream.Stream;

import static io.github.torand.javacommons.contract.Requires.require;

/**
 * Represents an enumeration of a projection, that is, a 1-based number referencing a single projection element in a SELECT clause.
 */
public class ColumnPosition implements Sql, OrderExpression {
    private final int position;

    /**
     * Creates a column position.
     * @param position the 1-based position.
     */
    public ColumnPosition(int position) {
        require(() -> position >= 1, "Position must be 1 or greater");
        this.position = position;
    }

    // Sql

    @Override
    public String sql(Context context) {
        return Integer.toString(position);
    }

    @Override
    public Stream<Object> params(Context context) {
        return Stream.empty();
    }

    @Override
    public Stream<Column> columnRefs() {
        return Stream.empty();
    }

    @Override
    public Stream<ColumnAlias> aliasRefs() {
        return Stream.empty();
    }
}
