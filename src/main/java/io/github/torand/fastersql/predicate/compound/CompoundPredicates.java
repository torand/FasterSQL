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

/**
 * Provides factory methods for compound predicates.
 */
public final class CompoundPredicates {
    private CompoundPredicates() {}

    /**
     * Creates a compound predicate using the boolean operator AND on specified predicate operands.
     * @param operands the predicate operands.
     * @return the compound predicate.
     */
    public static And and(Predicate... operands) {
        return new And(operands);
    }

    /**
     * Creates a compound predicate using the boolean operator OR on specified predicate operands.
     * @param operands the predicate operands.
     * @return the compound predicate.
     */
    public static Or or(Predicate... operands) {
        return new Or(operands);
    }

    /**
     * Creates a compound predicate using the boolean negation operator on specified predicate operand.
     * @param operand the predicate operand.
     * @return the compound predicate.
     */
    public static Not not(Predicate operand) {
        return new Not(operand);
    }
}
