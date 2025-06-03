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
import io.github.torand.fastersql.model.Column;
import io.github.torand.fastersql.model.Table;
import io.github.torand.fastersql.predicate.OptionalPredicate;
import io.github.torand.fastersql.predicate.Predicate;
import io.github.torand.fastersql.sql.Context;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static io.github.torand.fastersql.sql.Command.DELETE;
import static io.github.torand.fastersql.statement.Helpers.unwrapSuppliers;
import static io.github.torand.javacommons.collection.CollectionHelper.asList;
import static io.github.torand.javacommons.collection.CollectionHelper.concat;
import static io.github.torand.javacommons.collection.CollectionHelper.nonEmpty;
import static io.github.torand.javacommons.collection.CollectionHelper.streamSafely;
import static io.github.torand.javacommons.contract.Requires.requireNonEmpty;
import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.joining;

/**
 * Implements a DELETE statement.
 */
public class DeleteStatement implements PreparableStatement {
    private final Table<?> fromTable;
    private final List<Predicate> predicates;

    DeleteStatement(Table<?> table, Collection<Predicate> predicates) {
        this.fromTable = requireNonNull(table, "No table specified");
        this.predicates = asList(predicates);
    }

    /**
     * Adds one or more predicates to the WHERE clause.
     * @param predicates the predicates.
     * @return the modified statement.
     */
    public DeleteStatement where(Predicate... predicates) {
        requireNonEmpty(predicates, "No predicates specified");
        List<Predicate> concatenated = concat(this.predicates, predicates);
        return new DeleteStatement(fromTable, concatenated);
    }

    /**
     * Adds optional predicates to the WHERE clause if the wrapped predicates are present.
     * @param maybePredicates the optional predicates.
     * @return the modified statement.
     */
    public final DeleteStatement where(OptionalPredicate... maybePredicates) {
        requireNonEmpty(maybePredicates, "No optional predicates specified");
        List<Predicate> concatenated = concat(this.predicates, OptionalPredicate.unwrap(maybePredicates));
        return new DeleteStatement(fromTable, concatenated);
    }

    /**
     * Adds supplied predicates to the WHERE clause, if the condition is true.
     * @param condition the condition.
     * @param predicateSuppliers the suppliers providing predicates
     * @return the modified statement.
     */
    @SafeVarargs
    public final DeleteStatement whereIf(boolean condition, Supplier<Predicate>... predicateSuppliers) {
        requireNonEmpty(predicateSuppliers, "No predicate suppliers specified");
        if (condition) {
            List<Predicate> concatenated = concat(this.predicates, unwrapSuppliers(predicateSuppliers));
            return new DeleteStatement(fromTable, concatenated);
        }
        return this;
    }

    @Override
    public String sql(Context context) {
        final Context localContext = context.withCommand(DELETE);
        validate();

        StringBuilder sb = new StringBuilder();
        sb.append("delete from ");
        sb.append(fromTable.sql(localContext));

        if (nonEmpty(predicates)) {
            sb.append(" where ");
            sb.append(streamSafely(predicates)
                .map(p -> p.sql(localContext))
                .collect(joining(" and ")));
        }

        return sb.toString();
    }

    @Override
    public Stream<Object> params(Context context) {
        return streamSafely(predicates)
            .flatMap(p -> p.params(context));
    }

    private void validate() {
        if (isNull(fromTable)) {
            throw new IllegalStateException("No FROM clause specified");
        }

        // TODO: Verify that deleteTables is subset of fromTables

        validateColumnTableRelations(streamSafely(predicates).flatMap(Predicate::columnRefs));
    }

    private void validateColumnTableRelations(Stream<Column> columns) {
        columns
            .filter(c -> !fromTable.name().equalsIgnoreCase(c.table().name()))
            .findFirst()
            .ifPresent(c -> {
                throw new IllegalStateException("Column " + c.name() + " belongs to table " + c.table().name() + ", but table is not specified in the FROM clause");
            });
    }

    @Override
    public String toString() {
        return toString(new AnsiIsoDialect());
    }
}
