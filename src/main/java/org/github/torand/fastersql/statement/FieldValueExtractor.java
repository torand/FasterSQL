package org.github.torand.fastersql.statement;

import org.github.torand.fastersql.Context;
import org.github.torand.fastersql.Field;

import java.util.Optional;
import java.util.function.Function;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

/**
 * A field-value pair used in INSERT batch statements.
 */
class FieldValueExtractor<T> {
    private final Field field;
    private final Function<? super T, Object> valueExtractor;

    public FieldValueExtractor(Field field, Function<? super T, Object> valueExtractor) {
        this.field = requireNonNull(field, "No field specified");
        this.valueExtractor = requireNonNull(valueExtractor, "No valueExtractor specified");
    }

    Field field() {
        return field;
    }

    Optional<Object> param(T entity) {
        return Optional.ofNullable(extractValue(entity));
    }

    String valueSql(Context context, T entity) {
        Object value = extractValue(entity);
        if (isNull(value)) {
            return "null";
        } else {
            return "?";
        }
    }

    private Object extractValue(T entity) {
        return valueExtractor.andThen(Helpers::unwrapOptional).apply(entity);
    }
}