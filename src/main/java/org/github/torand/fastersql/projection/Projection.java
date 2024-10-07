package org.github.torand.fastersql.projection;

import org.github.torand.fastersql.Sql;

/**
 * A construct that can be returned in a result set, i.e. an item in the SELECT clause.
 */
public interface Projection extends Sql {
    Projection as(String alias);

    String alias();
}
