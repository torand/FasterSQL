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
package io.github.torand.fastersql.statement;

import io.github.torand.fastersql.Table;
import io.github.torand.fastersql.projection.Projection;

import java.util.Collection;
import java.util.List;

import static java.util.Collections.singletonList;
import static java.util.Objects.requireNonNull;
import static io.github.torand.fastersql.constant.Constants.constant;
import static io.github.torand.fastersql.util.collection.CollectionHelper.asNonEmptyList;

public final class Statements {
    private Statements() {}

    public static SelectFromBuilder select(long value) {
        return new SelectFromBuilder(false, singletonList(constant(value)));
    }

    public static SelectFromBuilder select(Projection firstProjection, Projection... moreProjections) {
        requireNonNull(firstProjection, "First projection is null");
        List<Projection> projections = asNonEmptyList(firstProjection, moreProjections);
        return new SelectFromBuilder(false, projections);
    }

    public static SelectFromBuilder selectDistinct(Projection firstProjection, Projection... moreProjections) {
        requireNonNull(firstProjection, "First projection is null");
        List<Projection> projections = asNonEmptyList(firstProjection, moreProjections);
        return new SelectFromBuilder(true, projections);
    }

    public static UpdateStatement update(Table table) {
        return new UpdateStatement(table, null, null);
    }

    public static DeleteFromBuilder delete() {
        return new DeleteFromBuilder();
    }

    public static DeleteStatement deleteFrom(Table table) {
        return new DeleteStatement(table, null);
    }

    public static TruncateTableBuilder truncate() {
        return new TruncateTableBuilder();
    }

    public static TruncateStatement truncateTable(Table table) {
        return new TruncateStatement(table);
    }

    public static InsertIntoBuilder insert() {
        return new InsertIntoBuilder();
    }

    public static InsertStatement insertInto(Table table) {
        return new InsertStatement(table, null);
    }

    public static <T> InsertBatchIntoBuilder<T> insertBatch(Collection<T> entities) {
        return new InsertBatchIntoBuilder<>(entities);
    }
}
