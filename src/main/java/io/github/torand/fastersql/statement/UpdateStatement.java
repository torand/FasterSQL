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
import io.github.torand.fastersql.constant.Constant;
import io.github.torand.fastersql.function.Function;
import io.github.torand.fastersql.predicate.OptionalPredicate;
import io.github.torand.fastersql.predicate.Predicate;
import io.github.torand.fastersql.util.functional.Optionals;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static io.github.torand.fastersql.Command.UPDATE;
import static io.github.torand.fastersql.util.collection.CollectionHelper.*;
import static io.github.torand.fastersql.util.contract.Requires.requireNonEmpty;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.joining;

public class UpdateStatement extends PreparableStatement {
    private final Table<?> table;
    private final List<FieldValue> fieldValues;
    private final List<Predicate> predicates;

    UpdateStatement(Table<?> table, Collection<FieldValue> fieldValues, Collection<Predicate> predicates) {
        this.table = requireNonNull(table, "No table specified");
        this.fieldValues = asList(fieldValues);
        this.predicates = asList(predicates);
    }

    public UpdateStatement set(Field field, Constant constant) {
        requireNonNull(field, "No field specified");
        requireNonNull(constant, "No constant specified");

        List<FieldValue> concatenated = concat(this.fieldValues, new FieldValue(field, constant.value()));
        return new UpdateStatement(table, concatenated, predicates);
    }

    public UpdateStatement set(Field field, Function function) {
        requireNonNull(field, "No field specified");
        requireNonNull(function, "No function specified");

        List<FieldValue> concatenated = concat(this.fieldValues, new FieldValue(field, function));
        return new UpdateStatement(table, concatenated, predicates);
    }

    public UpdateStatement set(Field field, Object value) {
        requireNonNull(field, "No field specified");

        List<FieldValue> concatenated = concat(this.fieldValues, new FieldValue(field, value));
        return new UpdateStatement(table, concatenated, predicates);
    }

    public UpdateStatement set(Field field, Optional<?> maybeValue) {
        requireNonNull(field, "No field specified");
        requireNonNull(maybeValue, "No value specified");

        if (maybeValue.isPresent()) {
            List<FieldValue> concatenated = concat(this.fieldValues, new FieldValue(field, maybeValue.get()));
            return new UpdateStatement(table, concatenated, predicates);
        } else {
            return this;
        }
    }

    public UpdateStatement where(Predicate... predicates) {
        requireNonEmpty(predicates, "No predicates specified");

        List<Predicate> concatenated = concat(this.predicates, predicates);
        return new UpdateStatement(table, fieldValues, concatenated);
    }

    /**
     * Same as other method of same name, but only adds to the where clause predicates that are present.
     * @param maybePredicates the predicates that may be present or not
     * @return updated statement, for method chaining
     */
    public final UpdateStatement where(OptionalPredicate... maybePredicates) {
        requireNonEmpty(maybePredicates, "No optional predicates specified");

        List<Predicate> concatenated = concat(this.predicates, OptionalPredicate.unwrap(maybePredicates));
        return new UpdateStatement(table, fieldValues, concatenated);
    }

    @Override
    String sql(Context context) {
        final Context localContext = context.withCommand(UPDATE);
        validate();

        StringBuilder sb = new StringBuilder();
        sb.append("update ");
        sb.append(table.sql(context));
        sb.append(" set ");
        sb.append(streamSafely(fieldValues).map(fv -> fv.field().sql(localContext) + " = " + fv.valueSql(localContext)).collect(joining(", ")));
        if (nonEmpty(predicates)) {
            sb.append(" where ");
            sb.append(streamSafely(predicates).map(e -> e.sql(localContext)).collect(joining(" and ")));
        }

        return sb.toString();
    }

    @Override
    List<Object> params(Context context) {
        return Stream.concat(streamSafely(fieldValues).map(FieldValue::param).flatMap(Optionals::stream), streamSafely(predicates).flatMap(e -> e.params(context))).toList();
    }

    private void validate() {
        if (isEmpty(fieldValues)) {
            throw new IllegalStateException("No values to set");
        }
        validateFieldTableRelations(streamSafely(fieldValues).map(FieldValue::field));
        validateFieldTableRelations(streamSafely(predicates).flatMap(Predicate::fieldRefs));
    }

    private void validateFieldTableRelations(Stream<Field> fields) {
        fields
            .filter(f -> !table.name().equalsIgnoreCase(f.table().name()))
            .findFirst()
            .ifPresent(f -> {
                throw new IllegalStateException("Field " + f.name() + " belongs to table " + f.table().name() + ", but is not specified in the UPDATE clause");
            });
    }
}
