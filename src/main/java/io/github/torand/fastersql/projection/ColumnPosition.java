package io.github.torand.fastersql.projection;

import io.github.torand.fastersql.Column;
import io.github.torand.fastersql.Context;
import io.github.torand.fastersql.Sql;
import io.github.torand.fastersql.alias.ColumnAlias;
import io.github.torand.fastersql.order.OrderExpression;

import java.util.stream.Stream;

import static io.github.torand.fastersql.util.contract.Requires.require;

/**
 * Represents an enumeration of a projection.
 */
public class ColumnPosition implements Sql, OrderExpression {
    private final int position;

    public ColumnPosition(int position) {
        require(() -> position >= 1, "Position must be 1 or greater");
        this.position = position;
    }

    // Sql

    @Override
    public String sql(Context context) {
        return Integer.toString(position);
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
        return Stream.empty();
    }
}
