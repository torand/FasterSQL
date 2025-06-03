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
 * Implements the addition (sum) expression.
 */
public class Addition implements Expression, OrderExpression {
    private final Expression firstTerm;
    private final Expression secondTerm;
    private final ColumnAlias alias;

    Addition(Expression firstTerm, Expression secondTerm, String alias) {
        this.firstTerm = requireNonNull(firstTerm, "No left operand (first term) specified");
        this.secondTerm = requireNonNull(secondTerm, "No right operand (second term) specified");
        this.alias = nonBlank(alias) ? new ColumnAlias(alias) : defaultAlias();
    }

    // Sql

    @Override
    public String sql(Context context) {
        return firstTerm.sql(context) + " + " + secondTerm.sql(context);
    }

    @Override
    public Stream<Object> params(Context context) {
        return Stream.concat(firstTerm.params(context), secondTerm.params(context));
    }

    @Override
    public Stream<Column> columnRefs() {
        return Stream.concat(firstTerm.columnRefs(), secondTerm.columnRefs());
    }

    @Override
    public Stream<ColumnAlias> aliasRefs() {
        return Stream.concat(firstTerm.aliasRefs(), secondTerm.aliasRefs());
    }

    // Projection

    @Override
    public Projection as(String alias) {
        requireNonBlank(alias, "No alias specified");
        return new Addition(firstTerm, secondTerm, alias);
    }

    @Override
    public Optional<ColumnAlias> alias() {
        return Optional.ofNullable(alias);
    }

    private ColumnAlias defaultAlias() {
        return ColumnAlias.generate("PLUS_");
    }
}
