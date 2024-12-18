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

import io.github.torand.fastersql.expression.Expression;
import io.github.torand.fastersql.expression.LeftOperand;
import io.github.torand.fastersql.expression.comparison.ComparisonExpressions;
import io.github.torand.fastersql.order.Order;
import io.github.torand.fastersql.order.Orders;
import io.github.torand.fastersql.projection.Projection;

import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static io.github.torand.fastersql.Command.SELECT;
import static io.github.torand.fastersql.util.contract.Requires.requireNonBlank;

public class Field implements Projection, LeftOperand {
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

    @Override
    public Projection as(String alias) {
        return new Field(table, name, alias);
    }

    @Override
    public Stream<Field> fields() {
        return Stream.of(this);
    }

    public Expression isNull() {
        return ComparisonExpressions.isNull(this);
    }

    public Join on(Field other) {
        return new Join(this, other);
    }

    public Order asc() {
        return Orders.asc(this);
    }

    public Order ascIf(boolean condition) {
        return condition ? asc() : desc();
    }

    public Order desc() {
        return Orders.desc(this);
    }

    public String name() {
        return name;
    }

    @Override
    public String alias() {
        return alias;
    }

    public Table<?> table() {
        return table;
    }

    @Override
    public String sql(Context context) {
        if (context.isCommand(SELECT)) {
            return table.alias() + "." + name;
        } else {
            return name;
        }
    }

    private String defaultAlias(Table<?> table, String name) {
        return (table.alias() + "_" + name).toUpperCase();
    }
}
