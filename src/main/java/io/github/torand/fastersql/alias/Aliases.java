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
package io.github.torand.fastersql.alias;

/**
 * Provides factory methods for aliases.
 */
public final class Aliases {
    private Aliases() {}

    /**
     * Creates a column alias with specified name.
     * @param alias the alias name.
     * @return the column alias.
     */
    public static ColumnAlias alias(String alias) {
        return new ColumnAlias(alias);
    }

    /**
     * Creates a reference to a specified column in a specified table using aliases.
     * @param tableAlias the table alias.
     * @param columnAlias the column alias.
     * @return the column reference.
     */
    public static ColumnRef colRef(String tableAlias, String columnAlias) {
        return new ColumnRef(tableAlias, columnAlias, null);
    }
}
