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
package io.github.torand.fastersql.constant;

import io.github.torand.fastersql.alias.Alias;
import io.github.torand.fastersql.alias.ColumnAlias;
import io.github.torand.fastersql.model.Column;
import io.github.torand.fastersql.projection.Projection;
import io.github.torand.fastersql.sql.Context;

import java.util.Optional;
import java.util.stream.Stream;

import static io.github.torand.javacommons.contract.Requires.requireNonBlank;
import static io.github.torand.javacommons.lang.StringHelper.nonBlank;
import static java.util.Objects.requireNonNull;

/**
 * Represents an inline string constant.
 * The string value is included in the SQL statement directly, not via a statement parameter.
 */
public class InlineStringConstant implements Constant {
    private final String value;
    private final ColumnAlias alias;

    InlineStringConstant(String value, String alias) {
        this.value = requireNonNull(value, "Value is null. Use the NullValue constant instead.");
        this.alias = nonBlank(alias) ? new ColumnAlias(alias) : null;
    }

    // Sql

    @Override
    public String sql(Context context) {
        return "'%s'".formatted(value);
    }

    @Override
    public Stream<Object> params(Context context) {
        return Stream.empty();
    }

    // Projection

    @Override
    public Projection as(String alias) {
        requireNonBlank(alias, "No alias specified");
        return new InlineStringConstant(value, alias);
    }

    @Override
    public Optional<ColumnAlias> alias() {
        return Optional.ofNullable(alias);
    }

    // Constant

    @Override
    public String value() {
        return value;
    }

    @Override
    public Projection forColumn(Column column) {
        requireNonNull(column, "No column specified");
        return new InlineStringConstant(value, column.alias().map(Alias::name).orElse(null));
    }
}
