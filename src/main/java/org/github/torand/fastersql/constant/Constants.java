package org.github.torand.fastersql.constant;

import java.util.UUID;

import static org.github.torand.fastersql.util.functional.Optionals.mapIfNonNull;

public final class Constants {

    private Constants() {}

    public static StringConstant constant(UUID value) {
        return new StringConstant(mapIfNonNull(value, UUID::toString), null);
    }

    public static StringConstant constant(Enum<? extends Enum<?>> value) {
        return new StringConstant(mapIfNonNull(value, Enum::name), null);
    }

    public static StringConstant constant(String value) {
        return new StringConstant(value, null);
    }

    public static IntegerConstant constant(Long value) {
        return new IntegerConstant(value, null);
    }

    public static NullConstant nullValue() {
        return new NullConstant(null);
    }
}
