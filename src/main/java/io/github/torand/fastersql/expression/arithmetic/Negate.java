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
package io.github.torand.fastersql.expression.arithmetic;

import io.github.torand.fastersql.alias.ColumnAlias;
import io.github.torand.fastersql.expression.Expression;
import io.github.torand.fastersql.model.Column;
import io.github.torand.fastersql.order.OrderExpression;
import io.github.torand.fastersql.projection.Projection;
import io.github.torand.fastersql.sql.Context;

import java.util.Optional;
import java.util.stream.Stream;

import static io.github.torand.javacommons.contract.Requires.requireNonBlank;
import static io.github.torand.javacommons.lang.StringHelper.nonBlank;
import static java.util.Objects.requireNonNull;

/**
 * Implements the negate numerical function.
 */
public class Negate implements Expression, OrderExpression {
    private final Expression expression;
    private final ColumnAlias alias;

    Negate(Expression expression, String alias) {
        this.expression = requireNonNull(expression, "No expression specified");
        this.alias = nonBlank(alias) ? new ColumnAlias(alias) : defaultAlias();
    }

    // Sql

    @Override
    public String sql(Context context) {
        String exprSql = expression.sql(context);
        if (expression instanceof Addition || expression instanceof Subtraction) {
            exprSql = "(" + exprSql + ")";
        }

        return "-" + exprSql;
    }

    @Override
    public Stream<Object> params(Context context) {
        return expression.params(context);
    }

    @Override
    public Stream<Column> columnRefs() {
        return expression.columnRefs();
    }

    @Override
    public Stream<ColumnAlias> aliasRefs() {
        return expression.aliasRefs();
    }

    // Projection

    @Override
    public Projection as(String alias) {
        requireNonBlank(alias, "No alias specified");
        return new Negate(expression, alias);
    }

    @Override
    public Optional<ColumnAlias> alias() {
        return Optional.ofNullable(alias);
    }

    private ColumnAlias defaultAlias() {
        return ColumnAlias.generate("NEG_");
    }
}
