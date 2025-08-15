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

/**
 * Provides factory methods for CASE expressions.
 */
public final class CaseExpressions {
    private CaseExpressions() {}

    /**
     * Creates a simple CASE expression.
     * @param caseExpression the CASE expression.
     * @return the simple CASE builder.
     */
    public static SimpleCaseBuilder case_(Expression caseExpression) {
        return new SimpleCaseBuilder(caseExpression);
    }

    /**
     * Creates a searched CASE expression.
     * @return the searched CASE builder.
     */
    public static SearchedCaseBuilder case_() {
        return new SearchedCaseBuilder();
    }
}
