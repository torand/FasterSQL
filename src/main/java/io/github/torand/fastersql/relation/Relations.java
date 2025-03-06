package io.github.torand.fastersql.relation;

import io.github.torand.fastersql.statement.SelectStatement;
import io.github.torand.fastersql.subquery.TableSubquery;

public final class Relations {
    private Relations() {}

    public static TableSubquery table(SelectStatement query) {
        return new TableSubquery(query);
    }
}
