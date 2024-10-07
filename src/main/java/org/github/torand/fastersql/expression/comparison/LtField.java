package org.github.torand.fastersql.expression.comparison;

import org.github.torand.fastersql.Context;
import org.github.torand.fastersql.Field;
import org.github.torand.fastersql.expression.Expression;
import org.github.torand.fastersql.expression.LeftOperand;

import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

public class LtField implements Expression {
    private final LeftOperand left;
    private final Field right;

    LtField(LeftOperand left, Field right) {
        this.left = requireNonNull(left, "No left field specified");
        this.right = requireNonNull(right, "No right field specified");
    }

    @Override
    public String sql(Context context) {
        return left.sql(context) + " < " + right.sql(context);
    }

    @Override
    public String negatedSql(Context context) {
        return left.sql(context) + " >= " + right.sql(context);
    }

    @Override
    public Stream<Object> params(Context context) {
        return Stream.empty();
    }

    @Override
    public Stream<Field> fields() {
        return Stream.concat(left.fields(), Stream.of(right));
    }
}
