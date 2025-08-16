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
package io.github.torand.fastersql.statement.access;

import io.github.torand.fastersql.statement.PreparableStatement;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.github.torand.fastersql.datamodel.DataModel.PRODUCT;
import static io.github.torand.fastersql.function.singlerow.SingleRowFunctions.length;
import static io.github.torand.fastersql.statement.Statements.delete;
import static io.github.torand.fastersql.statement.Statements.select;

class AccessDeleteStatementTest extends AccessTest {

    @Test
    void shouldRemoveDeletedRow() {
        final UUID id = UUID.fromString("dba9f942-c24f-4b6a-89b6-881236ff5438"); // Apple iPad

        PreparableStatement stmt =
            delete().from(PRODUCT)
                .where(PRODUCT.ID.eq(id));

        statementTester()
            .assertSql("""
                delete from PRODUCT \
                where ID = ?"""
            )
            .assertParams(id)
            .assertAffectedRowCount(1)
            .verify(stmt);

        statementTester()
            .assertRowCount(0)
            .verify(
                select(PRODUCT.ID)
                    .from(PRODUCT)
                    .where(PRODUCT.ID.eq(id))
            );
    }

    @Test
    void shouldHandleOptionalPredicates() {
        final UUID id = UUID.fromString("7a4b3e96-afee-4284-8ccd-f7461bcd602b"); // Samsung Galaxy

        PreparableStatement stmt =
            delete().from(PRODUCT)
                .where(PRODUCT.ID.eq(id))
                .whereIf(true, () -> length(PRODUCT.NAME).gt(10));

        statementTester()
            .assertSql("""
                delete from PRODUCT \
                where ID = ? \
                and len(NAME) > ?"""
            )
            .assertParams(id, 10)
            .assertAffectedRowCount(1)
            .verify(stmt);

        statementTester()
            .assertRowCount(0)
            .verify(
                select(PRODUCT.ID)
                    .from(PRODUCT)
                    .where(PRODUCT.ID.eq(id))
            );
    }
}
