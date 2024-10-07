package org.github.torand.fastersql.expression.logical;

import org.github.torand.fastersql.Context;
import org.github.torand.fastersql.Field;
import org.github.torand.fastersql.expression.Expression;

import java.util.List;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;
import static org.github.torand.fastersql.util.contract.Requires.requireNonEmpty;

public class Or implements Expression {
    private final List<Expression> operands;

    Or(Expression... operands) {
        this.operands = asList(requireNonEmpty(operands, "No operands specified"));
    }

    @Override
    public String sql(Context context) {
        return "(" + operands.stream().map(e -> e.sql(context)).collect(joining(" or ")) + ")";
    }

    @Override
    public String negatedSql(Context context) {
        return "not " + sql(context);
    }

    @Override
    public Stream<Object> params(Context context) {
        return operands.stream().flatMap(o -> o.params(context));
    }

    @Override
    public Stream<Field> fields() {
        return operands.stream().flatMap(Expression::fields);
    }
}
