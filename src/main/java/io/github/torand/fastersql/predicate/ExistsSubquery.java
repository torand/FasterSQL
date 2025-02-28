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
package io.github.torand.fastersql.predicate;

import io.github.torand.fastersql.Column;
import io.github.torand.fastersql.Context;
import io.github.torand.fastersql.alias.ColumnAlias;
import io.github.torand.fastersql.subquery.Subquery;

import java.util.stream.Stream;

import static io.github.torand.fastersql.Clause.RESTRICTION;
import static java.util.Objects.requireNonNull;

public class ExistsSubquery implements Predicate {
    private final Subquery operand;

    ExistsSubquery(Subquery operand) {
        this.operand = requireNonNull(operand, "No operand specified");
    }

    // Sql

    @Override
    public String sql(Context context) {
        Context localContext = context.withClause(RESTRICTION);
        return "exists " + operand.sql(localContext);
    }

    @Override
    public Stream<Object> params(Context context) {
        Context localContext = context.withClause(RESTRICTION);
        return operand.params(localContext);
    }

    @Override
    public Stream<Column> columnRefs() {
        return Stream.empty();
    }

    @Override
    public Stream<ColumnAlias> aliasRefs() {
        return Stream.empty();
    }

    // Predicate

    @Override
    public String negatedSql(Context context) {
        Context localContext = context.withClause(RESTRICTION);
        return "not exists " + operand.sql(localContext);
    }
}
