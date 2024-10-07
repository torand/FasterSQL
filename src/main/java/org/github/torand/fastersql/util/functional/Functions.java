package org.github.torand.fastersql.util.functional;

import java.util.function.Function;

/**
 * General purpose functions for streams.
 */
public final class Functions {
    private Functions() {}

    /**
     * Returns a function casting the input object to the specified class.
     * @param targetClass the class to cast to
     * @param <S> the input object type
     * @param <T> the output object type
     * @return the function object
     */
    public static <S, T> Function<S,T> castTo(Class<T> targetClass) {
        return targetClass::cast;
    }
}
