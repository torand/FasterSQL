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
package io.github.torand.fastersql.alias;

import io.github.torand.fastersql.sql.Context;

import java.util.stream.Stream;

import static io.github.torand.javacommons.contract.Requires.requireNonBlank;

/**
 * Represents an alias (label) for a table
 */
public class TableAlias implements Alias {
    private final String name;

    /**
     * Creates a table alias.
     * @param name the alias name.
     */
    public TableAlias(String name) {
        this.name = requireNonBlank(name, "No name specified");
    }

    @Override
    public String name() {
        return name;
    }

    // Sql

    @Override
    public String sql(Context context) {
        return name.contains(" ") ? "\"" + name + "\"" : name;
    }

    @Override
    public Stream<Object> params(Context context) {
        return Stream.empty();
    }
}
