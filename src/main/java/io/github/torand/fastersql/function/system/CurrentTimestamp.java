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
package io.github.torand.fastersql.function.system;

import io.github.torand.fastersql.alias.ColumnAlias;
import io.github.torand.fastersql.projection.Projection;
import io.github.torand.fastersql.sql.Context;

import java.util.Optional;

import static io.github.torand.javacommons.contract.Requires.requireNonBlank;
import static io.github.torand.javacommons.lang.StringHelper.nonBlank;

/**
 * Implements the current timestamp system function.
 */
public class CurrentTimestamp implements SystemFunction {
    private final ColumnAlias alias;

    CurrentTimestamp(String alias) {
        this.alias = nonBlank(alias) ? new ColumnAlias(alias) : null;
    }

    // Sql

    @Override
    public String sql(Context context) {
        return "current_timestamp";
    }

    // Projection

    @Override
    public Projection as(String alias) {
        requireNonBlank(alias, "No alias specified");
        return new CurrentTimestamp(alias);
    }

    @Override
    public Optional<ColumnAlias> alias() {
        return Optional.ofNullable(alias);
    }
}
