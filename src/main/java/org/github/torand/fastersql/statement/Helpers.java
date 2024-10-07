package org.github.torand.fastersql.statement;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static org.github.torand.fastersql.util.collection.CollectionHelper.streamSafely;

public final class Helpers {
    private Helpers() {}

    public static String paramMarkers(long count) {
        return Stream.generate(() -> "?").limit(count).collect(joining(", "));
    }

    public static Object unwrapOptional(Object obj) {
        if (obj instanceof Optional<?> opt) {
            return opt.orElse(null);
        } else {
            return obj;
        }
    }

    @SafeVarargs
    public static <T> Collection<T> unwrapSuppliers(Supplier<T>... suppliers) {
        return streamSafely(suppliers).map(Supplier::get).filter(Objects::nonNull).toList();
    }
}
