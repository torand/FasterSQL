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
package io.github.torand.fastersql.expression.cases;

import io.github.torand.fastersql.expression.Expression;

import static io.github.torand.fastersql.constant.Constants.$;
import static java.util.Objects.requireNonNull;

/**
 * Builder of a WHEN-THEN clause in a simple CASE expression.
 */
public class SimpleWhenThenBuilder {
    private final SimpleCaseBuilder caseBuilder;
    private final Expression whenExpression;

    SimpleWhenThenBuilder(SimpleCaseBuilder caseBuilder, Expression whenExpression) {
        this.caseBuilder = requireNonNull(caseBuilder, "No case builder specified");
        this.whenExpression = requireNonNull(whenExpression, "No when expression specified");
    }

    /**
     * Adds a WHEN-THEN clause.
     * @param thenExpression the THEN expression.
     * @return the modified CASE expression.
     */
    public SimpleCaseBuilder then(Expression thenExpression) {
        requireNonNull(thenExpression, "No then expression specified");
        caseBuilder.addWhenThenClause(new SimpleWhenThen(whenExpression, thenExpression));
        return caseBuilder;
    }

    /**
     * Adds a WHEN-THEN clause.
     * @param thenConstant the THEN constant value.
     * @return the modified CASE expression.
     */
    public SimpleCaseBuilder then(String thenConstant) {
        requireNonNull(thenConstant, "No then constant specified");
        return then($(thenConstant));
    }

    /**
     * Adds a WHEN-THEN clause.
     * @param thenConstant the THEN constant value.
     * @return the modified CASE expression.
     */
    public SimpleCaseBuilder then(Number thenConstant) {
        requireNonNull(thenConstant, "No then constant specified");
        return then($(thenConstant));
    }
}
