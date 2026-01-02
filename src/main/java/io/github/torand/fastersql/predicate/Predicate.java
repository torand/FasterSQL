/*
 * Copyright (c) 2024-2026 Tore Eide Andersen
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

import io.github.torand.fastersql.predicate.compound.CompoundPredicates;
import io.github.torand.fastersql.sql.Context;
import io.github.torand.fastersql.sql.Sql;

/**
 * Defines a restriction on the rows fetched by a SELECT or affected by an UPDATE or DELETE.
 */
public interface Predicate extends Sql {
    /**
     * Formats negated predicate as an SQL fragment.
     * @param context the context (incl. dialect).
     * @return the formatted SQL fragment.
     */
    String negatedSql(Context context);

    /**
     * Creates a compound predicate using the boolean operator OR on this predicate and the specified predicate.
     * @param other the other predicate.
     * @return the compound predicate.
     */
    default Predicate or(Predicate other) {
        return CompoundPredicates.or(this, other);
    }

    /**
     * Creates an optional compound predicate using the boolean operator OR on this predicate and the specified optional predicate.
     * @param optionalOther the other optional predicate
     * @return the optional compound predicate.
     */
    default OptionalPredicate or(OptionalPredicate optionalOther) {
        if (optionalOther.isPresent()) {
            return OptionalPredicate.of(CompoundPredicates.or(this, optionalOther.get()));
        } else {
            return OptionalPredicate.of(this);
        }
    }

    /**
     * Creates a compound predicate using the boolean operator AND on this predicate and the specified predicate.
     * @param other the other predicate.
     * @return the compound predicate.
     */
    default Predicate and(Predicate other) {
        return CompoundPredicates.and(this, other);
    }

    /**
     * Creates an optional compound predicate using the boolean operator AND on this predicate and the specified optional predicate.
     * @param optionalOther the other optional predicate
     * @return the optional compound predicate.
     */
    default OptionalPredicate and(OptionalPredicate optionalOther) {
        if (optionalOther.isPresent()) {
            return OptionalPredicate.of(CompoundPredicates.and(this, optionalOther.get()));
        } else {
            return OptionalPredicate.of(this);
        }
    }
}
