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

import static java.util.Objects.requireNonNull;

/**
 * Builder for TRUNCATE statements with a TABLE clause.
 */
public class TruncateTableBuilder {

    /**
     * Creates a TRUNCATE statement with specified TABLE clause.
     * @param table the TABLE clause.
     * @return the statement.
     */
    public TruncateStatement table(Table<?> table) {
        return new TruncateStatement(requireNonNull(table, "No table specified"));
    }
}
