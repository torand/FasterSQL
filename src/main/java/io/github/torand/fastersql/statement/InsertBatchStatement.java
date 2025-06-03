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
import io.github.torand.fastersql.dialect.OracleDialect;
import io.github.torand.fastersql.model.Column;
import io.github.torand.fastersql.model.Table;
import io.github.torand.fastersql.sql.Context;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import static io.github.torand.fastersql.sql.Command.INSERT;
import static io.github.torand.javacommons.collection.CollectionHelper.asList;
import static io.github.torand.javacommons.collection.CollectionHelper.concat;
import static io.github.torand.javacommons.collection.CollectionHelper.isEmpty;
import static io.github.torand.javacommons.collection.CollectionHelper.streamSafely;
import static io.github.torand.javacommons.contract.Requires.requireNonEmpty;
import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.joining;

/**
 * Implements an INSERT statement for batch (multi-row) insertion.
 * @param <T> the batch entity type.
 */
public class InsertBatchStatement<T> implements PreparableStatement {
    private final Table<?> table;
    private final List<ColumnValueExtractor<? super T>> columnValueExtractors;
    private final List<? extends T> entities;

    InsertBatchStatement(Collection<? extends T> entities, Table<?> table, Collection<ColumnValueExtractor<? super T>> columnValueExtractors) {
        this.entities = asList(requireNonEmpty(entities, "No entities specified"));
        this.table = requireNonNull(table, "No table specified");
        this.columnValueExtractors = asList(columnValueExtractors);
    }

    /**
     * Registers a column-value mapper.
     * @param column the column.
     * @param valueExtractor the value extractor.
     * @return the modified statement.
     */
    public InsertBatchStatement<T> value(Column column, Function<? super T, Object> valueExtractor) {
        requireNonNull(column, "No column specified");
        requireNonNull(valueExtractor, "No value extractor specified");

        List<ColumnValueExtractor<? super T>> concatenated = concat(columnValueExtractors, new ColumnValueExtractor<>(column, valueExtractor));
        return new InsertBatchStatement<>(entities, table, concatenated);
    }

    @Override
    public String sql(Context context) {
        final Context localContext = context.withCommand(INSERT);
        validate();

        if (context.getDialect() instanceof OracleDialect) {

            // INSERT ALL
            //   INTO t (col1, col2, col3) VALUES ('val1_1', 'val1_2', 'val1_3')
            //   INTO t (col1, col2, col3) VALUES ('val2_1', 'val2_2', 'val2_3')
            //   INTO t (col1, col2, col3) VALUES ('val3_1', 'val3_2', 'val3_3')
            // SELECT 1 FROM DUAL;

            StringBuilder sb = new StringBuilder();
            sb.append("insert all");
            entities().forEach(e -> {
                sb.append(" into ").append(table.sql(localContext));
                sb.append(" (");
                sb.append(columnValueExtractors().map(cve -> cve.column().sql(localContext)).collect(joining(", ")));
                sb.append(") values (");
                sb.append(columnValueExtractors().map(cve -> cve.valueSql(localContext, e)).collect(joining(", ")));
                sb.append(")");
            });

            sb.append(" select 1 from DUAL");

            return sb.toString();
        } else {

            // INSERT INTO t
            //   (col1, col2, col3)
            // VALUES
            //   ('val1_1', 'val1_2', 'val1_3'),
            //   ('val2_1', 'val2_2', 'val2_3'),
            //   ('val3_1', 'val3_2', 'val3_3');

            StringBuilder sb = new StringBuilder();
            sb.append("insert into ").append(table.sql(context));
            sb.append(" (");
            sb.append(columnValueExtractors().map(cve -> cve.column().sql(localContext)).collect(joining(", ")));
            sb.append(") values ");
            sb.append(entities().map(e -> "("
                + columnValueExtractors().map(cve -> cve.valueSql(localContext, e)).collect(joining(", "))
                + ")")
                .collect(joining(", ")));

            return sb.toString();
        }
    }

    private Stream<? extends T> entities() {
        return streamSafely(entities);
    }

    private Stream<ColumnValueExtractor<? super T>> columnValueExtractors() {
        return streamSafely(columnValueExtractors);
    }

    @Override
    public Stream<Object> params(Context context) {
        return entities()
            .flatMap(e -> columnValueExtractors()
                .map(cve -> cve.valueParam(e))
                .flatMap(Optional::stream));
    }

    private void validate() {
        if (isNull(table)) {
            throw new IllegalStateException("No table specified");
        }
        if (isEmpty(columnValueExtractors)) {
            throw new IllegalStateException("No values specified");
        }
        validateColumnTableRelations(columnValueExtractors().map(ColumnValueExtractor::column));
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
