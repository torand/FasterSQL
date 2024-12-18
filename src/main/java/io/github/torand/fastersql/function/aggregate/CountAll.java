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
package io.github.torand.fastersql.function.aggregate;

import io.github.torand.fastersql.Context;
import io.github.torand.fastersql.Field;
import io.github.torand.fastersql.order.Order;
import io.github.torand.fastersql.projection.Projection;

import java.util.Optional;

import static io.github.torand.fastersql.util.contract.Requires.requireNonBlank;
import static io.github.torand.fastersql.util.lang.StringHelper.nonBlank;

public class CountAll implements AggregateFunction {
    private final String alias;

    CountAll(String alias) {
        this.alias = nonBlank(alias) ? alias : defaultAlias();
    }

    @Override
    public Projection as(String alias) {
        requireNonBlank(alias, "No alias specified");
        return new CountAll(alias);
    }

    @Override
    public Optional<Field> field() {
        return Optional.empty();
    }

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

    @Override
    public String alias() {
        return alias;
    }

    @Override
    public String sql(Context context) {
        return "count(*)";
    }

    private String defaultAlias() {
        return "COUNT_ALL";
    }
}