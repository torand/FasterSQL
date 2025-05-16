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
package io.github.torand.fastersql.predicate;

import io.github.torand.fastersql.Column;
import io.github.torand.fastersql.Context;
import io.github.torand.fastersql.alias.ColumnAlias;

import java.util.stream.Stream;

import static io.github.torand.fastersql.Clause.RESTRICTION;
import static java.util.Objects.requireNonNull;

/**
 * Implements the pattern matching predicate.
 */
public class Like implements Predicate {
    private final LeftOperand left;
    private final String pattern;

    Like(LeftOperand left, String pattern) {
        this.left = requireNonNull(left, "No left operand specified");

        requireNonNull(pattern, "No right operand (pattern) specified");
        this.pattern = pattern.contains("%") ? pattern : "%" + pattern + "%";
    }

    // Sql

    @Override
    public String sql(Context context) {
        Context localContext = context.withClause(RESTRICTION);
        return left.sql(localContext) + " like ?";
    }

    @Override
    public Stream<Object> params(Context context) {
        return Stream.of(pattern);
    }

    @Override
    public Stream<Column> columnRefs() {
        return left.columnRefs();
    }

    @Override
    public Stream<ColumnAlias> aliasRefs() {
        return left.aliasRefs();
    }

    // Predicate

    @Override
    public String negatedSql(Context context) {
        Context localContext = context.withClause(RESTRICTION);
        return left.sql(localContext) + " not like ?";
    }
}
