package org.github.torand.fastersql.expression.comparison;

import org.github.torand.fastersql.Context;
import org.github.torand.fastersql.Field;
import org.github.torand.fastersql.expression.Expression;

import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

public class IsNull implements Expression {
    private final Field operand;

    IsNull(Field operand) {
        this.operand = requireNonNull(operand, "No operand specified");
    }

    @Override
    public String sql(Context context) {
        return operand.sql(context) + " is null";
    }

    @Override
    public String negatedSql(Context context) {
        return operand.sql(context) + " is not null";
    }

    @Override
    public Stream<Object> params(Context context) {
        return Stream.empty();
    }

    @Override
    public Stream<Field> fields() {
        return Stream.empty();
    }
}
