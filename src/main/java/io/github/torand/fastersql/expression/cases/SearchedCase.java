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
package io.github.torand.fastersql.expression.cases;

import io.github.torand.fastersql.alias.ColumnAlias;
import io.github.torand.fastersql.expression.Expression;
import io.github.torand.fastersql.model.Column;
import io.github.torand.fastersql.projection.Projection;
import io.github.torand.fastersql.sql.Context;
import io.github.torand.fastersql.sql.Sql;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static io.github.torand.javacommons.contract.Requires.requireNonBlank;
import static io.github.torand.javacommons.contract.Requires.requireNonEmpty;
import static io.github.torand.javacommons.lang.StringHelper.nonBlank;
import static io.github.torand.javacommons.stream.StreamHelper.streamSafely;
import static java.util.Objects.nonNull;

/**
 * Implements a searched CASE expression.
 */
public class SearchedCase implements Expression {
    private final List<SearchedWhenThen> whenThenExpressions;
    private final Expression elseExpression;
    private final ColumnAlias alias;

    SearchedCase(List<SearchedWhenThen> whenThenExpressions, Expression elseExpression, String alias) {
        this.whenThenExpressions = requireNonEmpty(whenThenExpressions, "No when-then expressions specified");
        this.elseExpression = elseExpression;
        this.alias = nonBlank(alias) ? new ColumnAlias(alias) : defaultAlias();
    }

    // Sql

    @Override
    public String sql(Context context) {
        StringBuilder sql = new StringBuilder();
        sql.append("case ");
        streamSafely(whenThenExpressions).forEach(wt -> sql.append(wt.sql(context)).append(" "));
        if (nonNull(elseExpression)) {
            sql.append("else ").append(elseExpression.sql(context)).append(" ");
        }
        sql.append("end");
        return sql.toString();
    }

    @Override
    public Stream<Object> params(Context context) {
        return Stream.concat(
            streamSafely(whenThenExpressions).flatMap(wh -> wh.params(context)),
            Stream.ofNullable(elseExpression).flatMap(e -> e.params(context))
        );
    }

    @Override
    public Stream<Column> columnRefs() {
        return Stream.concat(
            streamSafely(whenThenExpressions).flatMap(SearchedWhenThen::columnRefs),
            Stream.ofNullable(elseExpression).flatMap(Sql::columnRefs)
        );
    }

    @Override
    public Stream<ColumnAlias> aliasRefs() {
        return Stream.concat(
            streamSafely(whenThenExpressions).flatMap(SearchedWhenThen::aliasRefs),
            Stream.ofNullable(elseExpression).flatMap(Sql::aliasRefs)
        );
    }

    // Projection

    @Override
    public Projection as(String alias) {
        requireNonBlank(alias, "No alias specified");
        return new SearchedCase(whenThenExpressions, elseExpression, alias);
    }

    @Override
    public Optional<ColumnAlias> alias() {
        return Optional.ofNullable(alias);
    }

    private ColumnAlias defaultAlias() {
        return ColumnAlias.generate("CASE_");
    }
}
