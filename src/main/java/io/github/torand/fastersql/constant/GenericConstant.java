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
 * Represents a scalar value constant of any type.
 * @param <T> the value type.
 */
public class GenericConstant<T> implements Constant {
    private final T value;
    private final ColumnAlias alias;

    GenericConstant(T value, String alias) {
        this.value = requireNonNull(value, "Value is null. Use the NullValue constant instead.");
        this.alias = nonBlank(alias) ? new ColumnAlias(alias) : null;
    }

    // Sql

    @Override
    public String sql(Context context) {
        return "?";
    }

    @Override
    public Stream<Object> params(Context context) {
        return Stream.of(value);
    }

    // Projection

    @Override
    public Projection as(String alias) {
        requireNonBlank(alias, "No alias specified");
        return new GenericConstant<>(value, alias);
    }

    @Override
    public Optional<ColumnAlias> alias() {
        return Optional.ofNullable(alias);
    }

    // Constant

    @Override
    public T value() {
        return value;
    }

    @Override
    public Projection forColumn(Column column) {
        requireNonNull(column, "No column specified");
        return new GenericConstant<>(value, column.alias().map(Alias::name).orElse(null));
    }
}
