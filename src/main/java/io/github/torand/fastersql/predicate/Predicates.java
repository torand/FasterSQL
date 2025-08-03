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
package io.github.torand.fastersql.predicate;

import io.github.torand.fastersql.subquery.ExpressionSubquery;

import java.util.Collection;

/**
 * Provides factory methods for predicates.
 */
public final class Predicates {
    private Predicates() {
    }

    /**
     * Create a 'member of set' predicate taking a collection of scalar values as right operand.
     *
     * @param left  the left operand.
     * @param right the right operand (scalar value collection).
     * @return the predicate.
     */
    public static In in(LeftOperand left, Collection<?> right) {
        return new In(left, right);
    }

    /**
     * Create a 'member of set' predicate taking a subquery as right operand.
     *
     * @param left  the left operand.
     * @param right the right operand (subquery).
     * @return the predicate.
     */
    public static InSubquery in(LeftOperand left, ExpressionSubquery right) {
        return new InSubquery(left, right);
    }

    /**
     * Create an 'existence' (at least one row) predicate taking a subquery as right operand.
     *
     * @param operand the operand (subquery).
     * @return the predicate.
     */
    public static ExistsSubquery exists(ExpressionSubquery operand) {
        return new ExistsSubquery(operand);
    }

    /**
     * Creates a pattern matching predicate.
     *
     * @param left  the left operand.
     * @param right the right operand (pattern).
     * @return the predicate.
     */
    public static Like like(LeftOperand left, String right) {
        return new Like(left, right);
    }

    /**
     * Creates an 'is null' predicate.
     *
     * @param operand the operand.
     * @return the predicate.
     */
    public static IsNull isNull(LeftOperand operand) {
        return new IsNull(operand);
    }
}
