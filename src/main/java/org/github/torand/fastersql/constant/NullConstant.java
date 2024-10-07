package org.github.torand.fastersql.constant;

import org.github.torand.fastersql.Context;
import org.github.torand.fastersql.Field;
import org.github.torand.fastersql.projection.Projection;

import static java.util.Objects.requireNonNull;
import static org.github.torand.fastersql.util.contract.Requires.requireNonBlank;

public class NullConstant implements Constant {
    private final String alias;

    NullConstant(String alias) {
        this.alias = alias;
    }

    @Override
    public Object value() {
        return null;
    }

    @Override
    public Projection as(String alias) {
        requireNonBlank(alias, "No alias specified");
        return new NullConstant(alias);
    }

    @Override
    public Projection forField(Field field) {
        requireNonNull(field, "No field specified");
        return new NullConstant(field.alias());
    }

    @Override
    public String alias() {
        return alias;
    }

    @Override
    public String sql(Context context) {
        return "null";
    }
}
