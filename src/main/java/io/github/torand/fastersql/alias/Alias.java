package io.github.torand.fastersql.alias;

import io.github.torand.fastersql.Sql;

/**
 * Represents an alias (label) for a column or table
 */
public interface Alias extends Sql {

    String name();
}
