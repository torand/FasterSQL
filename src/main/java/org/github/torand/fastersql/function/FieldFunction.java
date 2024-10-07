package org.github.torand.fastersql.function;

import org.github.torand.fastersql.Field;
import org.github.torand.fastersql.order.Order;
import org.github.torand.fastersql.order.Orders;

import java.util.Optional;

/**
 * A function that operates on a specific field (or no specific field).
 */
public interface FieldFunction extends Function {
    // One aggregate function, count(*), does not operate on a specific field
    Optional<Field> field();

    default Order ascIf(boolean condition) {
        return condition ? asc() : desc();
    }

    default Order asc() {
        return Orders.asc(this);
    }

    default Order desc() {
        return Orders.desc(this);
    }
}
