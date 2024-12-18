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
package io.github.torand.fastersql.function.singlerow;

import io.github.torand.fastersql.Field;

public final class SingleRowFunctions {
    private SingleRowFunctions() {}

    public static Upper upper(Field field) {
        return new Upper(field, null);
    }

    public static Lower lower(Field field) {
        return new Lower(field, null);
    }

    public static ToNumber to_number(Field field, int precision, int scale) {
        return new ToNumber(field, precision, scale, null);
    }

    public static ToNumber to_number(Field field, int precision) {
        return new ToNumber(field, precision, 0, null);
    }
}
