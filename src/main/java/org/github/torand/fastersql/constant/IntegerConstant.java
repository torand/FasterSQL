package org.github.torand.fastersql.constant;

import org.github.torand.fastersql.Context;
import org.github.torand.fastersql.Field;
import org.github.torand.fastersql.projection.Projection;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;
import static org.github.torand.fastersql.util.contract.Requires.requireNonBlank;

public class IntegerConstant implements Constant {
    private final Long value;
    private final String alias;

    IntegerConstant(Long value, String alias) {
        this.value = value;
        this.alias = alias;
    }

    @Override
    public Object value() {
        return value;
    }

    @Override
    public Projection as(String alias) {
        requireNonBlank(alias, "No alias specified");
        return new IntegerConstant(value, alias);
    }

    @Override
    public Projection forField(Field field) {
        requireNonNull(field, "No field specified");
        return new IntegerConstant(value, field.alias());
    }

    @Override
    public String alias() {
        return alias;
    }

    @Override
    public String sql(Context context) {
        return isNull(value) ? "null" : Long.toString(value);
    }
}
