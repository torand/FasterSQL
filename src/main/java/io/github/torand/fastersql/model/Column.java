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

import io.github.torand.fastersql.alias.ColumnAlias;
import io.github.torand.fastersql.expression.Expression;
import io.github.torand.fastersql.join.Join;
import io.github.torand.fastersql.order.OrderExpression;
import io.github.torand.fastersql.predicate.LeftOperand;
import io.github.torand.fastersql.projection.Projection;
import io.github.torand.fastersql.sql.Context;

import java.util.Optional;
import java.util.stream.Stream;

import static io.github.torand.fastersql.sql.Command.SELECT;
import static io.github.torand.javacommons.contract.Requires.requireNonBlank;
import static java.util.Objects.requireNonNull;

/**
 * Represents a column in a database table.
 */
public class Column implements LeftOperand, Expression, OrderExpression {
    private final Table table;
    private final String name;
    private final ColumnAlias alias;

    /**
     * Creates a representation of a column inside a database table.
     * @param table the table representation.
     * @param name the column name.
     */
    public Column(Table table, String name) {
        this.table = requireNonNull(table, "No table specified");
        this.name = requireNonBlank(name, "No name specified");
        this.alias = defaultAlias(table, name);
    }

    private Column(Table table, String name, String alias) {
        this.table = requireNonNull(table, "No table specified");
        this.name = requireNonBlank(name, "No name specified");
        this.alias = new ColumnAlias(requireNonBlank(alias, "No alias specified"));
    }

    /**
     * Creates a JOIN clause by associating this column with specified column of another table.
     * @param other the other column.
     * @return the JOIN clause.
     */
    public Join on(Column other) {
        return new Join(this, other);
    }

    /**
     * Gets the column name.
     * @return the column name.
     */
    public String name() {
        return name;
    }

    /**
     * Gets the table this column belongs to.
     * @return the table.
     */
    public Table table() {
        return table;
    }

    // Sql

    @Override
    public String sql(Context context) {
        if (context.isCommand(SELECT)) {
            return table.alias().name() + "." + name;
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
        return Stream.of(this);
    }

    @Override
    public Stream<ColumnAlias> aliasRefs() {
        return Stream.of(alias);
    }

    // Projection

    @Override
    public Projection as(String alias) {
        return new Column(table, name, alias);
    }

    @Override
    public Optional<ColumnAlias> alias() {
        return Optional.ofNullable(alias);
    }

    private ColumnAlias defaultAlias(Table table, String name) {
        return new ColumnAlias((table.alias().name() + "_" + name).toUpperCase());
    }
}
