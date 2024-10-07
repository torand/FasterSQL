package org.github.torand.fastersql.function.aggregate;

import org.github.torand.fastersql.Context;
import org.github.torand.fastersql.Field;
import org.github.torand.fastersql.projection.Projection;

import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static org.github.torand.fastersql.util.contract.Requires.requireNonBlank;
import static org.github.torand.fastersql.util.lang.StringHelper.nonBlank;

public class Min implements AggregateFunction {
    private final Field field;
    private final String alias;

    Min(Field field, String alias) {
        this.field = requireNonNull(field, "No field specified");
        this.alias = nonBlank(alias) ? alias : defaultAlias(field);
    }

    @Override
    public Projection as(String alias) {
        requireNonBlank(alias, "No alias specified");
        return new Min(field, alias);
    }

    @Override
    public Optional<Field> field() {
        return Optional.of(field);
    }

    @Override
    public String alias() {
        return alias;
    }

    @Override
    public String sql(Context context) {
        return "min(" + field.sql(context) + ")";
    }

    private String defaultAlias(Field field) {
        return "MIN_" + field.alias();
    }
}
