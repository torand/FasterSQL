package org.github.torand.fastersql.function.system;

import org.github.torand.fastersql.Context;
import org.github.torand.fastersql.projection.Projection;

import static org.github.torand.fastersql.util.contract.Requires.requireNonBlank;

public class CurrentTimestamp implements SystemFunction {
    private final String alias;

    CurrentTimestamp(String alias) {
        this.alias = alias;
    }

    @Override
    public Projection as(String alias) {
        requireNonBlank(alias, "No alias specified");
        return new CurrentTimestamp(alias);
    }

    @Override
    public String alias() {
        return alias;
    }

    @Override
    public String sql(Context context) {
        return "current_timestamp";
    }
}
