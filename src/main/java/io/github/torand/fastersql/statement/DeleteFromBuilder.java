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
package io.github.torand.fastersql.statement;

import io.github.torand.fastersql.model.Table;

import static java.util.Objects.requireNonNull;

/**
 * Builder of DELETE statements with a FROM clause.
 */
public class DeleteFromBuilder {
    DeleteFromBuilder() {}

    /**
     * Creates a DELETE statement with specified FROM clause.
     * @param table the table to delete from.
     * @return the statement.
     */
    public DeleteStatement from(Table table) {
        return new DeleteStatement(requireNonNull(table, "No table specified"), null);
    }
}
