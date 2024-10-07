package org.github.torand.fastersql.function.system;

public final class SystemFunctions {
    private SystemFunctions() {}

    public static CurrentTimestamp currentTimestamp() {
        return new CurrentTimestamp(null);
    }
}
