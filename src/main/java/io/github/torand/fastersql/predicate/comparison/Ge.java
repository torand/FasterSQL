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
package io.github.torand.fastersql.predicate.comparison;

import io.github.torand.fastersql.Column;
import io.github.torand.fastersql.Context;
import io.github.torand.fastersql.alias.ColumnAlias;
import io.github.torand.fastersql.expression.Expression;
import io.github.torand.fastersql.predicate.LeftOperand;
import io.github.torand.fastersql.predicate.Predicate;

import java.util.stream.Stream;

import static io.github.torand.fastersql.Clause.RESTRICTION;
import static java.util.Objects.requireNonNull;

/**
 * Implements the greater than or equal predicate.
 */
public class Ge implements Predicate {
    private final LeftOperand left;
    private final Expression right;

    Ge(LeftOperand left, Expression right) {
        this.left = requireNonNull(left, "No left operand specified");
        this.right = requireNonNull(right, "No right operand specified");
    }

    // Sql

    @Override
    public String sql(Context context) {
        Context localContext = context.withClause(RESTRICTION);
        return left.sql(localContext) + " >= " + right.sql(localContext);
    }

    @Override
    public Stream<Object> params(Context context) {
        Context localContext = context.withClause(RESTRICTION);
        return Stream.concat(left.params(localContext), right.params(localContext));
    }

    @Override
    public Stream<Column> columnRefs() {
        return Stream.concat(left.columnRefs(), right.columnRefs());
    }

    @Override
    public Stream<ColumnAlias> aliasRefs() {
        return Stream.concat(left.aliasRefs(), right.aliasRefs());
    }

    // Predicate

    public String negatedSql(Context context) {
        Context localContext = context.withClause(RESTRICTION);
        return left.sql(localContext) + " < " + right.sql(localContext);
    }
}
