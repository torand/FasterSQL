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

import java.util.ArrayList;
import java.util.List;

import static io.github.torand.fastersql.constant.Constants.$;
import static java.util.Objects.requireNonNull;

/**
 * Builder of simple CASE expressions.
 */
public class SimpleCaseBuilder {
    private final Expression caseExpression;
    private final List<SimpleWhenThen> whenThenClauses = new ArrayList<>();
    private Expression elseExpression;

    SimpleCaseBuilder(Expression caseExpression) {
        this.caseExpression = requireNonNull(caseExpression, "No case expression specified");
    }

    /**
     * Creates a WHEN-THEN clause.
     * @param whenExpression the WHEN expression.
     * @return the WHEN-THEN builder.
     */
    public SimpleWhenThenBuilder when(Expression whenExpression) {
        requireNonNull(whenExpression, "No when expression specified");
        return new SimpleWhenThenBuilder(this, whenExpression);
    }

   /**
     * Creates a WHEN-THEN clause.
     * @param whenConstant the WHEN constant value.
     * @return the WHEN-THEN builder.
     */
    public SimpleWhenThenBuilder when(String whenConstant) {
        requireNonNull(whenConstant, "No when constant specified");
        return when($(whenConstant));
    }

   /**
     * Creates a WHEN-THEN clause.
     * @param whenConstant the WHEN constant value.
     * @return the WHEN-THEN builder.
     */
    public SimpleWhenThenBuilder when(Number whenConstant) {
        requireNonNull(whenConstant, "No when constant specified");
        return when($(whenConstant));
    }

    /**
     * Adds an ELSE clause.
     * @param elseExpression the ELSE expression.
     * @return the modified CASE expression.
     */
    public SimpleCaseBuilder else_(Expression elseExpression) {
        this.elseExpression = requireNonNull(elseExpression, "No else expression specified");
        return this;
    }

    /**
     * Adds an ELSE clause.
     * @param elseConstant the ELSE constant value.
     * @return the modified CASE expression.
     */
    public SimpleCaseBuilder else_(String elseConstant) {
        this.elseExpression = $(requireNonNull(elseConstant, "No else constant specified"));
        return this;
    }

    /**
     * Adds an ELSE clause.
     * @param elseConstant the ELSE constant value.
     * @return the modified CASE expression.
     */
    public SimpleCaseBuilder else_(Number elseConstant) {
        this.elseExpression = $(requireNonNull(elseConstant, "No else constant specified"));
        return this;
    }

    /**
     * Creates the simple CASE expression.
     * @return the simple CASE expression.
     */
    public SimpleCase end() {
        return new SimpleCase(caseExpression, whenThenClauses, elseExpression, null);
    }

    void addWhenThenClause(SimpleWhenThen whenThenClause) {
        requireNonNull(whenThenClause, "No when-then clause specified");
        this.whenThenClauses.add(whenThenClause);
    }
}
