package org.github.torand.fastersql.expression.comparison;

import org.github.torand.fastersql.Context;
import org.github.torand.fastersql.Field;
import org.github.torand.fastersql.expression.Expression;
import org.github.torand.fastersql.expression.LeftOperand;

import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

public class Eq implements Expression {
    private final LeftOperand operand;
    private final Object value;

    Eq(LeftOperand operand, Object value) {
        if (value instanceof Field) {
            throw new IllegalArgumentException("Use EqField for expressions with field as right operand");
        }
        this.operand = requireNonNull(operand, "No operand specified");
        this.value = requireNonNull(value, "Use IsNull for expressions with 'NULL' as right operand");
    }

    @Override
    public String sql(Context context) {
        return operand.sql(context) + " = ?";
    }

    @Override
    public String negatedSql(Context context) {
        return operand.sql(context) + " != ?";
    }

    @Override
    public Stream<Object> params(Context context) {
        return Stream.of(value);
    }

    @Override
    public Stream<Field> fields() {
        return operand.fields();
    }
}