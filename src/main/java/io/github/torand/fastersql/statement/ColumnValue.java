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
package io.github.torand.fastersql.statement;

import io.github.torand.fastersql.Column;
import io.github.torand.fastersql.Context;
import io.github.torand.fastersql.expression.Expression;

import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

/**
 * A column-value pair used in UPDATE and INSERT statements.
 */
class ColumnValue {
    private final Column column;
    private final Expression expression;

    ColumnValue(Column column, Expression expression) {
        this.column = requireNonNull(column, "No column specified");
        this.expression = requireNonNull(expression, "No expression specified");
    }

    Column column() {
        return column;
    }

    Stream<Object> params(Context context) {
        return expression.params(context);
    }

    String valueSql(Context context) {
        return expression.sql(context);
    }
}