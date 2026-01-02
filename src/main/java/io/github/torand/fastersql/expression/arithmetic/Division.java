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
 * Implements the division (quotient) expression.
 */
public class Division implements Expression, OrderExpression {
    private final Expression dividend;
    private final Expression divisor;
    private final ColumnAlias alias;

    Division(Expression dividend, Expression divisor, String alias) {
        this.dividend = requireNonNull(dividend, "No left operand (dividend) specified");
        this.divisor = requireNonNull(divisor, "No right operand (divisor) specified");
        this.alias = nonBlank(alias) ? new ColumnAlias(alias) : defaultAlias();
    }

    // Sql

    @Override
    public String sql(Context context) {
        String dividendSql = dividend.sql(context);
        if (dividend instanceof Addition || dividend instanceof Subtraction) {
            dividendSql = "(" + dividendSql + ")";
        }

        String divisorSql = divisor.sql(context);
        if (divisor instanceof Addition || divisor instanceof Subtraction) {
            divisorSql = "(" + divisorSql + ")";
        }

        return dividendSql + " / " + divisorSql;
    }

    @Override
    public Stream<Object> params(Context context) {
        return Stream.concat(dividend.params(context), divisor.params(context));
    }

    @Override
    public Stream<Column> columnRefs() {
        return Stream.concat(dividend.columnRefs(), divisor.columnRefs());
    }

    @Override
    public Stream<ColumnAlias> aliasRefs() {
        return Stream.concat(dividend.aliasRefs(), divisor.aliasRefs());
    }

    // Projection

    @Override
    public Projection as(String alias) {
        requireNonBlank(alias, "No alias specified");
        return new Division(dividend, divisor, alias);
    }

    @Override
    public Optional<ColumnAlias> alias() {
        return Optional.ofNullable(alias);
    }

    private ColumnAlias defaultAlias() {
        return ColumnAlias.generate("DIVIDE_");
    }
}
