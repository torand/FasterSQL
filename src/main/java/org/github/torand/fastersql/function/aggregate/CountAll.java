package org.github.torand.fastersql.function.aggregate;

import org.github.torand.fastersql.Context;
import org.github.torand.fastersql.Field;
import org.github.torand.fastersql.order.Order;
import org.github.torand.fastersql.projection.Projection;

import java.util.Optional;

import static org.github.torand.fastersql.util.contract.Requires.requireNonBlank;
import static org.github.torand.fastersql.util.lang.StringHelper.nonBlank;

public class CountAll implements AggregateFunction {
    private final String alias;

    CountAll(String alias) {
        this.alias = nonBlank(alias) ? alias : defaultAlias();
    }

    @Override
    public Projection as(String alias) {
        requireNonBlank(alias, "No alias specified");
        return new CountAll(alias);
    }

    @Override
    public Optional<Field> field() {
        return Optional.empty();
    }

    @Override
    public Order ascIf(boolean condition) {
        throw new UnsupportedOperationException("The 'count(*)' function can't be used in an 'order by' clause");
    }

    @Override
    public Order asc() {
        throw new UnsupportedOperationException("The 'count(*)' function can't be used in an 'order by' clause");
    }

    @Override
    public Order desc() {
        throw new UnsupportedOperationException("The 'count(*)' function can't be used in an 'order by' clause");
    }

    @Override
    public String alias() {
        return alias;
    }

    @Override
    public String sql(Context context) {
        return "count(*)";
    }

    private String defaultAlias() {
        return "COUNT_ALL";
    }
}
