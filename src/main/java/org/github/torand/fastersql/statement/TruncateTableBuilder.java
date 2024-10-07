package org.github.torand.fastersql.statement;

import org.github.torand.fastersql.Table;

import static java.util.Objects.requireNonNull;

public class TruncateTableBuilder {
    public TruncateStatement table(Table table) {
        return new TruncateStatement(requireNonNull(table, "No table specified"));
    }
}
