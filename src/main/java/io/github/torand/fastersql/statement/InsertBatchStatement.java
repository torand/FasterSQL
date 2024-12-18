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

import io.github.torand.fastersql.Context;
import io.github.torand.fastersql.Field;
import io.github.torand.fastersql.Table;
import io.github.torand.fastersql.dialect.OracleDialect;
import io.github.torand.fastersql.util.functional.Optionals;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.joining;
import static io.github.torand.fastersql.Command.INSERT;
import static io.github.torand.fastersql.util.collection.CollectionHelper.*;
import static io.github.torand.fastersql.util.contract.Requires.requireNonEmpty;

public class InsertBatchStatement<T> extends PreparableStatement {
    private final Table table;
    private final List<FieldValueExtractor<? super T>> fieldValueExtractors;
    private final List<? extends T> entities;

    InsertBatchStatement(Collection<? extends T> entities, Table table, Collection<FieldValueExtractor<? super T>> fieldValueExtractors) {
        this.entities = asList(requireNonEmpty(entities, "No entities specified"));
        this.table = requireNonNull(table, "No table specified");
        this.fieldValueExtractors = asList(fieldValueExtractors);
    }

    public InsertBatchStatement<T> value(Field field, Function<? super T, Object> valueExtractor) {
        requireNonNull(field, "No field specified");
        requireNonNull(valueExtractor, "No valueExtractor specified");

        List<FieldValueExtractor<? super T>> concatenated = concat(fieldValueExtractors, new FieldValueExtractor<>(field, valueExtractor));
        return new InsertBatchStatement<>(entities, table, concatenated);
    }

    @Override
    String sql(Context context) {
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
                sb.append(fieldValueExtractors().map(fve -> fve.field().sql(localContext)).collect(joining(", ")));
                sb.append(") values (");
                sb.append(fieldValueExtractors().map(fve -> fve.valueSql(localContext, e)).collect(joining(", ")));
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
            sb.append(fieldValueExtractors().map(fve -> fve.field().sql(localContext)).collect(joining(", ")));
            sb.append(") values ");
            sb.append(entities().map(e -> "("
                + fieldValueExtractors().map(fve -> fve.valueSql(localContext, e)).collect(joining(", "))
                + ")")
                .collect(joining(", ")));

            return sb.toString();
        }
    }

    private Stream<? extends T> entities() {
        return streamSafely(entities);
    }

    private Stream<FieldValueExtractor<? super T>> fieldValueExtractors() {
        return streamSafely(fieldValueExtractors);
    }

    @Override
    List<Object> params(Context context) {
        return entities()
            .flatMap(e -> fieldValueExtractors()
                .map(fve -> fve.param(e))
                .flatMap(Optionals::stream))
            .toList();
    }

    private void validate() {
        if (isNull(table)) {
            throw new IllegalStateException("No table specified");
        }
        if (isEmpty(fieldValueExtractors)) {
            throw new IllegalStateException("No values specified");
        }
        validateFieldTableRelations(fieldValueExtractors().map(FieldValueExtractor::field));
    }

    private void validateFieldTableRelations(Stream<Field> fields) {
        fields
            .filter(f -> !table.name().equals(f.table().name()))
            .findFirst()
            .ifPresent(f -> {
                throw new IllegalStateException("Field " + f.name() + " belongs to table " + f.table().name() + ", not the table specified by the INTO clause");
            });
    }
}
