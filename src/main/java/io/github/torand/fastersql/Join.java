/*
 * Copyright (c) 2024 Tore Eide Andersen
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
package io.github.torand.fastersql;

import io.github.torand.fastersql.alias.ColumnAlias;

import java.util.List;
import java.util.stream.Stream;

import static io.github.torand.fastersql.util.collection.CollectionHelper.asList;
import static io.github.torand.fastersql.util.collection.CollectionHelper.concat;
import static io.github.torand.fastersql.util.collection.CollectionHelper.headOf;
import static io.github.torand.fastersql.util.collection.CollectionHelper.streamSafely;
import static io.github.torand.fastersql.util.contract.Requires.require;
import static java.util.Objects.requireNonNull;

public class Join implements Sql {
    private enum JoinMode {
        INNER("inner join"), LEFT_OUTER("left outer join"), RIGHT_OUTER("right outer join");

        final String sql;

        JoinMode(String sql) {
            this.sql = sql;
        }
    }

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

    public Join leftOuter() {
        return new Join(lefts, rights, JoinMode.LEFT_OUTER);
    }

    public Join rightOuter() {
        return new Join(lefts, rights, JoinMode.RIGHT_OUTER);
    }

    public Join and(Join next) {
        require(() -> headOf(this.lefts).table().equals(headOf(next.lefts).table()), "Left side of nested joins must belong to the same table");
        require(() -> headOf(this.rights).table().equals(headOf(next.rights).table()), "Right side of nested joins must belong to the same table");

        List<Column> concatenatedLefts = concat(this.lefts, next.lefts);
        List<Column> concatenatedRights = concat(this.rights, next.rights);

        return new Join(concatenatedLefts, concatenatedRights, mode);
    }

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
