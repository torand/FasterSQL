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
package io.github.torand.fastersql.statement.oracle;

import io.github.torand.fastersql.statement.PreparableStatement;
import org.junit.jupiter.api.Test;

import static io.github.torand.fastersql.datamodel.DataModel.PURCHASE_ITEM;
import static io.github.torand.fastersql.function.aggregate.AggregateFunctions.count;
import static io.github.torand.fastersql.statement.Statements.select;
import static io.github.torand.fastersql.statement.Statements.truncate;
import static io.github.torand.fastersql.util.RowValueMatchers.isBigDecimal;

class OracleTruncateStatementTest extends OracleTest {

    @Test
    void shouldRemoveAllRowsFromTuncatedTable() {
        PreparableStatement stmt =
            truncate().table(PURCHASE_ITEM);

        statementTester()
            .assertSql("""
                truncate table PURCHASE_ITEM"""
            )
            .verify(stmt);

        statementTester()
            .assertRowCount(1)
            .assertRow(1, "NUM_ITEMS", isBigDecimal(0))
            .verify(
                select(count().as("NUM_ITEMS"))
                    .from(PURCHASE_ITEM)
            );
    }
}
