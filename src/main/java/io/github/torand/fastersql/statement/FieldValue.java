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
import io.github.torand.fastersql.Field;
import io.github.torand.fastersql.function.Function;

import java.util.Optional;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

/**
 * A field-value pair used in UPDATE and INSERT statements.
 */
class FieldValue {
    private final Field field;
    private final Object value;

    FieldValue(Field field, Object value) {
        this.field = requireNonNull(field, "No field specified");
        this.value = value;
    }

    Field field() {
        return field;
    }

    Optional<Object> param() {
        return isParameterized() ? Optional.ofNullable(value) : Optional.empty();
    }

    String valueSql(Context context) {
        if (isNull(value)) {
            return "null";
        } else if (isParameterized()) {
            return "?";
        } else {
            Function function = (Function)value;
            return function.sql(context);
        }
    }

    private boolean isParameterized() {
        return !(value instanceof Function);
    }
}