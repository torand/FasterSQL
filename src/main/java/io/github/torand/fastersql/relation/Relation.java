package io.github.torand.fastersql.relation;

import io.github.torand.fastersql.Sql;
import io.github.torand.fastersql.alias.TableAlias;

/**
 * Represents a table of columns and rows, whether a permanent database table or a temporary table (the result of a subquery).
 */
public interface Relation extends Sql {

    Relation as(String alias);

    TableAlias alias();
}
