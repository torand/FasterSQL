package io.github.torand.fastersql.alias;

public final class Aliases {
    private Aliases() {}

    public static ColumnAlias alias(String alias) {
        return new ColumnAlias(alias);
    }
}
