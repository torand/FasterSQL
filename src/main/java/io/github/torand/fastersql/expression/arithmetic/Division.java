package io.github.torand.fastersql.expression.arithmetic;

import io.github.torand.fastersql.Column;
import io.github.torand.fastersql.Context;
import io.github.torand.fastersql.alias.ColumnAlias;
import io.github.torand.fastersql.expression.Expression;
import io.github.torand.fastersql.order.OrderExpression;
import io.github.torand.fastersql.projection.Projection;

import java.util.Optional;
import java.util.stream.Stream;

import static io.github.torand.fastersql.util.contract.Requires.requireNonBlank;
import static io.github.torand.fastersql.util.lang.StringHelper.nonBlank;
import static java.util.Objects.requireNonNull;

public class Division implements Expression, OrderExpression {
    private final Expression left;
    private final Expression right;
    private final ColumnAlias alias;

    Division(Expression left, Expression right, String alias) {
        this.left = requireNonNull(left, "No left operand specified");
        this.right = requireNonNull(right, "No right operand specified");
        this.alias = nonBlank(alias) ? new ColumnAlias(alias) : defaultAlias();
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

    @Override
    public Stream<Column> columnRefs() {
        return Stream.concat(left.columnRefs(), right.columnRefs());
    }

    @Override
    public Stream<ColumnAlias> aliasRefs() {
        return Stream.concat(left.aliasRefs(), right.aliasRefs());
    }

    // Projection

    @Override
    public Projection as(String alias) {
        requireNonBlank(alias, "No alias specified");
        return new Division(left, right, alias);
    }

    @Override
    public Optional<ColumnAlias> alias() {
        return Optional.ofNullable(alias);
    }

    private ColumnAlias defaultAlias() {
        return ColumnAlias.generate("DIVIDE_");
    }
}
