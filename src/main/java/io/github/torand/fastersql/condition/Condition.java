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
package io.github.torand.fastersql.condition;

import io.github.torand.fastersql.Context;
import io.github.torand.fastersql.Field;
import io.github.torand.fastersql.Sql;
import io.github.torand.fastersql.condition.logical.LogicalConditions;

import java.util.stream.Stream;

/**
 * Represents a restriction on the rows fetched by a query or affected by an update or delete.
 */
public interface Condition extends Sql {
    String negatedSql(Context context);

    Stream<Field> fieldRefs();

    default Condition or(Condition other) {
        return LogicalConditions.or(this, other);
    }

    default OptionalCondition or(OptionalCondition maybeOther) {
        if (maybeOther.isPresent()) {
            return OptionalCondition.of(LogicalConditions.or(this, maybeOther.get()));
        } else {
            return OptionalCondition.of(this);
        }
    }

    default Condition and(Condition other) {
        return LogicalConditions.and(this, other);
    }

    default OptionalCondition and(OptionalCondition maybeOther) {
        if (maybeOther.isPresent()) {
            return OptionalCondition.of(LogicalConditions.and(this, maybeOther.get()));
        } else {
            return OptionalCondition.of(this);
        }
    }
}
