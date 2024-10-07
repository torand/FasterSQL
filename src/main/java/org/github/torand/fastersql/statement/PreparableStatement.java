package org.github.torand.fastersql.statement;

import org.github.torand.fastersql.Context;
import org.github.torand.fastersql.dialect.Dialect;
import org.github.torand.fastersql.dialect.MySqlDialect;

import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.joining;
import static org.github.torand.fastersql.util.collection.CollectionHelper.streamSafely;
import static org.github.torand.fastersql.util.lang.StringHelper.isBlank;

public abstract class PreparableStatement {
    abstract String sql(Context context);
    abstract List<Object> params(Context context);

    public String toString(Dialect dialect) {
        Context context = Context.of(dialect);

        String stringifiedParams = streamSafely(params(context))
            .map(Objects::toString)
            .collect(joining(", "));

        return sql(context) + (isBlank(stringifiedParams) ? " with no params" : " with params " + stringifiedParams);
    }

    @Override
    public String toString() {
        return toString(new MySqlDialect());
    }
}
