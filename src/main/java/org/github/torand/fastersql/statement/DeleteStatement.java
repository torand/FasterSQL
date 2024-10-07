package org.github.torand.fastersql.statement;

import org.github.torand.fastersql.Context;
import org.github.torand.fastersql.Field;
import org.github.torand.fastersql.Table;
import org.github.torand.fastersql.expression.Expression;
import org.github.torand.fastersql.expression.OptionalExpression;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.joining;
import static org.github.torand.fastersql.Command.DELETE;
import static org.github.torand.fastersql.statement.Helpers.unwrapSuppliers;
import static org.github.torand.fastersql.util.collection.CollectionHelper.*;
import static org.github.torand.fastersql.util.contract.Requires.requireNonEmpty;

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
