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

import io.github.torand.fastersql.Column;
import io.github.torand.fastersql.Context;
import io.github.torand.fastersql.alias.ColumnAlias;
import io.github.torand.fastersql.expression.Expression;
import io.github.torand.fastersql.projection.Projection;

import java.util.Optional;
import java.util.stream.Stream;

import static io.github.torand.fastersql.util.contract.Requires.require;
import static io.github.torand.fastersql.util.contract.Requires.requireNonBlank;
import static io.github.torand.fastersql.util.lang.StringHelper.nonBlank;
import static java.util.Objects.requireNonNull;

/**
 * Implements the substring string function.
 */
public class Substring implements SingleRowFunction {
    private final Expression expression;
    private final int startPos;
    private final int length;
    private final ColumnAlias alias;

    Substring(Expression expression, int startPos, int length, String alias) {
        require(() -> startPos >= 1, "startPos must be 1 or greater");
        require(() -> length >= 1, "length must be 1 or greater");

        this.expression = requireNonNull(expression, "No expression specified");
        this.alias = nonBlank(alias) ? new ColumnAlias(alias) : defaultAlias();
        this.startPos = startPos;
        this.length = length;
    }

    // Sql

    @Override
    public String sql(Context context) {
        return context.getDialect().formatSubstringFunction(expression.sql(context), startPos, length);
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
        return new Substring(expression, length, startPos, alias);
    }

    @Override
    public Optional<ColumnAlias> alias() {
        return Optional.ofNullable(alias);
    }

    private ColumnAlias defaultAlias() {
        return ColumnAlias.generate("SUBSTRING_");
    }
}
