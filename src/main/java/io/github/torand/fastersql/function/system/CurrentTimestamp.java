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
package io.github.torand.fastersql.function.system;

import io.github.torand.fastersql.Context;
import io.github.torand.fastersql.projection.Projection;

import static io.github.torand.fastersql.util.contract.Requires.requireNonBlank;

public class CurrentTimestamp implements SystemFunction {
    private final String alias;

    CurrentTimestamp(String alias) {
        this.alias = alias;
    }

    @Override
    public Projection as(String alias) {
        requireNonBlank(alias, "No alias specified");
        return new CurrentTimestamp(alias);
    }

    @Override
    public String alias() {
        return alias;
    }

    @Override
    public String sql(Context context) {
        return "current_timestamp";
    }
}
