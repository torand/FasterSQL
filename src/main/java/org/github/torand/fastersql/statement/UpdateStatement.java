package org.github.torand.fastersql.statement;

import org.github.torand.fastersql.Context;
import org.github.torand.fastersql.Field;
import org.github.torand.fastersql.Table;
import org.github.torand.fastersql.constant.Constant;
import org.github.torand.fastersql.expression.Expression;
import org.github.torand.fastersql.expression.OptionalExpression;
import org.github.torand.fastersql.function.Function;
import org.github.torand.fastersql.util.functional.Optionals;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.joining;
import static org.github.torand.fastersql.Command.UPDATE;
import static org.github.torand.fastersql.util.collection.CollectionHelper.*;
import static org.github.torand.fastersql.util.contract.Requires.requireNonEmpty;

public class UpdateStatement extends PreparableStatement {
    private final Table table;
    private final List<FieldValue> fieldValues;
    private final List<Expression> expressions;

    UpdateStatement(Table table, Collection<FieldValue> fieldValues, Collection<Expression> expressions) {
        this.table = requireNonNull(table, "No table specified");
        this.fieldValues = asList(fieldValues);
        this.expressions = asList(expressions);
    }

    public UpdateStatement set(Field field, Constant constant) {
        requireNonNull(field, "No field specified");
        requireNonNull(constant, "No constant specified");

        List<FieldValue> concatenated = concat(this.fieldValues, new FieldValue(field, constant.value()));
        return new UpdateStatement(table, concatenated, expressions);
    }

    public UpdateStatement set(Field field, Function function) {
        requireNonNull(field, "No field specified");
        requireNonNull(function, "No function specified");

        List<FieldValue> concatenated = concat(this.fieldValues, new FieldValue(field, function));
        return new UpdateStatement(table, concatenated, expressions);
    }

    public UpdateStatement set(Field field, Object value) {
        requireNonNull(field, "No field specified");

        List<FieldValue> concatenated = concat(this.fieldValues, new FieldValue(field, value));
        return new UpdateStatement(table, concatenated, expressions);
    }

    public UpdateStatement set(Field field, Optional<?> maybeValue) {
        requireNonNull(field, "No field specified");
        requireNonNull(maybeValue, "No value specified");

        if (maybeValue.isPresent()) {
            List<FieldValue> concatenated = concat(this.fieldValues, new FieldValue(field, maybeValue.get()));
            return new UpdateStatement(table, concatenated, expressions);
        } else {
            return this;
        }
    }

    public UpdateStatement where(Expression... expressions) {
        requireNonEmpty(expressions, "No expressions specified");

        List<Expression> concatenated = concat(this.expressions, expressions);
        return new UpdateStatement(table, fieldValues, concatenated);
    }

    /**
     * Same as other method of same name, but only adds to the where clause expressions that are present.
     * @param maybeExpressions the expressions that may be present or not
     * @return updated statement, for method chaining
     */
    public final UpdateStatement where(OptionalExpression... maybeExpressions) {
        requireNonEmpty(maybeExpressions, "No expressions specified");

        List<Expression> concatenated = concat(this.expressions, OptionalExpression.unwrap(maybeExpressions));
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
        if (nonEmpty(expressions)) {
            sb.append(" where ");
            sb.append(streamSafely(expressions).map(e -> e.sql(localContext)).collect(joining(" and ")));
        }

        return sb.toString();
    }

    @Override
    List<Object> params(Context context) {
        return Stream.concat(streamSafely(fieldValues).map(FieldValue::param).flatMap(Optionals::stream), streamSafely(expressions).flatMap(e -> e.params(context))).toList();
    }

    private void validate() {
        if (isEmpty(fieldValues)) {
            throw new IllegalStateException("No values to set");
        }
        validateFieldTableRelations(streamSafely(fieldValues).map(FieldValue::field));
        validateFieldTableRelations(streamSafely(expressions).flatMap(Expression::fields));
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
