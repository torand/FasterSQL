/*
 * Copyright (c) 2024-2026 Tore Eide Andersen
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
package io.github.torand.fastersql.statement;

import io.github.torand.fastersql.dialect.AnsiIsoDialect;
import io.github.torand.fastersql.model.Table;
import io.github.torand.fastersql.sql.Context;

import java.util.stream.Stream;

import static io.github.torand.fastersql.dialect.Capability.TRUNCATE_TABLE;
import static io.github.torand.fastersql.sql.Command.TRUNCATE;
import static java.util.Objects.requireNonNull;

/**
 * Implements a TRUNCATE statement.
 */
public class TruncateStatement implements PreparableStatement {
    private final Table table;

    TruncateStatement(Table table) {
        this.table = requireNonNull(table, "No table specified");
    }

    @Override
    public String sql(Context context) {
        final Context localContext = context.withCommand(TRUNCATE);

        StringBuilder sb = new StringBuilder();
        if (localContext.getDialect().supports(TRUNCATE_TABLE)) {
            sb.append("truncate table ");
        } else {
            sb.append("delete from ");
        }
        sb.append(table.sql(localContext));

        return sb.toString();
    }

    @Override
    public Stream<Object> params(Context context) {
        return Stream.empty();
    }

    @Override
    public String toString() {
        return toString(new AnsiIsoDialect());
    }
}
