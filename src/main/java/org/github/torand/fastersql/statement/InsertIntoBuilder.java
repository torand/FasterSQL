package org.github.torand.fastersql.statement;

import org.github.torand.fastersql.Table;

import static java.util.Objects.requireNonNull;

public class InsertIntoBuilder {
    public InsertStatement into(Table table) {
        return new InsertStatement(requireNonNull(table, "No table specified"), null);
    }
}
