package org.github.torand.fastersql.statement;

import org.github.torand.fastersql.Context;
import org.github.torand.fastersql.Table;

import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Objects.requireNonNull;
import static org.github.torand.fastersql.Command.TRUNCATE;

public class TruncateStatement extends PreparableStatement {
    private final Table table;

    TruncateStatement(Table table) {
        this.table = requireNonNull(table, "No table specified");
    }

    @Override
    String sql(Context context) {
        final Context localContext = context.withCommand(TRUNCATE);

        StringBuilder sb = new StringBuilder();
        sb.append("truncate table ");
        sb.append(table.sql(localContext));

        return sb.toString();
    }

    @Override
    List<Object> params(Context context) {
        return emptyList();
    }
}
