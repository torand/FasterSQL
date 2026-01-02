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
package io.github.torand.fastersql.predicate.comparison;

import io.github.torand.fastersql.alias.ColumnAlias;
import io.github.torand.fastersql.expression.Expression;
import io.github.torand.fastersql.model.Column;
import io.github.torand.fastersql.predicate.LeftOperand;
import io.github.torand.fastersql.predicate.Predicate;
import io.github.torand.fastersql.sql.Context;

import java.util.stream.Stream;

import static io.github.torand.fastersql.sql.Clause.RESTRICTION;
import static io.github.torand.javacommons.stream.StreamHelper.concatStreams;
import static java.util.Objects.requireNonNull;

/**
 * Implements the between predicate.
 */
public class Between implements Predicate {
    private final LeftOperand left;
    private final Expression lowerBound;
    private final Expression upperBound;

    Between(LeftOperand left, Expression lowerBound, Expression upperBound) {
        this.left = requireNonNull(left, "No left operand specified");
        this.lowerBound = requireNonNull(lowerBound, "No lower bound expression specified");
        this.upperBound = requireNonNull(upperBound, "No upper bound expression specified");
    }

    // Sql

    @Override
    public String sql(Context context) {
        Context localContext = context.withClause(RESTRICTION);
        return left.sql(localContext) + " between " + lowerBound.sql(localContext) + " and " + upperBound.sql(localContext);
    }

    @Override
    public Stream<Object> params(Context context) {
        Context localContext = context.withClause(RESTRICTION);
        return concatStreams(left.params(localContext), lowerBound.params(localContext), upperBound.params(localContext));
    }

    @Override
    public Stream<Column> columnRefs() {
        return concatStreams(left.columnRefs(), lowerBound.columnRefs(), upperBound.columnRefs());
    }

    @Override
    public Stream<ColumnAlias> aliasRefs() {
        return concatStreams(left.aliasRefs(), lowerBound.aliasRefs(), upperBound.aliasRefs());
    }

    // Predicate

    public String negatedSql(Context context) {
        Context localContext = context.withClause(RESTRICTION);
        return left.sql(localContext) + " not between " + lowerBound.sql(localContext) + " and " + upperBound.sql(localContext);
    }
}
