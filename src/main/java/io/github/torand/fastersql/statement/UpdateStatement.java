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

import io.github.torand.fastersql.dialect.AnsiIsoDialect;
import io.github.torand.fastersql.expression.Expression;
import io.github.torand.fastersql.model.Column;
import io.github.torand.fastersql.model.Table;
import io.github.torand.fastersql.predicate.OptionalPredicate;
import io.github.torand.fastersql.predicate.Predicate;
import io.github.torand.fastersql.sql.Context;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static io.github.torand.fastersql.constant.Constants.$;
import static io.github.torand.fastersql.sql.Command.UPDATE;
import static io.github.torand.javacommons.collection.CollectionHelper.asList;
import static io.github.torand.javacommons.collection.CollectionHelper.concat;
import static io.github.torand.javacommons.collection.CollectionHelper.isEmpty;
import static io.github.torand.javacommons.collection.CollectionHelper.nonEmpty;
import static io.github.torand.javacommons.collection.CollectionHelper.streamSafely;
import static io.github.torand.javacommons.contract.Requires.requireNonEmpty;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.joining;

/**
 * Implements an UPDATE statement.
 */
public class UpdateStatement implements PreparableStatement {
    private final Table<?> table;
    private final List<ColumnValue> columnValues;
    private final List<Predicate> predicates;

    UpdateStatement(Table<?> table, Collection<ColumnValue> columnValues, Collection<Predicate> predicates) {
        this.table = requireNonNull(table, "No table specified");
        this.columnValues = asList(columnValues);
        this.predicates = asList(predicates);
    }

    /**
     * Adds an expression value to be updated for a column.
     * @param column the column.
     * @param value the value.
     * @return the modified statement.
     */
    public UpdateStatement set(Column column, Expression value) {
        requireNonNull(column, "No column specified");
        requireNonNull(value, "No expression specified");

        List<ColumnValue> concatenated = concat(this.columnValues, new ColumnValue(column, value));
        return new UpdateStatement(table, concatenated, predicates);
    }

    /**
     * Adds a constant value to be updated for a column.
     * @param column the column.
     * @param value the value.
     * @return the modified statement.
     */
    public UpdateStatement set(Column column, Object value) {
        requireNonNull(column, "No column specified");

        List<ColumnValue> concatenated = concat(this.columnValues, new ColumnValue(column, $(value)));
        return new UpdateStatement(table, concatenated, predicates);
    }

    /**
     * Adds an optional value to be updated for a column.
     * The column is updated only if the optional value is present.
     * @param column the column.
     * @param maybeValue the optional value.
     * @return the modified statement.
     */
    public UpdateStatement set(Column column, Optional<?> maybeValue) {
        requireNonNull(column, "No column specified");
        requireNonNull(maybeValue, "No value specified");

        if (maybeValue.isPresent()) {
            List<ColumnValue> concatenated = concat(this.columnValues, new ColumnValue(column, $(maybeValue.get())));
            return new UpdateStatement(table, concatenated, predicates);
        } else {
            return this;
        }
    }

    /**
     * Adds one or more predicates to the WHERE clause.
     * @param predicates the predicates.
     * @return the modified statement.
     */
    public UpdateStatement where(Predicate... predicates) {
        requireNonEmpty(predicates, "No predicates specified");

        List<Predicate> concatenated = concat(this.predicates, predicates);
        return new UpdateStatement(table, columnValues, concatenated);
    }

    /**
     * Adds optional predicates to the WHERE clause if the wrapped predicates are present.
     * @param maybePredicates the optional predicates.
     * @return the modified statement.
     */
    public final UpdateStatement where(OptionalPredicate... maybePredicates) {
        requireNonEmpty(maybePredicates, "No optional predicates specified");

        List<Predicate> concatenated = concat(this.predicates, OptionalPredicate.unwrap(maybePredicates));
        return new UpdateStatement(table, columnValues, concatenated);
    }

    @Override
    public String sql(Context context) {
        final Context localContext = context.withCommand(UPDATE);
        validate();

        StringBuilder sb = new StringBuilder();
        sb.append("update ");
        sb.append(table.sql(context));
        sb.append(" set ");
        sb.append(streamSafely(columnValues).map(cv -> cv.column().sql(localContext) + " = " + cv.valueSql(localContext)).collect(joining(", ")));
        if (nonEmpty(predicates)) {
            sb.append(" where ");
            sb.append(streamSafely(predicates).map(p -> p.sql(localContext)).collect(joining(" and ")));
        }

        return sb.toString();
    }

    @Override
    public Stream<Object> params(Context context) {
        final Context localContext = context.withCommand(UPDATE);
        return Stream.concat(
                streamSafely(columnValues).flatMap(cv -> cv.valueParams(localContext)),
                streamSafely(predicates).flatMap(p -> p.params(localContext)));
    }

    private void validate() {
        if (isEmpty(columnValues)) {
            throw new IllegalStateException("No values to set");
        }
        validateColumnTableRelations(streamSafely(columnValues).map(ColumnValue::column));
        validateColumnTableRelations(streamSafely(predicates).flatMap(Predicate::columnRefs));
    }

    private void validateColumnTableRelations(Stream<Column> columns) {
        columns
            .filter(c -> !table.name().equalsIgnoreCase(c.table().name()))
            .findFirst()
            .ifPresent(c -> {
                throw new IllegalStateException("Column " + c.name() + " belongs to table " + c.table().name() + ", but is not specified in the UPDATE clause");
            });
    }

    @Override
    public String toString() {
        return toString(new AnsiIsoDialect());
    }
}
