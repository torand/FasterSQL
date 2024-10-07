package org.github.torand.fastersql.subquery;

import org.github.torand.fastersql.Context;
import org.github.torand.fastersql.Sql;
import org.github.torand.fastersql.statement.SelectStatement;

import java.util.List;

import static java.util.Objects.requireNonNull;
import static org.github.torand.fastersql.util.contract.Requires.requireNonBlank;

public class Subquery implements Sql {
    private final SelectStatement selectStatement;
    private final String alias;

    public Subquery(SelectStatement selectStatement) {
        this.selectStatement = requireNonNull(selectStatement, "No select statement specified");
        this.alias = null;
    }

    private Subquery(SelectStatement selectStatement, String alias) {
        this.selectStatement = requireNonNull(selectStatement, "No select statement specified");
        this.alias = requireNonBlank(alias, "No alias specified");
    }

    public Subquery as(String alias) {
        return new Subquery(selectStatement, alias);
    }

    public String alias() {
        return alias;
    }

    public List<Object> params(Context context) {
        return selectStatement.params(context);
    }

    @Override
    public String sql(Context context) {
        return "(" + selectStatement.sql(context) + ")";
    }
}
