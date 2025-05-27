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
package io.github.torand.fastersql.statement;

import io.github.torand.fastersql.expression.Expression;
import io.github.torand.fastersql.model.Column;
import io.github.torand.fastersql.sql.Context;

import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

/**
 * A column-value pair used in UPDATE and INSERT statements.
 */
class ColumnValue {
    private final Column column;
    private final Expression value;

    ColumnValue(Column column, Expression value) {
        this.column = requireNonNull(column, "No column specified");
        this.value = requireNonNull(value, "No value specified");
    }

    /**
     * Gets the column being populated.
     * @return the column being populated.
     */
    Column column() {
        return column;
    }

    /**
     * Gets the statement parameters introduced by the column value expression.
     * @param context the context (incl. dialect).
     * @return the statement parameters.
     */
    Stream<Object> valueParams(Context context) {
        return value.params(context);
    }

    /**
     * Formats column value expression as an SQL fragment.
     * @param context the context (incl. dialect).
     * @return the formatted SQL fragment.
     */
    String valueSql(Context context) {
        return value.sql(context);
    }
}