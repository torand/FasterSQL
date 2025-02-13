package io.github.torand.fastersql.expression.arithmetic;

import io.github.torand.fastersql.Context;
import io.github.torand.fastersql.Field;
import io.github.torand.fastersql.expression.Expression;
import io.github.torand.fastersql.projection.Projection;

import java.util.Random;
import java.util.stream.Stream;

import static io.github.torand.fastersql.util.contract.Requires.requireNonBlank;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

public class Division implements Expression {
    private final Expression left;
    private final Expression right;
    private final String alias;

    Division(Expression left, Expression right, String alias) {
        this.left = requireNonNull(left, "No left operand specified");
        this.right = requireNonNull(right, "No right operand specified");
        this.alias = nonNull(alias) ? alias : defaultAlias();
    }

    // Sql

    @Override
    public String sql(Context context) {
        String leftSql = left.sql(context);
        if (left instanceof Addition || left instanceof Subtraction) {
            leftSql = "(" + leftSql + ")";
        }

        String rightSql = right.sql(context);
        if (right instanceof Addition || right instanceof Subtraction) {
            rightSql = "(" + rightSql + ")";
        }

        return leftSql + " / " + rightSql;
    }

    @Override
    public Stream<Object> params(Context context) {
        return Stream.concat(left.params(context), right.params(context));
    }

    // Projection

    @Override
    public Projection as(String alias) {
        requireNonBlank(alias, "No alias specified");
        return new Division(left, right, alias);
    }

    @Override
    public String alias() {
        return alias;
    }

    // Expression

    @Override
    public Stream<Field> fieldRefs() {
        return Stream.concat(left.fieldRefs(), right.fieldRefs());
    }

    private String defaultAlias() {
        return "DIVIDE_" + (new Random().nextInt(999) + 1);
    }
}
