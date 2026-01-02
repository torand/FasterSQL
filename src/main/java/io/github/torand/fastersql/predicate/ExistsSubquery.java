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
package io.github.torand.fastersql.predicate;

import io.github.torand.fastersql.sql.Context;
import io.github.torand.fastersql.subquery.Subquery;

import java.util.stream.Stream;

import static io.github.torand.fastersql.sql.Clause.RESTRICTION;
import static java.util.Objects.requireNonNull;

/**
 * Implements the 'existence' (at least one row) predicate.
 */
public class ExistsSubquery implements Predicate {
    private final Subquery query;

    ExistsSubquery(Subquery query) {
        this.query = requireNonNull(query, "No operand (query) specified");
    }

    // Sql

    @Override
    public String sql(Context context) {
        Context localContext = context.withClause(RESTRICTION);
        return "exists " + query.sql(localContext);
    }

    @Override
    public Stream<Object> params(Context context) {
        Context localContext = context.withClause(RESTRICTION);
        return query.params(localContext);
    }

    // Predicate

    @Override
    public String negatedSql(Context context) {
        Context localContext = context.withClause(RESTRICTION);
        return "not exists " + query.sql(localContext);
    }
}
