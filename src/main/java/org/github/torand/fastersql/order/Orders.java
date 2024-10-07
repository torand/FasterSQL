package org.github.torand.fastersql.order;

import org.github.torand.fastersql.projection.Projection;

public final class Orders {
    private Orders() {}

    public static Ascending asc(Projection projection) {
        return new Ascending(projection);
    }

    public static Descending desc(Projection projection) {
        return new Descending(projection);
    }
}
