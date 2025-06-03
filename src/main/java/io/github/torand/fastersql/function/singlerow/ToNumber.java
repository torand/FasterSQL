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
package io.github.torand.fastersql.function.singlerow;

import io.github.torand.fastersql.alias.ColumnAlias;
import io.github.torand.fastersql.expression.Expression;
import io.github.torand.fastersql.model.Column;
import io.github.torand.fastersql.projection.Projection;
import io.github.torand.fastersql.sql.Context;

import java.util.Optional;
import java.util.stream.Stream;

import static io.github.torand.javacommons.contract.Requires.require;
import static io.github.torand.javacommons.contract.Requires.requireNonBlank;
import static io.github.torand.javacommons.lang.StringHelper.nonBlank;
import static java.util.Objects.requireNonNull;

/**
 * Implements the string to number conversion function.
 */
public class ToNumber implements SingleRowFunction {
    private final Expression expression;
    private final int precision;
    private final int scale;
    private final ColumnAlias alias;

    ToNumber(Expression expression, int precision, int scale, String alias) {
        require(() -> precision >= 1, "precision must be 1 or greater");
        require(() -> scale >= 0, "scale must be 0 or greater");
        require(() -> scale <= precision, "scale must be less than or equal to precision");

        this.expression = requireNonNull(expression, "No expression specified");
        this.alias = nonBlank(alias) ? new ColumnAlias(alias) : defaultAlias();
        this.precision = precision;
        this.scale = scale;
    }

    // Sql

    @Override
    public String sql(Context context) {
        return context.getDialect().formatToNumberFunction(expression.sql(context), precision, scale);
    }

    @Override
    public Stream<Object> params(Context context) {
        return expression.params(context);
    }

    @Override
    public Stream<Column> columnRefs() {
        return expression.columnRefs();
    }

    @Override
    public Stream<ColumnAlias> aliasRefs() {
        return expression.aliasRefs();
    }

    // Projection

    @Override
    public Projection as(String alias) {
        requireNonBlank(alias, "No alias specified");
        return new ToNumber(expression, precision, scale, alias);
    }

    @Override
    public Optional<ColumnAlias> alias() {
        return Optional.ofNullable(alias);
    }

    private ColumnAlias defaultAlias() {
        return ColumnAlias.generate("TO_NUMBER_");
    }
}
