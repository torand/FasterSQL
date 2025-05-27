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
package io.github.torand.fastersql.statement;

import io.github.torand.fastersql.model.Table;
import io.github.torand.fastersql.projection.Projection;

import java.util.Collection;
import java.util.List;

import static io.github.torand.fastersql.util.collection.CollectionHelper.asNonEmptyList;
import static java.util.Objects.requireNonNull;

/**
 * Provides factory methods for statements and statement builders.
 */
public final class Statements {
    private Statements() {}

    /**
     * Creates a SELECT statement builder.
     * @param firstProjection the first projection.
     * @param moreProjections the other projections, if any.
     * @return the statement builder.
     */
    public static SelectFromBuilder select(Projection firstProjection, Projection... moreProjections) {
        requireNonNull(firstProjection, "First projection is null");
        List<Projection> projections = asNonEmptyList(firstProjection, moreProjections);
        return new SelectFromBuilder(false, projections);
    }

    /**
     * Creates a SELECT statement builder with DISTINCT clause added.
     * @param firstProjection the first projection.
     * @param moreProjections the other projections, if any.
     * @return the statement builder.
     */
    public static SelectFromBuilder selectDistinct(Projection firstProjection, Projection... moreProjections) {
        requireNonNull(firstProjection, "First projection is null");
        List<Projection> projections = asNonEmptyList(firstProjection, moreProjections);
        return new SelectFromBuilder(true, projections);
    }

    /**
     * Creates an UPDATE statement for specified table.
     * @param table the table
     * @return the statement.
     */
    public static UpdateStatement update(Table<?> table) {
        return new UpdateStatement(table, null, null);
    }

    /**
     * Creates a DELETE statement builder.
     * @return the statement builder.
     */
    public static DeleteFromBuilder delete() {
        return new DeleteFromBuilder();
    }

    /**
     * Creates a DELETE statement with specified FROM clause.
     * @param table the FROM clause
     * @return the statement.
     */
    public static DeleteStatement deleteFrom(Table<?> table) {
        return new DeleteStatement(table, null);
    }

    /**
     * Creates a TRUNCATE statement builder.
     * @return the statement builder.
     */
    public static TruncateTableBuilder truncate() {
        return new TruncateTableBuilder();
    }

    /**
     * Creates a TRUNCATE statement with specified TABLE clause.
     * @param table the TABLE clause.
     * @return the statement.
     */
    public static TruncateStatement truncateTable(Table<?> table) {
        return new TruncateStatement(table);
    }

    /**
     * Creates an INSERT statement builder.
     * @return the statement builder.
     */
    public static InsertIntoBuilder insert() {
        return new InsertIntoBuilder();
    }

    /**
     * Creates an INSERT statement with specified INTO clause.
     * @param table the INTO clause.
     * @return the statement builder.
     */
    public static InsertStatement insertInto(Table<?> table) {
        return new InsertStatement(table, null);
    }

    /**
     * Creates an INSERT (batch) statement builder, for specified entities.
     * @param entities the entities
     * @return the statement builder.
     * @param <T> the entity type.
     */
    public static <T> InsertBatchIntoBuilder<T> insertBatch(Collection<T> entities) {
        return new InsertBatchIntoBuilder<>(entities);
    }
}
