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
 * Implements the multiplication (product) expression.
 */
public class Multiplication implements Expression, OrderExpression {
    private final Expression firstFactor;
    private final Expression secondFactor;
    private final ColumnAlias alias;

    Multiplication(Expression firstFactor, Expression secondFactor, String alias) {
        this.firstFactor = requireNonNull(firstFactor, "No left operand (first factor) specified");
        this.secondFactor = requireNonNull(secondFactor, "No right operand (second factor) specified");
        this.alias = nonBlank(alias) ? new ColumnAlias(alias) : defaultAlias();
    }

    // Sql

    @Override
    public String sql(Context context) {
        String firstFactorSql = firstFactor.sql(context);
        if (firstFactor instanceof Addition || firstFactor instanceof Subtraction) {
            firstFactorSql = "(" + firstFactorSql + ")";
        }

        String secondFactorSql = secondFactor.sql(context);
        if (secondFactor instanceof Addition || secondFactor instanceof Subtraction) {
            secondFactorSql = "(" + secondFactorSql + ")";
        }

        return firstFactorSql + " * " + secondFactorSql;
    }

    @Override
    public Stream<Object> params(Context context) {
        return Stream.concat(firstFactor.params(context), secondFactor.params(context));
    }

    @Override
    public Stream<Column> columnRefs() {
        return Stream.concat(firstFactor.columnRefs(), secondFactor.columnRefs());
    }

    @Override
    public Stream<ColumnAlias> aliasRefs() {
        return Stream.concat(firstFactor.aliasRefs(), secondFactor.aliasRefs());
    }

    // Projection

    @Override
    public Projection as(String alias) {
        requireNonBlank(alias, "No alias specified");
        return new Multiplication(firstFactor, secondFactor, alias);
    }

    @Override
    public Optional<ColumnAlias> alias() {
        return Optional.ofNullable(alias);
    }

    private ColumnAlias defaultAlias() {
        return ColumnAlias.generate("TIMES_");
    }
}
