package org.github.torand.fastersql.order;

import org.github.torand.fastersql.Field;
import org.github.torand.fastersql.Sql;

import java.util.stream.Stream;

public interface Order extends Sql {
    Stream<Field> fields();
}
