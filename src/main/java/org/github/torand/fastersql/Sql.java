package org.github.torand.fastersql;

/**
 * A construct that can be expressed as an SQL fragment.
 */
public interface Sql {
    String sql(Context context);
}
