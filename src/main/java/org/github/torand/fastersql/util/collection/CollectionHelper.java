package org.github.torand.fastersql.util.collection;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Objects.*;
import static org.github.torand.fastersql.util.contract.Requires.requireNonEmpty;

/**
 * Helper functions for collections
 */
public final class CollectionHelper {
    private CollectionHelper() {}

    public static <T> List<T> asList(Iterable<T> items) {
        return streamSafely(items).toList();
    }

    @SafeVarargs
    public static <T> List<T> asList(T... items) {
        return streamSafely(items).toList();
    }

    @SafeVarargs
    public static <T> List<T> asNonEmptyList(T first, T... others) {
        requireNonNull(first, "first can not be null");
        return Stream.concat(Stream.of(first), streamSafely(others)).toList();
    }

    public static boolean isEmpty(Collection<?> collection) {
        return isNull(collection) || collection.isEmpty();
    }

    public static boolean isEmpty(Map<?,?> map) {
        return isNull(map) || map.isEmpty();
    }

    public static boolean nonEmpty(Collection<?> collection) {
        return nonNull(collection) && !collection.isEmpty();
    }

    public static boolean nonEmpty(Map<?,?> map) {
        return nonNull(map) && !map.isEmpty();
    }

    public static <T> Stream<T> streamSafely(Iterable<T> iterable) {
        if (isNull(iterable)) {
            return Stream.empty();
        } else {
            return StreamSupport.stream(iterable.spliterator(), false);
        }
    }

    @SafeVarargs
    public static <T> Stream<T> streamSafely(T... items) {
        if (isNull(items)) {
            return Stream.empty();
        } else {
            return Stream.of(items);
        }
    }

    public static <T> List<T> concat(Iterable<T> first, Iterable<T> second) {
        return Stream.concat(streamSafely(first), streamSafely(second)).toList();
    }

    @SafeVarargs
    public static <T> List<T> concat(Iterable<T> first, T... second) {
        return Stream.concat(streamSafely(first), streamSafely(second)).toList();
    }

    public static <T> T headOf(Collection<T> collection) {
        requireNonEmpty(collection, "collection is empty");
        return collection.iterator().next();
    }
}
