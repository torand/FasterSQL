package org.github.torand.fastersql.function.singlerow;

import org.github.torand.fastersql.Field;
import org.github.torand.fastersql.expression.LeftOperand;
import org.github.torand.fastersql.function.FieldFunction;
import org.github.torand.fastersql.util.functional.Optionals;

import java.util.stream.Stream;

/**
 * A function that returns a single value based upon one value from a single row.
 */
public interface SingleRowFunction extends FieldFunction, LeftOperand {
    @Override
    default Stream<Field> fields() {
        return Optionals.stream(field());
    }
}
