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
package io.github.torand.fastersql.function.aggregate;

import io.github.torand.fastersql.Field;

public final class Aggregates {
    private Aggregates() {}

    public static Max max(Field field) {
        return new Max(field, null);
    }

    public static Min min(Field field) {
        return new Min(field, null);
    }

    public static Count count(Field field) {
        return new Count(field, null);
    }

    public static CountAll countAll() {
        return new CountAll(null);
    }

    public static Sum sum(Field field) {
        return new Sum(field, null);
    }
}
