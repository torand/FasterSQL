package io.github.torand.fastersql.alias;

public final class Aliases {
    private Aliases() {}

    public static ColumnAlias alias(String alias) {
        return new ColumnAlias(alias);
    }

    public static ColumnRef colRef(String tableAlias, String columnAlias) {
        return new ColumnRef(tableAlias, columnAlias, null);
    }
}
