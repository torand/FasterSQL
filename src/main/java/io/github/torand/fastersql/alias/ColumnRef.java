package io.github.torand.fastersql.alias;

import io.github.torand.fastersql.Column;
import io.github.torand.fastersql.Context;
import io.github.torand.fastersql.expression.Expression;
import io.github.torand.fastersql.order.OrderExpression;
import io.github.torand.fastersql.predicate.LeftOperand;
import io.github.torand.fastersql.projection.Projection;

import java.util.Optional;
import java.util.stream.Stream;

import static io.github.torand.fastersql.util.contract.Requires.requireNonBlank;
import static io.github.torand.fastersql.util.lang.StringHelper.nonBlank;

/**
 * References a column using a combination of a table alias and a column alias.
 * Typically used to refer to columns in a subquery (inline view).
 */
public class ColumnRef implements LeftOperand, OrderExpression, Expression {
    private final TableAlias tableAlias;
    private final ColumnAlias columnAlias;
    private final ColumnAlias alias;

    ColumnRef(String tableAlias, String columnAlias, String alias) {
        this.tableAlias = new TableAlias(requireNonBlank(tableAlias, "No table alias specified"));
        this.columnAlias = new ColumnAlias(requireNonBlank(columnAlias, "No column alias specified"));
        this.alias = nonBlank(alias) ? new ColumnAlias(alias) : null;
    }

    // Sql

    @Override
    public String sql(Context context) {
        return tableAlias.sql(context) + "." + columnAlias.sql(context);
    }

    @Override
    public Stream<Object> params(Context context) {
        return Stream.empty();
    }

    @Override
    public Stream<Column> columnRefs() {
        return Stream.empty();
    }

    @Override
    public Stream<ColumnAlias> aliasRefs() {
        return Stream.of(columnAlias);
    }

    // Projection

    @Override
    public Projection as(String alias) {
        requireNonBlank(alias, "No alias specified");
        return new ColumnRef(tableAlias.name(), columnAlias.name(), alias);
    }

    @Override
    public Optional<ColumnAlias> alias() {
        return Optional.ofNullable(alias);
    }
}
