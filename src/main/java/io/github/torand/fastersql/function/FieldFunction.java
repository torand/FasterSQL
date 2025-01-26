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
package io.github.torand.fastersql.function;

import io.github.torand.fastersql.order.Order;
import io.github.torand.fastersql.order.Orders;

/**
 * A function that operates on a specific field (or no specific field).
 */
public interface FieldFunction extends Function {

    default Order ascIf(boolean condition) {
        return condition ? asc() : desc();
    }

    default Order asc() {
        return Orders.asc(this);
    }

    default Order desc() {
        return Orders.desc(this);
    }
}
