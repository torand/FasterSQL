package io.github.torand.fastersql.alias;

import io.github.torand.fastersql.Column;
import io.github.torand.fastersql.Context;

import java.util.stream.Stream;

import static io.github.torand.fastersql.util.contract.Requires.requireNonBlank;

/**
 * Represents an alias (label) for a table
 */
public class TableAlias implements Alias {
    private final String name;

    public TableAlias(String name) {
        this.name = requireNonBlank(name, "No name specified");
    }

    @Override
    public String name() {
        return name;
    }

    // Sql

    @Override
    public String sql(Context context) {
        return name;
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
