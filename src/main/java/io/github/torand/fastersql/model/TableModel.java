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

/**
 * Models a database table.
 * @param <T> the concrete table model class with columns.
 */
public abstract class TableModel<T extends TableModel<?>> extends Table {
    private final TableModelFactory<T> instanceFactory;

    /**
     * Creates a representation (model) of a database table.
     * @param name the table name.
     * @param alias the table alias.
     * @param instanceFactory the table model instance factory.
     */
    protected TableModel(String name, String alias, TableModelFactory<T> instanceFactory) {
        super(name, alias);
        this.instanceFactory = instanceFactory;
    }

    /**
     * Creates a model of a column belonging to this table.
     * @param name the column name.
     * @return the column model.
     */
    public Column column(String name) {
        return new Column(this, name);
    }

    // Relation

    @Override
    public T as(String alias) {
        return instanceFactory.newInstance(alias);
    }
}
