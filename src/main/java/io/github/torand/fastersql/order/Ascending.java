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
package io.github.torand.fastersql.order;

import io.github.torand.fastersql.Context;
import io.github.torand.fastersql.Field;
import io.github.torand.fastersql.projection.Projection;

import java.util.stream.Stream;

import static io.github.torand.fastersql.util.contract.Requires.requireNonBlank;
import static java.util.Objects.requireNonNull;

public class Ascending implements Order {
    private final Projection projection;
    private final String alias;

    Ascending(Projection projection) {
        this.projection = requireNonNull(projection, "No projection specified");
        this.alias = projection.alias();
    }

    Ascending(String alias) {
        this.projection = null;
        this.alias = requireNonBlank(alias, "No alias specified");
    }

    // Sql

    @Override
    public String sql(Context context) {
        return alias + " asc";
    }

    @Override
    public Stream<Object> params(Context context) {
        return Stream.empty();
    }

    // Order

    @Override
    public Stream<Field> fieldRefs() {
        return projection instanceof Field ? Stream.of((Field)projection) : Stream.empty();
    }
}
