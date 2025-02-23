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
package io.github.torand.fastersql.statement.postgresql;

import io.github.torand.fastersql.statement.PreparableStatement;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static io.github.torand.fastersql.constant.Constants.$;
import static io.github.torand.fastersql.datamodel.DataModel.PRODUCT;
import static io.github.torand.fastersql.datamodel.DataModel.PURCHASE;
import static io.github.torand.fastersql.function.singlerow.SingleRowFunctions.concat;
import static io.github.torand.fastersql.statement.Statements.select;
import static io.github.torand.fastersql.statement.Statements.update;
import static io.github.torand.fastersql.util.RowValueMatchers.isInteger;
import static io.github.torand.fastersql.util.RowValueMatchers.isNull;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;

public class PostgreSqlUpdateStatementTest extends PostgreSqlTest {

    @Test
    void shouldRetrieveUpdatedRow() {
        final UUID id = UUID.fromString("92bfca8e-2898-408c-8dd3-2b3f9d362044"); // Louis Poulsen

        PreparableStatement stmt =
            update(PRODUCT)
                .set(PRODUCT.DESCRIPTION, "Fresh from the factory")
                .set(PRODUCT.STOCK_COUNT, 42)
                .where(PRODUCT.ID.eq(id));

        statementTester()
            .assertSql("""
                update PRODUCT \
                set DESCRIPTION = ?, STOCK_COUNT = ? \
                where ID = ?"""
            )
            .assertParams("Fresh from the factory", 42, id)
            .assertAffectedRowCount(1)
            .verify(stmt);

        statementTester()
            .assertRowCount(1)
            .assertRow(1,
                "PR_ID", is(id.toString()),
                "PR_DESCRIPTION", is("Fresh from the factory"),
                "PR_STOCK_COUNT", isInteger(42)
            )
            .verify(
                select(PRODUCT.ID, PRODUCT.DESCRIPTION, PRODUCT.STOCK_COUNT)
                    .from(PRODUCT)
                    .where(PRODUCT.ID.eq(id))
            );
    }

    @Test
    void shouldHandleExpressionAsValue() {
        final UUID customerId = UUID.fromString("9df03cd1-245f-4257-95e2-85cb5bd39ad8"); // Ola Nordmann

        PreparableStatement stmt =
            update(PURCHASE)
                .set(PURCHASE.NOTES, concat(PURCHASE.NOTES, $(" - put on hold"), $(" (for a while)")))
                .where(PURCHASE.CUSTOMER_ID.eq(customerId));

        statementTester()
            .assertSql("""
                update PURCHASE \
                set NOTES = NOTES || ? || ? \
                where CUSTOMER_ID = ?"""
            )
            .assertParams(" - put on hold", " (for a while)", customerId)
            .assertAffectedRowCount(1)
            .verify(stmt);

        statementTester()
            .assertRowCount(1)
            .assertRow(1,
                "PU_NOTES", endsWith(" - put on hold (for a while)")
            )
            .verify(
                select(PURCHASE.NOTES)
                    .from(PURCHASE)
                    .where(PURCHASE.CUSTOMER_ID.eq(customerId))
            );
    }

    @Test
    void shouldHandleNullAsValue() {
        final UUID id = UUID.fromString("92bfca8e-2898-408c-8dd3-2b3f9d362044"); // Louis Poulsen

        PreparableStatement stmt =
            update(PRODUCT)
                .set(PRODUCT.DESCRIPTION, $(null))
                .where(PRODUCT.ID.eq(id));

        statementTester()
            .assertSql("""
                update PRODUCT \
                set DESCRIPTION = null \
                where ID = ?"""
            )
            .assertParams(id)
            .assertAffectedRowCount(1)
            .verify(stmt);

        statementTester()
            .assertRowCount(1)
            .assertRow(1,
                "PR_DESCRIPTION", isNull()
            )
            .verify(
                select(PRODUCT.DESCRIPTION)
                    .from(PRODUCT)
                    .where(PRODUCT.ID.eq(id))
            );
    }

    @Test
    void shouldHandleOptionalValues() {
        final UUID id = UUID.fromString("92bfca8e-2898-408c-8dd3-2b3f9d362044"); // Louis Poulsen

        PreparableStatement stmt =
            update(PRODUCT)
                .set(PRODUCT.NAME, Optional.empty())
                .set(PRODUCT.STOCK_COUNT, Optional.of(123))
                .where(PRODUCT.ID.eq(id));

        statementTester()
            .assertSql("""
                update PRODUCT \
                set STOCK_COUNT = ? \
                where ID = ?"""
            )
            .assertParams(123, id)
            .assertAffectedRowCount(1)
            .verify(stmt);

        statementTester()
            .assertRowCount(1)
            .assertRow(1,
                "PR_ID", is(id.toString()),
                "PR_NAME", allOf(startsWith("Louis Poulsen"), endsWith("table lamp")),
                "PR_STOCK_COUNT", isInteger(123)
            )
            .verify(
                select(PRODUCT.ID, PRODUCT.NAME, PRODUCT.STOCK_COUNT)
                    .from(PRODUCT)
                    .where(PRODUCT.ID.eq(id))
            );
    }
}
