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

import io.github.torand.fastersql.condition.Condition;
import io.github.torand.fastersql.condition.LeftOperand;
import io.github.torand.fastersql.condition.comparison.ComparisonConditions;
import io.github.torand.fastersql.expression.Expression;
import io.github.torand.fastersql.order.Order;
import io.github.torand.fastersql.order.Orders;
import io.github.torand.fastersql.projection.Projection;

import java.util.stream.Stream;

import static io.github.torand.fastersql.Command.SELECT;
import static io.github.torand.fastersql.util.contract.Requires.requireNonBlank;
import static java.util.Objects.requireNonNull;

public class Field implements Projection, LeftOperand, Expression {
    private final Table<?> table;
    private final String name;
    private final String alias;

    public Field(Table<?> table, String name) {
        this.table = requireNonNull(table, "No table specified");
        this.name = requireNonBlank(name, "No name specified");
        this.alias = defaultAlias(table, name);
    }

    private Field(Table<?> table, String name, String alias) {
        this.table = requireNonNull(table, "No table specified");
        this.name = requireNonBlank(name, "No name specified");
        this.alias = requireNonBlank(alias, "No alias specified");
    }

    // Sql

    @Override
    public String sql(Context context) {
        if (context.isCommand(SELECT)) {
            return table.alias() + "." + name;
        } else {
            return name;
        }
    }

    @Override
    public Stream<Object> params(Context context) {
        return Stream.empty();
    }

    // Projection

    @Override
    public Projection as(String alias) {
        return new Field(table, name, alias);
    }

    @Override
    public String alias() {
        return alias;
    }

    // LeftOperand / Expression

    @Override
    public Stream<Field> fieldRefs() {
        return Stream.of(this);
    }

    public Condition isNull() {
        return ComparisonConditions.isNull(this);
    }

    public Join on(Field other) {
        return new Join(this, other);
    }

    public Order asc() {
        return Orders.asc(this);
    }

    public Order ascIf(boolean predicate) {
        return predicate ? asc() : desc();
    }

    public Order desc() {
        return Orders.desc(this);
    }

    public String name() {
        return name;
    }

    public Table<?> table() {
        return table;
    }

    private String defaultAlias(Table<?> table, String name) {
        return (table.alias() + "_" + name).toUpperCase();
    }
}
