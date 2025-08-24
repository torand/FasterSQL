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
package io.github.torand.fastersql.function.aggregate;

import io.github.torand.fastersql.alias.ColumnAlias;
import io.github.torand.fastersql.order.Order;
import io.github.torand.fastersql.projection.Projection;
import io.github.torand.fastersql.sql.Context;

import java.util.Optional;
import java.util.stream.Stream;

import static io.github.torand.javacommons.contract.Requires.requireNonBlank;
import static io.github.torand.javacommons.lang.StringHelper.nonBlank;

/**
 * Implements the count all aggregate function.
 */
public class CountAll implements AggregateFunction {
    private final ColumnAlias alias;

    CountAll(String alias) {
        this.alias = nonBlank(alias) ? new ColumnAlias(alias) : defaultAlias();
    }

    // Sql

    @Override
    public String sql(Context context) {
        return "count(*)";
    }

    @Override
    public Stream<Object> params(Context context) {
        return Stream.empty();
    }

    // Projection

    @Override
    public Projection as(String alias) {
        requireNonBlank(alias, "No alias specified");
        return new CountAll(alias);
    }

    @Override
    public Optional<ColumnAlias> alias() {
        return Optional.ofNullable(alias);
    }

    // OrderExpression

    @Override
    public Order ascIf(boolean condition) {
        throw new UnsupportedOperationException("The 'count(*)' function can't be used in an 'order by' clause");
    }

    @Override
    public Order asc() {
        throw new UnsupportedOperationException("The 'count(*)' function can't be used in an 'order by' clause");
    }

    @Override
    public Order desc() {
        throw new UnsupportedOperationException("The 'count(*)' function can't be used in an 'order by' clause");
    }

    private ColumnAlias defaultAlias() {
        return new ColumnAlias("COUNT_ALL");
    }
}
