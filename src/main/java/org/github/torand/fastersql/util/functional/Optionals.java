package org.github.torand.fastersql.util.functional;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public final class Optionals {
    private Optionals() {}

    public static <T> Stream<T> stream(Optional<T> optional) {
        return optional.stream();
    }

    public static <T, U> U mapIfNonNull(T value, Function<T, U> mapper) {
        return Optional.ofNullable(value).map(mapper).orElse(null);
    }
}
