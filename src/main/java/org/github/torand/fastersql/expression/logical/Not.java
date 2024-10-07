package org.github.torand.fastersql.expression.logical;

import org.github.torand.fastersql.Context;
import org.github.torand.fastersql.Field;
import org.github.torand.fastersql.expression.Expression;

import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

public class Not implements Expression {
    private final Expression operand;

    Not(Expression operand) {
        this.operand = requireNonNull(operand, "No operand specified");
    }

    @Override
    public String sql(Context context) {
        return operand.negatedSql(context);
    }

    @Override
    public String negatedSql(Context context) {
        return operand.sql(context);
    }

    @Override
    public Stream<Object> params(Context context) {
        return operand.params(context);
    }

    @Override
    public Stream<Field> fields() {
        return operand.fields();
    }
}
