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
package io.github.torand.fastersql.model;

import io.github.torand.fastersql.alias.TableAlias;
import io.github.torand.fastersql.relation.Relation;
import io.github.torand.fastersql.sql.Context;

import java.util.stream.Stream;

import static io.github.torand.fastersql.sql.Command.SELECT;
import static io.github.torand.javacommons.contract.Requires.requireNonBlank;

/**
 * Represents a database table.
 */
public class Table implements Relation {
    private final String name;
    private final TableAlias alias;

    /**
     * Creates a representation of a database table.
     * @param name the table name.
     */
    protected Table(String name) {
        this.name = requireNonBlank(name, "No name specified");
        this.alias = defaultAlias(name);
    }

    /**
     * Creates a representation of a database table.
     * @param name the table name.
     * @param alias the table alias.
     */
    protected Table(String name, String alias) {
        this.name = requireNonBlank(name, "No name specified");
        this.alias = new TableAlias(requireNonBlank(alias, "No alias specified"));
    }

    /**
     * Gets the table name.
     * @return the table name.
     */
    public String name() {
        return name;
    }

    // Sql

    @Override
    public String sql(Context context) {
        if (context.isCommand(SELECT)) {
            return name + " " + alias.sql(context);
        } else {
            return name;
        }
    }

    @Override
    public Stream<Object> params(Context context) {
        return Stream.empty();
    }

    // Relation

    @Override
    public Table as(String alias) {
        return new Table(alias);
    }

    @Override
    public TableAlias alias() {
        return alias;
    }

    private TableAlias defaultAlias(String name) {
        return new TableAlias(name);
    }
}
