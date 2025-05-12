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
import io.github.torand.fastersql.Sql;
import io.github.torand.fastersql.alias.ColumnAlias;
import io.github.torand.fastersql.expression.Expression;
import io.github.torand.fastersql.projection.Projection;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static io.github.torand.fastersql.dialect.Capability.CONCAT_OPERATOR;
import static io.github.torand.fastersql.util.collection.CollectionHelper.streamSafely;
import static io.github.torand.fastersql.util.contract.Requires.requireNonBlank;
import static io.github.torand.fastersql.util.contract.Requires.requireNonEmpty;
import static io.github.torand.fastersql.util.lang.StringHelper.nonBlank;
import static java.util.stream.Collectors.joining;

public class Concat implements SingleRowFunction {
    private final List<Expression> expressions;
    private final ColumnAlias alias;

    Concat(List<Expression> expressions, String alias) {
        this.expressions = new ArrayList<>(requireNonEmpty(expressions, "No expression specified"));
        this.alias = nonBlank(alias) ? new ColumnAlias(alias) : defaultAlias();
    }

    // Sql

    @Override
    public String sql(Context context) {
        if (context.getDialect().supports(CONCAT_OPERATOR)) {
            String operator = context.getDialect().getConcatOperator().orElseThrow();
            return streamSafely(expressions).map(e -> e.sql(context)).collect(joining(" " + operator + " "));
        }

        List<String> expressionsAsSql = streamSafely(expressions).map(e -> e.sql(context)).toList();
        return context.getDialect().formatConcatFunction(expressionsAsSql);
    }

    @Override
    public Stream<Object> params(Context context) {
        return streamSafely(expressions).flatMap(e -> e.params(context));
    }

    @Override
    public Stream<Column> columnRefs() {
        return streamSafely(expressions).flatMap(Sql::columnRefs);
    }

    @Override
    public Stream<ColumnAlias> aliasRefs() {
        return streamSafely(expressions).flatMap(Sql::aliasRefs);
    }

    // Projection

    @Override
    public Projection as(String alias) {
        requireNonBlank(alias, "No alias specified");
        return new Concat(expressions, alias);
    }

    @Override
    public Optional<ColumnAlias> alias() {
        return Optional.ofNullable(alias);
    }

    private ColumnAlias defaultAlias() {
        return ColumnAlias.generate("CONCAT_");
    }
}
