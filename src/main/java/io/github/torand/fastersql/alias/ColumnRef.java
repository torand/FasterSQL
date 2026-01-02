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

import io.github.torand.fastersql.expression.Expression;
import io.github.torand.fastersql.order.OrderExpression;
import io.github.torand.fastersql.predicate.LeftOperand;
import io.github.torand.fastersql.projection.Projection;
import io.github.torand.fastersql.sql.Context;

import java.util.Optional;
import java.util.stream.Stream;

import static io.github.torand.javacommons.contract.Requires.requireNonBlank;
import static io.github.torand.javacommons.lang.StringHelper.nonBlank;

/**
 * References a column using a combination of a table alias and a column alias.
 * Typically used to refer to columns in a subquery (inline view).
 */
public class ColumnRef implements LeftOperand, OrderExpression, Expression {
    private final TableAlias tableAlias;
    private final ColumnAlias columnAlias;
    private final ColumnAlias alias;

    ColumnRef(String tableAlias, String columnAlias, String alias) {
        this.tableAlias = new TableAlias(requireNonBlank(tableAlias, "No table alias specified"));
        this.columnAlias = new ColumnAlias(requireNonBlank(columnAlias, "No column alias specified"));
        this.alias = nonBlank(alias) ? new ColumnAlias(alias) : null;
    }

    // Sql

    @Override
    public String sql(Context context) {
        return tableAlias.sql(context) + "." + columnAlias.sql(context);
    }

    @Override
    public Stream<Object> params(Context context) {
        return Stream.empty();
    }

    @Override
    public Stream<ColumnAlias> aliasRefs() {
        return Stream.of(columnAlias);
    }

    // Projection

    @Override
    public Projection as(String alias) {
        requireNonBlank(alias, "No alias specified");
        return new ColumnRef(tableAlias.name(), columnAlias.name(), alias);
    }

    @Override
    public Optional<ColumnAlias> alias() {
        return Optional.ofNullable(alias);
    }
}
