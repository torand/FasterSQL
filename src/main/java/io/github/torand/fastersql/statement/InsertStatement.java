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
import io.github.torand.fastersql.expression.Expression;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static io.github.torand.fastersql.Command.INSERT;
import static io.github.torand.fastersql.constant.Constants.$;
import static io.github.torand.fastersql.util.collection.CollectionHelper.asList;
import static io.github.torand.fastersql.util.collection.CollectionHelper.concat;
import static io.github.torand.fastersql.util.collection.CollectionHelper.isEmpty;
import static io.github.torand.fastersql.util.collection.CollectionHelper.streamSafely;
import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.joining;

public class InsertStatement extends PreparableStatement {
    private final Table<?> table;
    private final List<FieldValue> fieldValues;

    InsertStatement(Table<?> table, Collection<FieldValue> fieldValues) {
        this.table = requireNonNull(table, "No table specified");
        this.fieldValues = asList(fieldValues);
    }

    public InsertStatement value(Field field, Expression expression) {
        requireNonNull(field, "No field specified");
        requireNonNull(expression, "No expression specified");

        List<FieldValue> concatenated = concat(fieldValues, new FieldValue(field, expression));
        return new InsertStatement(table, concatenated);
    }

    public InsertStatement value(Field field, Object value) {
        requireNonNull(field, "No field specified");

        List<FieldValue> concatenated = concat(fieldValues, new FieldValue(field, $(value)));
        return new InsertStatement(table, concatenated);
    }

    public InsertStatement value(Field field, Optional<?> maybeValue) {
        requireNonNull(field, "No field specified");
        requireNonNull(maybeValue, "No value specified");

        if (maybeValue.isPresent()) {
            List<FieldValue> concatenated = concat(fieldValues, new FieldValue(field, $(maybeValue.get())));
            return new InsertStatement(table, concatenated);
        } else {
            return this;
        }
    }

    @Override
    String sql(Context context) {
        final Context localContext = context.withCommand(INSERT);
        validate();

        StringBuilder sb = new StringBuilder();
        sb.append("insert into ").append(table.sql(context));
        sb.append(" (");
        sb.append(streamSafely(fieldValues).map(fv -> fv.field().sql(localContext)).collect(joining(", ")));
        sb.append(") values (").append(streamSafely(fieldValues).map(fv -> fv.valueSql(localContext)).collect(joining(", ")));
        sb.append(")");

        return sb.toString();
    }

    @Override
    List<Object> params(Context context) {
        final Context localContext = context.withCommand(INSERT);
        return streamSafely(fieldValues)
            .flatMap(fv -> fv.params(localContext))
            .toList();
    }

    private void validate() {
        if (isNull(table)) {
            throw new IllegalStateException("No table specified");
        }
        if (isEmpty(fieldValues)) {
            throw new IllegalStateException("No values specified");
        }
        validateFieldTableRelations(streamSafely(fieldValues).map(FieldValue::field));
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
