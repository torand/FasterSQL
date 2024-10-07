package org.github.torand.fastersql.statement;

import org.github.torand.fastersql.Context;
import org.github.torand.fastersql.Field;
import org.github.torand.fastersql.function.Function;

import java.util.Optional;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

/**
 * A field-value pair used in UPDATE and INSERT statements.
 */
class FieldValue {
    private final Field field;
    private final Object value;

    FieldValue(Field field, Object value) {
        this.field = requireNonNull(field, "No field specified");
        this.value = value;
    }

    Field field() {
        return field;
    }

    Optional<Object> param() {
        return isParameterized() ? Optional.ofNullable(value) : Optional.empty();
    }

    String valueSql(Context context) {
        if (isNull(value)) {
            return "null";
        } else if (isParameterized()) {
            return "?";
        } else {
            Function function = (Function)value;
            return function.sql(context);
        }
    }

    private boolean isParameterized() {
        return !(value instanceof Function);
    }
}