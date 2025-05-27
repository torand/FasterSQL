/*
 * Copyright (c) 2024-2025 Tore Eide Andersen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.torand.fastersql.join;

import io.github.torand.fastersql.alias.ColumnAlias;
import io.github.torand.fastersql.model.Column;
import io.github.torand.fastersql.model.Table;
import io.github.torand.fastersql.sql.Context;
import io.github.torand.fastersql.sql.Sql;

import java.util.List;
import java.util.stream.Stream;

import static io.github.torand.fastersql.util.collection.CollectionHelper.asList;
import static io.github.torand.fastersql.util.collection.CollectionHelper.concat;
import static io.github.torand.fastersql.util.collection.CollectionHelper.headOf;
import static io.github.torand.fastersql.util.collection.CollectionHelper.streamSafely;
import static io.github.torand.fastersql.util.contract.Requires.require;
import static java.util.Objects.requireNonNull;

/**
 * Implements a JOIN clause.
 */
public class Join implements Sql {

    private final List<Column> lefts;
    private final List<Column> rights;
    private final JoinMode mode;

    public Join(Column left, Column right) {
        this.lefts = asList(requireNonNull(left, "No left column specified"));
        this.rights = asList(requireNonNull(right, "No right column specified"));
        this.mode = JoinMode.INNER;
    }

    private Join(List<Column> lefts, List<Column> rights, JoinMode mode) {
        this.lefts = asList(lefts);
        this.rights = asList(rights);
        this.mode = mode;
    }

    /**
     * Specifies that this is a LEFT OUTER JOIN clause.
     * @return the modified JOIN clause.
     */
    public Join leftOuter() {
        return new Join(lefts, rights, JoinMode.LEFT_OUTER);
    }

    /**
     * Specifies that this is a RIGHT OUTER JOIN clause.
     * @return the modified JOIN clause.
     */
    public Join rightOuter() {
        return new Join(lefts, rights, JoinMode.RIGHT_OUTER);
    }

    /**
     * Creates a nested JOIN clause by combining this JOIN clause with the specified JOIN clause.
     * @param next the JOIN clause to combine with.
     * @return the nested JOIN clause.
     */
    public Join and(Join next) {
        require(() -> headOf(this.lefts).table().equals(headOf(next.lefts).table()), "Left side of nested joins must belong to the same table");
        require(() -> headOf(this.rights).table().equals(headOf(next.rights).table()), "Right side of nested joins must belong to the same table");

        List<Column> concatenatedLefts = concat(this.lefts, next.lefts);
        List<Column> concatenatedRights = concat(this.rights, next.rights);

        return new Join(concatenatedLefts, concatenatedRights, mode);
    }

    /**
     * Gets the table joined with.
     * @return the table joined with.
     */
    public Table<?> joined() {
        return headOf(rights).table();
    }

    // Sql

    @Override
    public String sql(Context context) {
        Table<?> rightTable = headOf(this.rights).table();
        StringBuilder sql = new StringBuilder()
            .append(mode.sql)
            .append(" ")
            .append(rightTable.sql(context))
            .append(" on ");

        for (int i = 0; i < this.lefts.size(); i++) {
            if (i > 0) {
                sql.append(" and ");
            }
            sql.append(this.lefts.get(i).sql(context))
                .append(" = ")
                .append(this.rights.get(i).sql(context));
        }

        return sql.toString();
    }

    @Override
    public Stream<Object> params(Context context) {
        return Stream.empty();
    }

    @Override
    public Stream<Column> columnRefs() {
        return Stream.concat(streamSafely(lefts), streamSafely(rights));
    }

    @Override
    public Stream<ColumnAlias> aliasRefs() {
        return Stream.empty();
    }
}
