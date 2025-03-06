package io.github.torand.fastersql.relation;

import io.github.torand.fastersql.Sql;
import io.github.torand.fastersql.alias.TableAlias;

public interface Relation extends Sql {

    Relation as(String alias);

    TableAlias alias();
}
