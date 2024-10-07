package org.github.torand.fastersql.function.singlerow;

import org.github.torand.fastersql.Context;
import org.github.torand.fastersql.Field;
import org.github.torand.fastersql.projection.Projection;

import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static org.github.torand.fastersql.util.contract.Requires.require;
import static org.github.torand.fastersql.util.contract.Requires.requireNonBlank;
import static org.github.torand.fastersql.util.lang.StringHelper.nonBlank;

public class ToNumber implements SingleRowFunction {
    private final Field field;
    private final int precision;
    private final int scale;
    private final String alias;

    ToNumber(Field field, int precision, int scale, String alias) {
        require(() -> precision >= 1, "precision must be 1 or greater");
        require(() -> scale >= 0, "scale must be 0 or greater");
        require(() -> scale <= precision, "scale must be less than or equal precision");

        this.field = requireNonNull(field, "No field specified");
        this.alias = nonBlank(alias) ? alias : defaultAlias(field);
        this.precision = precision;
        this.scale = scale;
    }

    @Override
    public Optional<Field> field() {
        return Optional.of(field);
    }

    @Override
    public Projection as(String alias) {
        requireNonBlank(alias, "No alias specified");
        return new ToNumber(field, precision, scale, alias);
    }

    @Override
    public String alias() {
        return alias;
    }

    @Override
    public String sql(Context context) {
        return context.getDialect().getToNumberFunction(field.sql(context), precision, scale);
    }

    private String defaultAlias(Field field) {
        return "TO_NUMBER_" + field.alias();
    }
}
