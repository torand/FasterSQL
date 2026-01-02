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
package io.github.torand.fastersql.statement;

import io.github.torand.fastersql.dialect.AnsiIsoDialect;
import io.github.torand.fastersql.expression.Expression;
import io.github.torand.fastersql.model.Column;
import io.github.torand.fastersql.model.Table;
import io.github.torand.fastersql.sql.Context;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static io.github.torand.fastersql.constant.Constants.$;
import static io.github.torand.fastersql.sql.Command.INSERT;
import static io.github.torand.javacommons.collection.CollectionHelper.*;
import static io.github.torand.javacommons.stream.StreamHelper.streamSafely;
import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.joining;

/**
 * Implements an INSERT statement for single row insertion.
 */
public class InsertStatement implements PreparableStatement {
    private final Table table;
    private final List<ColumnValue> columnValues;

    InsertStatement(Table table, Collection<ColumnValue> columnValues) {
        this.table = requireNonNull(table, "No table specified");
        this.columnValues = asList(columnValues);
    }

    /**
     * Adds an expression value to be inserted into a column.
     * @param column the column.
     * @param value the value.
     * @return the modified statement.
     */
    public InsertStatement value(Column column, Expression value) {
        requireNonNull(column, "No column specified");
        requireNonNull(value, "No expression specified");

        List<ColumnValue> concatenated = concat(columnValues, new ColumnValue(column, value));
        return new InsertStatement(table, concatenated);
    }

    /**
     * Adds a constant value to be inserted into a column.
     * @param column the column.
     * @param value the value.
     * @return the modified statement.
     */
    public InsertStatement value(Column column, Object value) {
        requireNonNull(column, "No column specified");

        List<ColumnValue> concatenated = concat(columnValues, new ColumnValue(column, $(value)));
        return new InsertStatement(table, concatenated);
    }

    /**
     * Adds an optional value to be inserted into a column.
     * The value is inserted only if the optional value is present.
     * @param column the column.
     * @param maybeValue the optional value.
     * @return the modified statement.
     */
    public InsertStatement value(Column column, Optional<?> maybeValue) {
        requireNonNull(column, "No column specified");
        requireNonNull(maybeValue, "No value specified");

        if (maybeValue.isPresent()) {
            List<ColumnValue> concatenated = concat(columnValues, new ColumnValue(column, $(maybeValue.get())));
            return new InsertStatement(table, concatenated);
        } else {
            return this;
        }
    }

    @Override
    public String sql(Context context) {
        final Context localContext = context.withCommand(INSERT);
        validate();

        return "insert into " + table.sql(context) +
            " (" +
            streamSafely(columnValues).map(cv -> cv.column().sql(localContext)).collect(joining(", ")) +
            ") values (" +
            streamSafely(columnValues).map(cv -> cv.valueSql(localContext)).collect(joining(", ")) +
            ")";
    }

    @Override
    public Stream<Object> params(Context context) {
        final Context localContext = context.withCommand(INSERT);
        return streamSafely(columnValues)
            .flatMap(cv -> cv.valueParams(localContext));
    }

    private void validate() {
        if (isNull(table)) {
            throw new IllegalStateException("No table specified");
        }
        if (isEmpty(columnValues)) {
            throw new IllegalStateException("No values specified");
        }
        validateColumnTableRelations(streamSafely(columnValues).map(ColumnValue::column));
    }

    private void validateColumnTableRelations(Stream<Column> columns) {
        columns
            .filter(c -> !table.name().equals(c.table().name()))
            .findFirst()
            .ifPresent(c -> {
                throw new IllegalStateException("Column " + c.name() + " belongs to table " + c.table().name() + ", not the table specified by the INTO clause");
            });
    }

    @Override
    public String toString() {
        return toString(new AnsiIsoDialect());
    }
}
