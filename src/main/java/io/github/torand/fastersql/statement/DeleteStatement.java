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
import io.github.torand.fastersql.expression.OptionalExpression;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.joining;
import static io.github.torand.fastersql.Command.DELETE;
import static io.github.torand.fastersql.statement.Helpers.unwrapSuppliers;
import static io.github.torand.fastersql.util.collection.CollectionHelper.*;
import static io.github.torand.fastersql.util.contract.Requires.requireNonEmpty;

public class DeleteStatement extends PreparableStatement {
    private final Table fromTable;
    private final List<Expression> expressions;

    DeleteStatement(Table table, Collection<Expression> expressions) {
        this.fromTable = requireNonNull(table, "No table specified");
        this.expressions = asList(expressions);
    }

    public DeleteStatement where(Expression... expressions) {
        requireNonEmpty(expressions, "No expressions specified");
        List<Expression> concatenated = concat(this.expressions, expressions);
        return new DeleteStatement(fromTable, concatenated);
    }

    /**
     * Same as other method of same name, but only adds to the where clause expressions that are present.
     * @param maybeExpressions the expressions that may be present or not
     * @return updated statement, for method chaining
     */
    public final DeleteStatement where(OptionalExpression... maybeExpressions) {
        requireNonEmpty(maybeExpressions, "No optional expressions specified");
        List<Expression> concatenated = concat(this.expressions, OptionalExpression.unwrap(maybeExpressions));
        return new DeleteStatement(fromTable, concatenated);
    }

    /**
     * Adds one or more expressions to the where clause, if a condition is true.
     * @param expressionSuppliers the suppliers providing expressions to add
     * @return updated statement, for method chaining
     */
    @SafeVarargs
    public final DeleteStatement whereIf(boolean condition, Supplier<Expression>... expressionSuppliers) {
        requireNonEmpty(expressionSuppliers, "No expression suppliers specified");
        if (condition) {
            List<Expression> concatenated = concat(this.expressions, unwrapSuppliers(expressionSuppliers));
            return new DeleteStatement(fromTable, concatenated);
        }
        return this;
    }

    @Override
    String sql(Context context) {
        final Context localContext = context.withCommand(DELETE);
        validate();

        StringBuilder sb = new StringBuilder();
        sb.append("delete from ");
        sb.append(fromTable.sql(localContext));

        if (nonEmpty(expressions)) {
            sb.append(" where ");
            sb.append(streamSafely(expressions)
                .map(e -> e.sql(localContext))
                .collect(joining(" and ")));
        }

        return sb.toString();
    }

    @Override
    List<Object> params(Context context) {
        return streamSafely(expressions)
            .flatMap(e -> e.params(context))
            .toList();
    }

    private void validate() {
        if (isNull(fromTable)) {
            throw new IllegalStateException("No FROM clause specified");
        }

        // TODO: Verify that deleteTables is subset of fromTables

        validateFieldTableRelations(streamSafely(expressions).flatMap(Expression::fields));
    }

    private void validateFieldTableRelations(Stream<Field> fields) {
        fields
            .filter(f -> !fromTable.name().equalsIgnoreCase(f.table().name()))
            .findFirst()
            .ifPresent(f -> {
                throw new IllegalStateException("Field " + f.name() + " belongs to table " + f.table().name() + ", but table is not specified in the FROM clause");
            });
    }
}
