package org.github.torand.fastersql.function.aggregate;

import org.github.torand.fastersql.Field;

public final class Aggregates {
    private Aggregates() {}

    public static Max max(Field field) {
        return new Max(field, null);
    }

    public static Min min(Field field) {
        return new Min(field, null);
    }

    public static Count count(Field field) {
        return new Count(field, null);
    }

    public static CountAll countAll() {
        return new CountAll(null);
    }

    public static Sum sum(Field field) {
        return new Sum(field, null);
    }
}
