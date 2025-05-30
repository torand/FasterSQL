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

import java.util.Collection;
import java.util.List;

import static io.github.torand.fastersql.util.collection.CollectionHelper.asList;
import static io.github.torand.fastersql.util.contract.Requires.requireNonEmpty;
import static java.util.Objects.requireNonNull;

/**
 * Builder of INSERT (batch) statements with an INTO clause.
 * @param <T> the batch entity type.
 */
public class InsertBatchIntoBuilder<T> {
    private final List<? extends T> entities;

    InsertBatchIntoBuilder(Collection<? extends T> entities) {
        this.entities = asList(requireNonEmpty(entities, "No entities specified"));
    }

    /**
     * Creates an INSERT (batch) statement with specified INTO clause.
     * @param table the table to insert into.
     * @return the statement.
     */
    public InsertBatchStatement<? extends T> into(Table<?> table) {
        return new InsertBatchStatement<>(entities, requireNonNull(table, "No table specified"), null);
    }
}
