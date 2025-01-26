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
package io.github.torand.fastersql.predicate.compound;

import io.github.torand.fastersql.Context;
import io.github.torand.fastersql.Field;
import io.github.torand.fastersql.predicate.Predicate;

import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

public class Not implements Predicate {
    private final Predicate operand;

    Not(Predicate operand) {
        this.operand = requireNonNull(operand, "No operand specified");
    }

    // Sql

    @Override
    public String sql(Context context) {
        return operand.negatedSql(context);
    }

    @Override
    public Stream<Object> params(Context context) {
        return operand.params(context);
    }

    // Predicate

    @Override
    public String negatedSql(Context context) {
        return operand.sql(context);
    }

    @Override
    public Stream<Field> fieldRefs() {
        return operand.fieldRefs();
    }
}
