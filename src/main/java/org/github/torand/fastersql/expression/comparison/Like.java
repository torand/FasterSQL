package org.github.torand.fastersql.expression.comparison;

import org.github.torand.fastersql.Context;
import org.github.torand.fastersql.Field;
import org.github.torand.fastersql.expression.Expression;
import org.github.torand.fastersql.expression.LeftOperand;

import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

public class Like implements Expression {
    private final LeftOperand operand;
    private final String pattern;

    Like(LeftOperand operand, String pattern) {
        this.operand = requireNonNull(operand, "No operand specified");

        requireNonNull(pattern, "No pattern specified");
        this.pattern = pattern.contains("%") ? pattern : "%" + pattern + "%";
    }

    @Override
    public String sql(Context context) {
        return operand.sql(context) + " like ?";
    }

    @Override
    public String negatedSql(Context context) {
        return operand.sql(context) + " not like ?";
    }

    @Override
    public Stream<Object> params(Context context) {
        return Stream.of(pattern);
    }

    @Override
    public Stream<Field> fields() {
        return operand.fields();
    }
}
