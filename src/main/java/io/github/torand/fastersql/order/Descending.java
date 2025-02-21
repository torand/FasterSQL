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
import io.github.torand.fastersql.dialect.Capability;
import io.github.torand.fastersql.projection.Projection;

import java.util.stream.Stream;

import static io.github.torand.fastersql.util.contract.Requires.requireNonBlank;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

public class Descending implements Order {
    private final Projection projection;
    private final String alias;
    private final Integer index;
    private final Boolean nullsFirst;

    Descending(Projection projection) {
        this(requireNonNull(projection, "No projection specified"), projection.alias(), null, null);
    }

    Descending(String alias) {
        this(null, requireNonBlank(alias, "No alias specified"), null, null);
    }

    Descending(Integer index) {
        this(null, null, requireNonNull(index, "No index specified"), null);
    }

    private Descending(Projection projection, String alias, Integer index, Boolean nullsFirst) {
        this.projection = projection;
        this.alias = alias;
        this.index = index;
        this.nullsFirst = nullsFirst;
    }

    public Descending nullsFirst() {
        return new Descending(projection, alias, index, true);
    }

    public Descending nullsLast() {
        return new Descending(projection, alias, index, false);
    }

    // Sql

    @Override
    public String sql(Context context) {
        if (nonNull(nullsFirst) && !context.getDialect().supports(Capability.NULL_ORDERING)) {
            throw new UnsupportedOperationException("%s does not support 'nulls first' or 'nulls last'".formatted(context.getDialect().getProductName()));
        }

        return (nonNull(index) ? index : alias)
            + " desc"
            + (TRUE.equals(nullsFirst) ? " nulls first" : "")
            + (FALSE.equals(nullsFirst) ? " nulls last" : "");
    }

    @Override
    public Stream<Object> params(Context context) {
        return Stream.empty();
    }

    // Order

    @Override
    public String alias() {
        return alias;
    }

    @Override
    public Stream<Field> fieldRefs() {
        return projection instanceof Field ? Stream.of((Field)projection) : Stream.empty();
    }
}
