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
package io.github.torand.fastersql.constant;

import io.github.torand.fastersql.Context;
import io.github.torand.fastersql.Field;
import io.github.torand.fastersql.projection.Projection;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;
import static io.github.torand.fastersql.util.contract.Requires.requireNonBlank;

public class StringConstant implements Constant {
    private final String value;
    private final String alias;

    StringConstant(String value, String alias) {
        this.value = value;
        this.alias = alias;
    }

    @Override
    public Object value() {
        return value;
    }

    @Override
    public Projection as(String alias) {
        requireNonBlank(alias, "No alias specified");
        return new StringConstant(value, alias);
    }

    @Override
    public Projection forField(Field field) {
        requireNonNull(field, "No field specified");
        return new StringConstant(value, field.alias());
    }

    @Override
    public String alias() {
        return alias;
    }

    @Override
    public String sql(Context context) {
        return isNull(value) ? "null" : "'" + value + "'";
    }
}
