package org.github.torand.fastersql.dialect;

public final class H2CustomFunctions {
    private H2CustomFunctions() {}

    public static Long toNumber(String value, String format) {
        return value == null ? null : Long.valueOf(value);
    }
}
