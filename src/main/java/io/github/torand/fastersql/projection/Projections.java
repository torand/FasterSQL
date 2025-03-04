package io.github.torand.fastersql.projection;

import io.github.torand.fastersql.statement.SelectStatement;
import io.github.torand.fastersql.subquery.ExpressionSubquery;

public final class Projections {
    private Projections() {}

    public static ColumnPosition colPos(int position) {
        return new ColumnPosition(position);
    }

    public static ExpressionSubquery subquery(SelectStatement query) {
        return new ExpressionSubquery(query);
    }
}
