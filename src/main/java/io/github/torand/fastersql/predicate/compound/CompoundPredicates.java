/*
 * Copyright (c) 2024-2025 Tore Eide Andersen
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
package io.github.torand.fastersql.predicate.compound;

import io.github.torand.fastersql.predicate.Predicate;

public final class CompoundPredicates {
    private CompoundPredicates() {}

    public static And and(Predicate... operands) {
        return new And(operands);
    }

    public static Or or(Predicate... operands) {
        return new Or(operands);
    }

    public static Not not(Predicate operand) {
        return new Not(operand);
    }
}
