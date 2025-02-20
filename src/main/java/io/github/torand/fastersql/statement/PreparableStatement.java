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
package io.github.torand.fastersql.statement;

import io.github.torand.fastersql.Context;
import io.github.torand.fastersql.dialect.AnsiIsoDialect;
import io.github.torand.fastersql.dialect.Dialect;

import java.util.List;
import java.util.Objects;

import static io.github.torand.fastersql.util.collection.CollectionHelper.streamSafely;
import static io.github.torand.fastersql.util.lang.StringHelper.isBlank;
import static java.util.stream.Collectors.joining;

public abstract class PreparableStatement {
    abstract String sql(Context context);
    abstract List<Object> params(Context context);

    public String toString(Dialect dialect) {
        Context context = Context.of(dialect);

        String stringifiedParams = streamSafely(params(context))
            .map(Objects::toString)
            .collect(joining(", "));

        return sql(context) + (isBlank(stringifiedParams) ? " with no params" : " with params " + stringifiedParams);
    }

    @Override
    public String toString() {
        return toString(new AnsiIsoDialect());
    }
}
