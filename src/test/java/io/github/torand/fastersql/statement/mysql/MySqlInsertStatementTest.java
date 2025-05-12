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
package io.github.torand.fastersql.statement.mysql;

import io.github.torand.fastersql.domainmodel.ProductCategory;
import io.github.torand.fastersql.domainmodel.PurchaseStatus;
import io.github.torand.fastersql.statement.PreparableStatement;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.util.Optional;
import java.util.UUID;

import static io.github.torand.fastersql.constant.Constants.$;
import static io.github.torand.fastersql.datamodel.DataModel.PRODUCT;
import static io.github.torand.fastersql.datamodel.DataModel.PURCHASE;
import static io.github.torand.fastersql.function.singlerow.SingleRowFunctions.concat;
import static io.github.torand.fastersql.function.system.SystemFunctions.currentTimestamp;
import static io.github.torand.fastersql.statement.Statements.insert;
import static io.github.torand.fastersql.statement.Statements.select;
import static io.github.torand.fastersql.util.RowValueMatchers.isNull;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;

public class MySqlInsertStatementTest extends MySqlTest {

    @Test
    void shouldRetrieveInsertedRow() {
        final UUID id = UUID.randomUUID();

        PreparableStatement stmt =
            insert().into(PRODUCT)
                .value(PRODUCT.ID, id)
                .value(PRODUCT.NAME, "Apple MacBook Pro")
                .value(PRODUCT.CATEGORY, ProductCategory.ELECTRONICS)
                .value(PRODUCT.DESCRIPTION, "TBD")
                .value(PRODUCT.PRICE, 24999)
                .value(PRODUCT.STOCK_COUNT, 7);

        statementTester()
            .assertSql("""
                insert into PRODUCT (ID, NAME, CATEGORY, DESCRIPTION, PRICE, STOCK_COUNT) \
                values (?, ?, ?, ?, ?, ?)"""
            )
            .assertParams(id, "Apple MacBook Pro", ProductCategory.ELECTRONICS, "TBD", 24999, 7)
            .assertAffectedRowCount(1)
            .verify(stmt);

        statementTester()
            .assertRowCount(1)
            .assertRow(1,
                "PR_ID", is(id.toString()),
                "PR_NAME", is("Apple MacBook Pro"))
            .verify(
                select(PRODUCT.ID, PRODUCT.NAME)
                    .from(PRODUCT)
                    .where(PRODUCT.ID.eq(id))
            );
    }

    @Test
    void shouldHandleExpressionAsValue() {
        final UUID id = UUID.randomUUID();
        final UUID customerId = UUID.fromString("9df03cd1-245f-4257-95e2-85cb5bd39ad8"); // Ola Nordmann
        final Year year = Year.now();

        PreparableStatement stmt =
            insert().into(PURCHASE)
                .value(PURCHASE.ID, id)
                .value(PURCHASE.STATUS, PurchaseStatus.REGISTERED)
                .value(PURCHASE.CUSTOMER_ID, customerId)
                .value(PURCHASE.NOTES, concat(currentTimestamp(), $(" ")))
                .value(PURCHASE.CREATED_TIME, currentTimestamp());

        statementTester()
            .assertSql("""
                insert into PURCHASE (ID, STATUS, CUSTOMER_ID, NOTES, CREATED_TIME) \
                values (?, ?, ?, concat(current_timestamp, ?), current_timestamp)"""
            )
            .assertParams(id, PurchaseStatus.REGISTERED, customerId, " ")
            .assertAffectedRowCount(1)
            .verify(stmt);

        statementTester()
            .assertRowCount(1)
            .assertRow(1,
                "PU_ID", is(id.toString()),
                "PU_NOTES", startsWith(year.toString()),
                "PU_CREATED_YEAR", startsWith(year.toString()))
            .verify(
                select(PURCHASE.ID, PURCHASE.NOTES, concat(PURCHASE.CREATED_TIME, $(" ")).as("PU_CREATED_YEAR"))
                    .from(PURCHASE)
                    .where(PURCHASE.ID.eq(id))
            );
    }

    @Test
    void shouldHandleNullAsValue() {
        final UUID id = UUID.randomUUID();

        PreparableStatement stmt =
            insert().into(PRODUCT)
                .value(PRODUCT.ID, id)
                .value(PRODUCT.NAME, "Apple MacBook Pro")
                .value(PRODUCT.CATEGORY, ProductCategory.ELECTRONICS)
                .value(PRODUCT.DESCRIPTION, $(null))
                .value(PRODUCT.PRICE, 24999)
                .value(PRODUCT.STOCK_COUNT, 7);

        statementTester()
            .assertSql("""
                insert into PRODUCT (ID, NAME, CATEGORY, DESCRIPTION, PRICE, STOCK_COUNT) \
                values (?, ?, ?, null, ?, ?)"""
            )
            .assertParams(id, "Apple MacBook Pro", ProductCategory.ELECTRONICS, 24999, 7)
            .assertAffectedRowCount(1)
            .verify(stmt);

        statementTester()
            .assertRowCount(1)
            .assertRow(1,
                "PR_ID", is(id.toString()),
                "PR_NAME", is("Apple MacBook Pro"),
                "PR_DESCRIPTION", isNull())
            .verify(
                select(PRODUCT.ID, PRODUCT.NAME, PRODUCT.DESCRIPTION)
                    .from(PRODUCT)
                    .where(PRODUCT.ID.eq(id))
            );
    }

    @Test
    void shouldHandleOptionalValues() {
        final UUID id = UUID.randomUUID();

        PreparableStatement stmt =
            insert().into(PRODUCT)
                .value(PRODUCT.ID, id)
                .value(PRODUCT.NAME, "Apple MacBook Pro")
                .value(PRODUCT.CATEGORY, ProductCategory.ELECTRONICS)
                .value(PRODUCT.DESCRIPTION, Optional.empty())
                .value(PRODUCT.PRICE, 24999)
                .value(PRODUCT.STOCK_COUNT, 7);

        statementTester()
            .assertSql("""
                insert into PRODUCT (ID, NAME, CATEGORY, PRICE, STOCK_COUNT) \
                values (?, ?, ?, ?, ?)"""
            )
            .assertParams(id, "Apple MacBook Pro", ProductCategory.ELECTRONICS, 24999, 7)
            .assertAffectedRowCount(1)
            .verify(stmt);

        statementTester()
            .assertRowCount(1)
            .assertRow(1,
                "PR_ID", is(id.toString()),
                "PR_NAME", is("Apple MacBook Pro"),
                "PR_DESCRIPTION", isNull())
            .verify(
                select(PRODUCT.ID, PRODUCT.NAME, PRODUCT.DESCRIPTION)
                    .from(PRODUCT)
                    .where(PRODUCT.ID.eq(id))
            );
    }
}
