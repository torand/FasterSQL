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
import io.github.torand.fastersql.projection.Projection;

import java.util.Optional;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;
import static io.github.torand.fastersql.util.contract.Requires.requireNonBlank;

public class Count implements AggregateFunction {
    private final Field field;
    private final String alias;

    Count(Field field, String alias) {
        this.field = requireNonNull(field, "No field specified");
        this.alias = nonNull(alias) ? alias : defaultAlias(field);
    }

    @Override
    public Projection as(String alias) {
        requireNonBlank(alias, "No alias specified");
        return new Count(field, alias);
    }

    @Override
    public Optional<Field> field() {
        return Optional.of(field);
    }

    @Override
    public String alias() {
        return alias;
    }

    @Override
    public String sql(Context context) {
        return "count(" + field.sql(context) + ")";
    }

    private String defaultAlias(Field field) {
        return "COUNT_" + field.alias();
    }
}
