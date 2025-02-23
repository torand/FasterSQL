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
package io.github.torand.fastersql.order;

import io.github.torand.fastersql.constant.IntegerConstant;
import io.github.torand.fastersql.constant.StringConstant;
import io.github.torand.fastersql.projection.Projection;

public final class Orders {
    private Orders() {}

    public static OrderBuilder alias(String alias) {
        return new OrderBuilder(alias);
    }

    public static OrderBuilder index(Integer index) {
        return new OrderBuilder(index);
    }

    public static Ascending asc(Projection projection) {
        return new Ascending(projection);
    }

    public static Ascending asc(StringConstant alias) {
        return new Ascending(alias.value());
    }

    public static Ascending asc(IntegerConstant index) {
        return new Ascending(index.value());
    }

    public static Descending desc(Projection projection) {
        return new Descending(projection);
    }

    public static Descending desc(StringConstant alias) {
        return new Descending(alias.value());
    }

    public static Descending desc(IntegerConstant index) {
        return new Descending(index.value());
    }
}
