package io.github.torand.fastersql.order;

import io.github.torand.fastersql.Sql;

/**
 * Represents an expression to define the ordering of rows from a query.
 */
public interface OrderExpression extends Sql {

    default Order asc() {
        return Orders.asc(this);
    }

    default Order desc() {
        return Orders.desc(this);
    }

    default Order ascIf(boolean condition) {
        return condition ? asc() : desc();
    }
}
