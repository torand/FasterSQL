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
import io.github.torand.fastersql.sql.Context;
import io.github.torand.fastersql.sql.Sql;

import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

/**
 * Implements the WHEN-THEN clause of a simple CASE expression.
 */
class SimpleWhenThen implements Sql {
    private final Expression when;
    private final Expression then;

    SimpleWhenThen(Expression when, Expression then) {
        this.when = requireNonNull(when, "No when expression specified");
        this.then = requireNonNull(then, "No then expression specified");
    }

    // Sql

    @Override
    public String sql(Context context) {
        return "when %s then %s".formatted(when.sql(context), then.sql(context));
    }

    @Override
    public Stream<Object> params(Context context) {
        return Stream.concat(when.params(context), then.params(context));
    }

    @Override
    public Stream<Column> columnRefs() {
        return Stream.concat(when.columnRefs(), then.columnRefs());
    }

    @Override
    public Stream<ColumnAlias> aliasRefs() {
        return Stream.concat(when.aliasRefs(), then.aliasRefs());
    }
}
