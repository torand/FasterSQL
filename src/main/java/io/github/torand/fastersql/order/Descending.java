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
package io.github.torand.fastersql.order;

import io.github.torand.fastersql.alias.ColumnAlias;
import io.github.torand.fastersql.dialect.Capability;
import io.github.torand.fastersql.model.Column;
import io.github.torand.fastersql.sql.Context;

import java.util.stream.Stream;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

/**
 * Implements a descending order clause.
 */
public class Descending implements Order {
    private final OrderExpression expression;
    private final Boolean nullsFirst;

    Descending(OrderExpression expression) {
        this(requireNonNull(expression, "No order expression specified"), null);
    }

    private Descending(OrderExpression expression, Boolean nullsFirst) {
        this.expression = expression;
        this.nullsFirst = nullsFirst;
    }

    public Descending nullsFirst() {
        return new Descending(expression, true);
    }

    public Descending nullsLast() {
        return new Descending(expression, false);
    }

    // Sql

    @Override
    public String sql(Context context) {
        if (nonNull(nullsFirst) && !context.getDialect().supports(Capability.NULL_ORDERING)) {
            throw new UnsupportedOperationException("%s does not support 'nulls first' or 'nulls last'".formatted(context.getDialect().getProductName()));
        }

        return expression.sql(context) + " desc"
            + (TRUE.equals(nullsFirst) ? " nulls first" : "")
            + (FALSE.equals(nullsFirst) ? " nulls last" : "");
    }

    @Override
    public Stream<Object> params(Context context) {
        return Stream.empty();
    }

    @Override
    public Stream<Column> columnRefs() {
        return expression.columnRefs();
    }

    @Override
    public Stream<ColumnAlias> aliasRefs() {
        return expression.aliasRefs();
    }
}
