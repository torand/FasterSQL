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

import static io.github.torand.fastersql.Command.SELECT;
import static io.github.torand.fastersql.util.contract.Requires.requireNonBlank;

public abstract class Table<ENTITY extends Table<?>> {
    private final String name;
    private final String alias;
    private final TableFactory<ENTITY> tableFactory;

    protected Table(String name, TableFactory<ENTITY> tableFactory) {
        this.name = requireNonBlank(name, "No name specified");
        this.alias = defaultAlias(name);
        this.tableFactory = tableFactory;
    }

    protected Table(String name, String alias, TableFactory<ENTITY> tableFactory) {
        this.name = requireNonBlank(name, "No name specified");
        this.alias = requireNonBlank(alias, "No name specified");
        this.tableFactory = tableFactory;
    }

    public ENTITY as(String alias) {
        return tableFactory.newInstance(alias);
    }

    public Field field(String name) {
        return new Field(this, name);
    }

    public String name() {
        return name;
    }

    public String alias() {
        return alias;
    }

    public String sql(Context context) {
        if (context.isCommand(SELECT)) {
            return name + " " + alias;
        } else {
            return name;
        }
    }

    private String defaultAlias(String name) {
        return name;
    }

    @FunctionalInterface
    public interface TableFactory<ENTITY> {
        ENTITY newInstance(String alias);
    }
}
