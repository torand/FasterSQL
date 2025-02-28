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
package io.github.torand.fastersql.function.singlerow;

import io.github.torand.fastersql.Column;
import io.github.torand.fastersql.Context;
import io.github.torand.fastersql.alias.ColumnAlias;
import io.github.torand.fastersql.expression.Expression;
import io.github.torand.fastersql.projection.Projection;

import java.util.Optional;
import java.util.stream.Stream;

import static io.github.torand.fastersql.util.contract.Requires.requireNonBlank;
import static io.github.torand.fastersql.util.lang.StringHelper.nonBlank;
import static java.util.Objects.requireNonNull;

public class Lower implements SingleRowFunction {
    private final Expression expression;
    private final ColumnAlias alias;

    Lower(Expression expression, String alias) {
        this.expression = requireNonNull(expression, "No expression specified");
        this.alias = nonBlank(alias) ? new ColumnAlias(alias) : defaultAlias();
    }

    // Sql

    @Override
    public String sql(Context context) {
        return "lower(" + expression.sql(context) + ")";
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
        return new Lower(expression, alias);
    }

    @Override
    public Optional<ColumnAlias> alias() {
        return Optional.ofNullable(alias);
    }

    private ColumnAlias defaultAlias() {
        return ColumnAlias.generate("LOWER_");
    }
}
