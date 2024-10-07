package org.github.torand.fastersql.util.functional;

import java.util.function.Predicate;

/**
 * General purpose predicates for streams.
 */
public final class Predicates {
    private Predicates() {}

    /**
     * Negates the outcome of another predicate.
     * @param inner the predicate to be negated
     * @return the predicate
     */
    public static <T> Predicate<T> not(Predicate<T> inner) {
        return inner.negate();
    }

    /**
     * Tests whether an object is instance of specified class.
     * @param clazz the class to test instance on
     * @return the predicate
     */
    public static <T> Predicate<? super T> instanceOf(Class<? extends T> clazz) {
        return clazz::isInstance;
    }
}
