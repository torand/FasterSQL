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
package io.github.torand.fastersql;

import io.github.torand.fastersql.alias.ColumnAlias;
import io.github.torand.fastersql.alias.TableAlias;
import io.github.torand.fastersql.relation.Relation;

import java.util.stream.Stream;

import static io.github.torand.fastersql.Command.SELECT;
import static io.github.torand.fastersql.util.contract.Requires.requireNonBlank;

public abstract class Table<ENTITY extends Table<?>> implements Relation {
    private final String name;
    private final TableAlias alias;
    private final TableFactory<ENTITY> tableFactory;

    protected Table(String name, TableFactory<ENTITY> tableFactory) {
        this.name = requireNonBlank(name, "No name specified");
        this.alias = defaultAlias(name);
        this.tableFactory = tableFactory;
    }

    protected Table(String name, String alias, TableFactory<ENTITY> tableFactory) {
        this.name = requireNonBlank(name, "No name specified");
        this.alias = new TableAlias(requireNonBlank(alias, "No alias specified"));
        this.tableFactory = tableFactory;
    }

    public Column column(String name) {
        return new Column(this, name);
    }

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

    @Override
    public Stream<Column> columnRefs() {
        return Stream.empty();
    }

    @Override
    public Stream<ColumnAlias> aliasRefs() {
        return Stream.empty();
    }

    // Relation

    @Override
    public ENTITY as(String alias) {
        return tableFactory.newInstance(alias);
    }

    @Override
    public TableAlias alias() {
        return alias;
    }

    private TableAlias defaultAlias(String name) {
        return new TableAlias(name);
    }

    @FunctionalInterface
    public interface TableFactory<ENTITY> {
        ENTITY newInstance(String alias);
    }
}
