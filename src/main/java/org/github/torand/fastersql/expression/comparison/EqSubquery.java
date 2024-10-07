package org.github.torand.fastersql.expression.comparison;

import org.github.torand.fastersql.Context;
import org.github.torand.fastersql.Field;
import org.github.torand.fastersql.expression.Expression;
import org.github.torand.fastersql.expression.LeftOperand;
import org.github.torand.fastersql.subquery.Subquery;

import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

public class EqSubquery implements Expression {
    private final LeftOperand operand;
    private final Subquery subquery;

    EqSubquery(LeftOperand operand, Subquery subquery) {
        this.operand = requireNonNull(operand, "No operand specified");
        this.subquery = requireNonNull(subquery, "No sub query specified");
    }

    @Override
    public String sql(Context context) {
        return operand.sql(context) + " = " + subquery.sql(context);
    }

    @Override
    public String negatedSql(Context context) {
        return operand.sql(context) + " != " + subquery.sql(context);
    }

    @Override
    public Stream<Object> params(Context context) {
        return subquery.params(context).stream();
    }

    @Override
    public Stream<Field> fields() {
        return operand.fields();
    }
}
