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
package io.github.torand.fastersql.statement.mysql;

import io.github.torand.fastersql.domainmodel.Product;
import io.github.torand.fastersql.domainmodel.ProductCategory;
import io.github.torand.fastersql.statement.PreparableStatement;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.UUID;

import static io.github.torand.fastersql.datamodel.DataModel.PRODUCT;
import static io.github.torand.fastersql.statement.Statements.insertBatch;
import static io.github.torand.fastersql.statement.Statements.select;
import static io.github.torand.fastersql.util.RowValueMatchers.isNull;
import static io.github.torand.fastersql.util.RowValueMatchers.isNumber;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.is;

public class MySqlInsertBatchStatementTest extends MySqlTest {

    @Test
    void shouldRetrieveInsertedRows() {
        final UUID id1 = UUID.randomUUID(), id2 = UUID.randomUUID(), id3 = UUID.randomUUID();

        Collection<Product> products = asList(
            new Product(id1, "IKEA Billy bookshelf", "TBD", ProductCategory.FURNITURE, 1234.56, 46),
            new Product(id2, "Siemens IQ500 dishwasher", null, ProductCategory.APPLIANCE, 4567.89, 34),
            new Product(id3, "HP Elitebook 830 laptop", "Power and portability", ProductCategory.ELECTRONICS, 9012.34, 9)
        );

        PreparableStatement stmt =
            insertBatch(products).into(PRODUCT)
                .value(PRODUCT.ID, Product::id)
                .value(PRODUCT.NAME, Product::name)
                .value(PRODUCT.DESCRIPTION, Product::description)
                .value(PRODUCT.CATEGORY, Product::category)
                .value(PRODUCT.PRICE, Product::price)
                .value(PRODUCT.STOCK_COUNT, Product::stock_count);

        statementTester()
            .assertSql("""
                insert into PRODUCT (ID, NAME, DESCRIPTION, CATEGORY, PRICE, STOCK_COUNT) \
                values (?, ?, ?, ?, ?, ?), (?, ?, null, ?, ?, ?), (?, ?, ?, ?, ?, ?)"""
            )
            .assertAffectedRowCount(3)
            .verify(stmt);

        statementTester()
            .assertRowCount(3)
            .assertRow(1,
                "PR_ID", is(id1.toString()),
                "PR_NAME", is("IKEA Billy bookshelf"),
                "PR_DESCRIPTION", is("TBD"),
                "PR_CATEGORY", is("FURNITURE"),
                "PR_PRICE", isNumber(1234.56),
                "PR_STOCK_COUNT", isNumber(46)
            )
            .assertRow(2,
                "PR_ID", is(id2.toString()),
                "PR_NAME", is("Siemens IQ500 dishwasher"),
                "PR_DESCRIPTION", isNull(),
                "PR_CATEGORY", is("APPLIANCE"),
                "PR_PRICE", isNumber(4567.89),
                "PR_STOCK_COUNT", isNumber(34)
            )
            .assertRow(3,
                "PR_ID", is(id3.toString()),
                "PR_NAME", is("HP Elitebook 830 laptop"),
                "PR_DESCRIPTION", is("Power and portability"),
                "PR_CATEGORY", is("ELECTRONICS"),
                "PR_PRICE", isNumber(9012.34),
                "PR_STOCK_COUNT", isNumber(9)
            )
            .verify(
                select(PRODUCT.ID, PRODUCT.NAME, PRODUCT.DESCRIPTION, PRODUCT.CATEGORY, PRODUCT.PRICE, PRODUCT.STOCK_COUNT)
                    .from(PRODUCT)
                    .where(PRODUCT.ID.in(id1, id2, id3))
                    .orderBy(PRODUCT.PRICE.asc())
            );
    }
}
