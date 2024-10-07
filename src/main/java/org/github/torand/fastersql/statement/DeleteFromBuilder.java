package org.github.torand.fastersql.statement;

import org.github.torand.fastersql.Table;

import static java.util.Objects.requireNonNull;

public class DeleteFromBuilder {
    DeleteFromBuilder() {}

    public DeleteStatement from(Table table) {
        return new DeleteStatement(requireNonNull(table, "No table specified"), null);
    }
}
