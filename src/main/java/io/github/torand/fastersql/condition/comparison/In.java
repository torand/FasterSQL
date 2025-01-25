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
package io.github.torand.fastersql.condition.comparison;

import io.github.torand.fastersql.Context;
import io.github.torand.fastersql.Field;
import io.github.torand.fastersql.condition.Condition;
import io.github.torand.fastersql.condition.LeftOperand;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static io.github.torand.fastersql.Clause.RESTRICTION;
import static io.github.torand.fastersql.statement.Helpers.paramMarkers;
import static io.github.torand.fastersql.util.collection.CollectionHelper.asList;
import static io.github.torand.fastersql.util.contract.Requires.requireNonEmpty;
import static java.util.Objects.requireNonNull;

public class In implements Condition {
    private final LeftOperand left;
    private final List<?> right;

    In(LeftOperand left, Collection<?> right) {
        this.left = requireNonNull(left, "No left operand specified");
        this.right = asList(requireNonEmpty(right, "No right operand specified"));
    }

    // Sql

    @Override
    public String sql(Context context) {
        Context localContext = context.withClause(RESTRICTION);
        return left.sql(localContext) + " in (" + paramMarkers(right.size()) + ")";
    }

    @Override
    public Stream<Object> params(Context context) {
        return right.stream().map(v -> v);
    }

    // Condition

    @Override
    public String negatedSql(Context context) {
        Context localContext = context.withClause(RESTRICTION);
        return left.sql(localContext) + " not in (" + paramMarkers(right.size()) + ")";
    }

    @Override
    public Stream<Field> fieldRefs() {
        return left.fieldRefs();
    }
}
