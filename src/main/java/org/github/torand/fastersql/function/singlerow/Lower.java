package org.github.torand.fastersql.function.singlerow;

import org.github.torand.fastersql.Context;
import org.github.torand.fastersql.Field;
import org.github.torand.fastersql.projection.Projection;

import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static org.github.torand.fastersql.util.contract.Requires.requireNonBlank;
import static org.github.torand.fastersql.util.lang.StringHelper.nonBlank;

public class Lower implements SingleRowFunction {
    private final Field field;
    private final String alias;

    Lower(Field field, String alias) {
        this.field = requireNonNull(field, "No field specified");
        this.alias = nonBlank(alias) ? alias : defaultAlias(field);
    }

    @Override
    public Optional<Field> field() {
        return Optional.of(field);
    }

    @Override
    public Projection as(String alias) {
        requireNonBlank(alias, "No alias specified");
        return new Lower(field, alias);
    }

    @Override
    public String alias() {
        return alias;
    }

    @Override
    public String sql(Context context) {
        return "lower(" + field.sql(context) + ")";
    }

    private String defaultAlias(Field field) {
        return "LOWER_" + field.alias();
    }
}
