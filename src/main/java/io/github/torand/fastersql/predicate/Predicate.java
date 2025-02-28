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
package io.github.torand.fastersql.predicate;

import io.github.torand.fastersql.Context;
import io.github.torand.fastersql.Sql;
import io.github.torand.fastersql.predicate.compound.CompoundPredicates;

/**
 * Represents a restriction on the rows fetched by a SELECT or affected by an UPDATE or DELETE.
 */
public interface Predicate extends Sql {
    String negatedSql(Context context);

    default Predicate or(Predicate other) {
        return CompoundPredicates.or(this, other);
    }

    default OptionalPredicate or(OptionalPredicate maybeOther) {
        if (maybeOther.isPresent()) {
            return OptionalPredicate.of(CompoundPredicates.or(this, maybeOther.get()));
        } else {
            return OptionalPredicate.of(this);
        }
    }

    default Predicate and(Predicate other) {
        return CompoundPredicates.and(this, other);
    }

    default OptionalPredicate and(OptionalPredicate maybeOther) {
        if (maybeOther.isPresent()) {
            return OptionalPredicate.of(CompoundPredicates.and(this, maybeOther.get()));
        } else {
            return OptionalPredicate.of(this);
        }
    }
}
