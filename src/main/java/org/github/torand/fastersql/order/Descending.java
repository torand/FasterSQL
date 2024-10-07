package org.github.torand.fastersql.order;

import org.github.torand.fastersql.Context;
import org.github.torand.fastersql.Field;
import org.github.torand.fastersql.projection.Projection;

import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

public class Descending implements Order {
    private final Projection projection;

    Descending(Projection projection) {
        this.projection = requireNonNull(projection, "No projection specified");
    }

    @Override
    public String sql(Context context) {
        return projection.alias() + " desc";
    }

    @Override
    public Stream<Field> fields() {
        return projection instanceof Field ? Stream.of((Field)projection) : Stream.empty();
    }
}