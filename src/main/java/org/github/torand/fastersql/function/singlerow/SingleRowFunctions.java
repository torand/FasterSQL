package org.github.torand.fastersql.function.singlerow;

import org.github.torand.fastersql.Field;

public final class SingleRowFunctions {
    private SingleRowFunctions() {}

    public static Upper upper(Field field) {
        return new Upper(field, null);
    }

    public static Lower lower(Field field) {
        return new Lower(field, null);
    }

    public static ToNumber to_number(Field field, int precision, int scale) {
        return new ToNumber(field, precision, scale, null);
    }

    public static ToNumber to_number(Field field, int precision) {
        return new ToNumber(field, precision, 0, null);
    }
}
