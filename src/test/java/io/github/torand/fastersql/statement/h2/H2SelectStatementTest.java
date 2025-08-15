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
package io.github.torand.fastersql.statement.h2;

import io.github.torand.fastersql.datamodel.CustomerTable;
import io.github.torand.fastersql.statement.PreparableStatement;
import io.github.torand.fastersql.statement.SelectSetOpStatement;
import io.github.torand.fastersql.statement.SelectStatement;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static io.github.torand.fastersql.alias.Aliases.alias;
import static io.github.torand.fastersql.alias.Aliases.colRef;
import static io.github.torand.fastersql.constant.Constants.$;
import static io.github.torand.fastersql.constant.Constants.$i;
import static io.github.torand.fastersql.constant.Constants.nullValue;
import static io.github.torand.fastersql.datamodel.DataModel.CUSTOMER;
import static io.github.torand.fastersql.datamodel.DataModel.PRODUCT;
import static io.github.torand.fastersql.datamodel.DataModel.PURCHASE;
import static io.github.torand.fastersql.datamodel.DataModel.PURCHASE_ITEM;
import static io.github.torand.fastersql.expression.arithmetic.ArithmeticExpressions.neg;
import static io.github.torand.fastersql.expression.cases.CaseExpressions.case_;
import static io.github.torand.fastersql.function.aggregate.AggregateFunctions.count;
import static io.github.torand.fastersql.function.aggregate.AggregateFunctions.max;
import static io.github.torand.fastersql.function.aggregate.AggregateFunctions.sum;
import static io.github.torand.fastersql.function.singlerow.SingleRowFunctions.abs;
import static io.github.torand.fastersql.function.singlerow.SingleRowFunctions.cast;
import static io.github.torand.fastersql.function.singlerow.SingleRowFunctions.ceil;
import static io.github.torand.fastersql.function.singlerow.SingleRowFunctions.exp;
import static io.github.torand.fastersql.function.singlerow.SingleRowFunctions.floor;
import static io.github.torand.fastersql.function.singlerow.SingleRowFunctions.ln;
import static io.github.torand.fastersql.function.singlerow.SingleRowFunctions.lower;
import static io.github.torand.fastersql.function.singlerow.SingleRowFunctions.pow;
import static io.github.torand.fastersql.function.singlerow.SingleRowFunctions.round;
import static io.github.torand.fastersql.function.singlerow.SingleRowFunctions.sqrt;
import static io.github.torand.fastersql.function.singlerow.SingleRowFunctions.substring;
import static io.github.torand.fastersql.function.singlerow.SingleRowFunctions.toNumber;
import static io.github.torand.fastersql.function.singlerow.SingleRowFunctions.upper;
import static io.github.torand.fastersql.function.singlerow.cast.DataTypes.decimal;
import static io.github.torand.fastersql.function.singlerow.cast.DataTypes.varchar;
import static io.github.torand.fastersql.function.system.SystemFunctions.currentDate;
import static io.github.torand.fastersql.function.system.SystemFunctions.currentTime;
import static io.github.torand.fastersql.function.system.SystemFunctions.currentTimestamp;
import static io.github.torand.fastersql.projection.Projections.colPos;
import static io.github.torand.fastersql.projection.Projections.subquery;
import static io.github.torand.fastersql.relation.Relations.table;
import static io.github.torand.fastersql.statement.Statements.select;
import static io.github.torand.fastersql.statement.Statements.selectDistinct;
import static io.github.torand.fastersql.util.RowValueMatchers.isBigDecimal;
import static io.github.torand.fastersql.util.RowValueMatchers.isBigDecimalCloseTo;
import static io.github.torand.fastersql.util.RowValueMatchers.isDouble;
import static io.github.torand.fastersql.util.RowValueMatchers.isDoubleCloseTo;
import static io.github.torand.fastersql.util.RowValueMatchers.isInteger;
import static io.github.torand.fastersql.util.RowValueMatchers.isLong;
import static io.github.torand.fastersql.util.RowValueMatchers.isNull;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;

public class H2SelectStatementTest extends H2Test {

    @Test
    void shouldHandleComplexQuery() {
        LocalDateTime since = LocalDateTime.of(2020, 1, 1, 0, 0);

        SelectStatement stmt =
            select(CUSTOMER.LAST_NAME, CUSTOMER.FIRST_NAME, PRODUCT.NAME, PURCHASE_ITEM.QUANTITY, PURCHASE.STATUS)
                .from(CUSTOMER)
                .join(CUSTOMER.ID.on(PURCHASE.CUSTOMER_ID))
                .join(PURCHASE.ID.on(PURCHASE_ITEM.PURCHASE_ID))
                .join(PURCHASE_ITEM.PRODUCT_ID.on(PRODUCT.ID))
                .where(CUSTOMER.MOBILE_NO_VERIFIED.eq(true)
                    .and(CUSTOMER.EMAIL_ADDRESS.isNotNull())
                    .and(CUSTOMER.COUNTRY_CODE.in("NOR", "DEN"))
                    .and(PRODUCT.CATEGORY.eq("FURNITURE").or(PRODUCT.CATEGORY.eq("LAMP")))
                    .and(PURCHASE.CREATED_TIME.gt(since)))
                .orderBy(CUSTOMER.LAST_NAME.asc(), PRODUCT.NAME.desc())
                .limit(5)
                .offset(0);

        statementTester()
            .assertSql("""
                select C.LAST_NAME C_LAST_NAME, C.FIRST_NAME C_FIRST_NAME, PR.NAME PR_NAME, PI.QUANTITY PI_QUANTITY, PU.STATUS PU_STATUS \
                from CUSTOMER C \
                inner join PURCHASE PU on C.ID = PU.CUSTOMER_ID \
                inner join PURCHASE_ITEM PI on PU.ID = PI.PURCHASE_ID \
                inner join PRODUCT PR on PI.PRODUCT_ID = PR.ID \
                where C.MOBILE_NO_VERIFIED = ? \
                and C.EMAIL_ADDRESS is not null \
                and C.COUNTRY_CODE in (?, ?) \
                and (PR.CATEGORY = ? or PR.CATEGORY = ?) \
                and PU.CREATED_TIME > ? \
                order by C.LAST_NAME asc, PR.NAME desc \
                limit ? \
                offset ?"""
            )
            .assertParams(true, "NOR", "DEN", "FURNITURE", "LAMP", since, 5L, 0L)
            .assertRowCount(2)
            .assertRow(1,
                "C_LAST_NAME", is("Hansen"),
                "PR_NAME", is("Louis Poulsen Panthella 160 table lamp"),
                "PI_QUANTITY", isInteger(3))
            .assertRow(2,
                "C_LAST_NAME", is("Nordmann"),
                "PR_NAME", is("Ekornes Stressless resting chair"),
                "PI_QUANTITY", isInteger(1))
            .verify(stmt);
    }

    @Test
    void shouldHandleOptionalPredicate() {
        Optional<String> maybeFirstName = Optional.of("Ola");
        Optional<String> maybeZipCode = Optional.empty();

        SelectStatement stmt =
            select(CUSTOMER.LAST_NAME, CUSTOMER.FIRST_NAME)
                .from(CUSTOMER)
                .where(CUSTOMER.FIRST_NAME.eq(maybeFirstName), CUSTOMER.ZIP_CODE.eq(maybeZipCode));

        statementTester()
            .assertSql("""
                select C.LAST_NAME C_LAST_NAME, C.FIRST_NAME C_FIRST_NAME \
                from CUSTOMER C \
                where C.FIRST_NAME = ?"""
            )
            .assertParams("Ola")
            .assertRowCount(1)
            .verify(stmt);
    }

    @Test
    void shouldHandleExpressionsInProjection() {
        SelectStatement stmt =
            select(upper(CUSTOMER.LAST_NAME).as("C_LAST_NAME"), lower(CUSTOMER.FIRST_NAME).as("C_FIRST_NAME"), PRODUCT.PRICE.times(PURCHASE_ITEM.QUANTITY).as("TOTAL"), nullValue().forColumn(CUSTOMER.ZIP_CODE), $(3.14).as("PI"))
                .from(CUSTOMER)
                .join(CUSTOMER.ID.on(PURCHASE.CUSTOMER_ID))
                .join(PURCHASE.ID.on(PURCHASE_ITEM.PURCHASE_ID))
                .join(PURCHASE_ITEM.PRODUCT_ID.on(PRODUCT.ID))
                .where(CUSTOMER.COUNTRY_CODE.in("NOR", "DEN"))
                .orderBy(CUSTOMER.LAST_NAME.asc());

        statementTester()
            .assertSql("""
                select upper(C.LAST_NAME) C_LAST_NAME, lower(C.FIRST_NAME) C_FIRST_NAME, PR.PRICE * PI.QUANTITY TOTAL, null C_ZIP_CODE, ? PI \
                from CUSTOMER C \
                inner join PURCHASE PU on C.ID = PU.CUSTOMER_ID \
                inner join PURCHASE_ITEM PI on PU.ID = PI.PURCHASE_ID \
                inner join PRODUCT PR on PI.PRODUCT_ID = PR.ID \
                where C.COUNTRY_CODE in (?, ?) \
                order by C.LAST_NAME asc"""
            )
            .assertParams(3.14, "NOR", "DEN")
            .assertRowCount(2)
            .assertRow(1,
                "C_LAST_NAME", is("HANSEN"),
                "C_FIRST_NAME", is("jens"),
                "TOTAL", isBigDecimalCloseTo(11335.35, 0.01),
                "C_ZIP_CODE", isNull(),
                "PI", isDouble(3.14)
            )
            .assertRow(2,
                "C_LAST_NAME", is("NORDMANN"),
                "C_FIRST_NAME", is("ola"),
                "TOTAL", isBigDecimalCloseTo(5433.50, 0.01),
                "C_ZIP_CODE", isNull(),
                "PI", isDouble(3.14)
            )
            .verify(stmt);
    }

    @Test
    void shouldHandleExpressionsInPredicate() {
        SelectStatement stmt =
            select(CUSTOMER.LAST_NAME, CUSTOMER.FIRST_NAME)
                .from(CUSTOMER)
                .join(CUSTOMER.ID.on(PURCHASE.CUSTOMER_ID))
                .join(PURCHASE.ID.on(PURCHASE_ITEM.PURCHASE_ID))
                .join(PURCHASE_ITEM.PRODUCT_ID.on(PRODUCT.ID))
                .where(PRODUCT.PRICE.gt($(5).times($(9).plus(toNumber($("11"), 2))))
                    .and(CUSTOMER.COUNTRY_CODE.eq(upper($("nor"))).or(CUSTOMER.COUNTRY_CODE.eq(substring($("DENMARK"), 1,3))))
                    .and(PRODUCT.PRICE.between(3000, 6000))
                )
                .orderBy(CUSTOMER.LAST_NAME.asc());

        statementTester()
            .assertSql("""
                select C.LAST_NAME C_LAST_NAME, C.FIRST_NAME C_FIRST_NAME \
                from CUSTOMER C \
                inner join PURCHASE PU on C.ID = PU.CUSTOMER_ID \
                inner join PURCHASE_ITEM PI on PU.ID = PI.PURCHASE_ID \
                inner join PRODUCT PR on PI.PRODUCT_ID = PR.ID \
                where PR.PRICE > ? * (? + to_number(?)) \
                and (C.COUNTRY_CODE = upper(?) or C.COUNTRY_CODE = substring(?, 1, 3)) \
                and PR.PRICE between ? and ? \
                order by C.LAST_NAME asc"""
            )
            .assertParams(5, 9, "11", "nor", "DENMARK", 3000, 6000)
            .assertRowCount(2)
            .assertRow(1,
                "C_LAST_NAME", is("Hansen"),
                "C_FIRST_NAME", is("Jens")
            )
            .assertRow(2,
                "C_LAST_NAME", is("Nordmann"),
                "C_FIRST_NAME", is("Ola")
            )
            .verify(stmt);
    }

    @Test
    public void shouldHandleOptionalWhereClauses() {
        Optional<String> maybeFirstName = Optional.of("Ola");
        Optional<String> maybeCountryCode = Optional.empty();

        PreparableStatement stmt =
            select(CUSTOMER.LAST_NAME)
                .from(CUSTOMER)
                .where(CUSTOMER.FIRST_NAME.eq(maybeFirstName), CUSTOMER.COUNTRY_CODE.eq(maybeCountryCode)
                    .and(CUSTOMER.LAST_NAME.ne("Doe"))
                );

        statementTester()
            .assertSql("""
                select C.LAST_NAME C_LAST_NAME \
                from CUSTOMER C \
                where C.FIRST_NAME = ? \
                and C.LAST_NAME <> ?"""
            )
            .assertParams("Ola", "Doe")
            .assertRowCount(1)
            .assertRow(1,
                "C_LAST_NAME", is("Nordmann")
            )
            .verify(stmt);
    }

    @Test
    void shouldHandleRightOuterJoin() {
        SelectStatement stmt =
            select(PURCHASE_ITEM.ID, PRODUCT.NAME)
                .from(PURCHASE_ITEM)
                .rightOuterJoin(PURCHASE_ITEM.PRODUCT_ID.on(PRODUCT.ID))
                .orderBy(PRODUCT.NAME.asc());

        statementTester()
            .assertSql("""
                select PI.ID PI_ID, PR.NAME PR_NAME \
                from PURCHASE_ITEM PI \
                right outer join PRODUCT PR on PI.PRODUCT_ID = PR.ID \
                order by PR.NAME asc"""
            )
            .assertRowCount(5)
            .assertRow(1,
                "PI_ID", isNull(),
                "PR_NAME", is("Apple iPad Pro tablet")
            )
            .assertRow(2,
                "PI_ID", is("6d35986a-04ee-4ba0-a3f6-d421e98139db"),
                "PR_NAME", is("Ekornes Stressless resting chair")
            )
            .assertRow(3,
                "PI_ID", isNull(),
                "PR_NAME", is("Electrolux 800 UltraCare washing machine")
            )
            .assertRow(4,
                "PI_ID", is("a6fd519a-3baf-4892-b435-d11f7a080385"),
                "PR_NAME", is("Louis Poulsen Panthella 160 table lamp")
            )
            .assertRow(5,
                "PI_ID", isNull(),
                "PR_NAME", is("Samsung Galaxy S25 Ultra mobile phone")
            )
            .verify(stmt);
    }

    @Test
    public void shouldHandleSubqueriesInProjection() {
        PreparableStatement stmt =
            select(CUSTOMER.LAST_NAME, subquery(select(count()).from(PURCHASE).where(PURCHASE.CUSTOMER_ID.eq(CUSTOMER.ID))).as("PURCHASE_COUNT"))
                .from(CUSTOMER)
                .orderBy(CUSTOMER.LAST_NAME.asc());

        statementTester()
            .assertSql("""
                select C.LAST_NAME C_LAST_NAME, (select count(*) COUNT_ALL from PURCHASE PU where PU.CUSTOMER_ID = C.ID) PURCHASE_COUNT \
                from CUSTOMER C \
                order by C.LAST_NAME asc"""
            )
            .assertRowCount(3)
            .assertRow(1,
                "C_LAST_NAME", is("Hansen"),
                "PURCHASE_COUNT", isLong(1)
            )
            .assertRow(2,
                "C_LAST_NAME", is("Nordmann"),
                "PURCHASE_COUNT", isLong(1)
            )
            .assertRow(3,
                "C_LAST_NAME", is("Svensson"),
                "PURCHASE_COUNT", isLong(0)
            )
            .verify(stmt);
    }

    @Test
    public void shouldHandleSubqueriesInPredicate() {
        final CustomerTable CUSTOMER2 = CUSTOMER.as("C2");

        PreparableStatement stmt =
            select(CUSTOMER.LAST_NAME)
                .from(CUSTOMER)
                .where(CUSTOMER.LAST_NAME.eq(
                    select(CUSTOMER2.LAST_NAME).from(CUSTOMER2).where(CUSTOMER2.COUNTRY_CODE.eq("NOR"))));

        statementTester()
            .assertSql("""
                select C.LAST_NAME C_LAST_NAME \
                from CUSTOMER C \
                where C.LAST_NAME = (select C2.LAST_NAME C2_LAST_NAME from CUSTOMER C2 where C2.COUNTRY_CODE = ?)"""
            )
            .assertParams("NOR")
            .assertRowCount(1)
            .assertRow(1,
                "C_LAST_NAME", is("Nordmann")
            )
            .verify(stmt);
    }

    @Test
    public void shouldHandleSimpleSubqueryInFromClause() {
        // Note! H2 does not support params in subquery projection

        PreparableStatement stmt =
            select(count().as("CUSTOMER_COUNT"))
                .from(table(
                    select(CUSTOMER.ID)
                        .from(CUSTOMER)
                        .where(CUSTOMER.LAST_NAME.like("ordm")))
                    .as("MATCHES"));

        statementTester()
            .assertSql("""
                select count(*) CUSTOMER_COUNT from (\
                select C.ID C_ID \
                from CUSTOMER C \
                where C.LAST_NAME like ?\
                ) MATCHES"""
            )
            .assertParams("%ordm%")
            .assertRowCount(1)
            .assertRow(1,
                "CUSTOMER_COUNT", isLong(1)
            )
            .verify(stmt);
    }

    @Test
    public void shouldHandleMultipleSubqueriesInFromClause() {
        PreparableStatement stmt =
            select(PRODUCT.ID, colRef("PURCHASED_QUANTITY", "QUANTITY"), colRef("PURCHASED_AMOUNT", "AMOUNT"))
                .from(PRODUCT, table(
                        select(PURCHASE_ITEM.PRODUCT_ID.as("PR_ID"), sum(PURCHASE_ITEM.QUANTITY).as("QUANTITY"))
                            .from(PURCHASE_ITEM)
                            .groupBy(PURCHASE_ITEM.PRODUCT_ID))
                        .as("PURCHASED_QUANTITY"),
                    table(
                        select(PURCHASE_ITEM.PRODUCT_ID.as("PR_ID"), sum(PURCHASE_ITEM.QUANTITY.times(PRODUCT.PRICE)).as("AMOUNT"))
                            .from(PURCHASE_ITEM)
                            .join(PURCHASE_ITEM.PRODUCT_ID.on(PRODUCT.ID))
                            .groupBy(PURCHASE_ITEM.PRODUCT_ID))
                        .as("PURCHASED_AMOUNT"))
                .where(PRODUCT.ID.eq(colRef("PURCHASED_QUANTITY", "PR_ID"))
                    .and(PRODUCT.ID.eq(colRef("PURCHASED_AMOUNT", "PR_ID"))))
                .orderBy(PRODUCT.ID.asc());

        statementTester()
            .assertSql("""
                select PR.ID PR_ID, PURCHASED_QUANTITY.QUANTITY, PURCHASED_AMOUNT.AMOUNT \
                from PRODUCT PR, \
                (select PI.PRODUCT_ID PR_ID, sum(PI.QUANTITY) QUANTITY from PURCHASE_ITEM PI group by PI.PRODUCT_ID) PURCHASED_QUANTITY, \
                (select PI.PRODUCT_ID PR_ID, sum(PI.QUANTITY * PR.PRICE) AMOUNT from PURCHASE_ITEM PI inner join PRODUCT PR on PI.PRODUCT_ID = PR.ID group by PI.PRODUCT_ID) PURCHASED_AMOUNT \
                where PR.ID = PURCHASED_QUANTITY.PR_ID and PR.ID = PURCHASED_AMOUNT.PR_ID \
                order by PR.ID asc"""
            )
            .assertRowCount(2)
            .assertRow(1,
                "PR_ID", is("92bfca8e-2898-408c-8dd3-2b3f9d362044"),
                "QUANTITY", isLong(3),
                "AMOUNT", isBigDecimalCloseTo(11335.35, 0.01)
            )
            .assertRow(2,
                "PR_ID", is("fdd89ce1-db38-4deb-9767-0324e91d4933"),
                "QUANTITY", isLong(1),
                "AMOUNT", isBigDecimalCloseTo(5433.5, 0.01)
            )
            .verify(stmt);
    }

    @Test
    public void shouldHandleAggregates() {
        PreparableStatement stmt =
            select(max(PRODUCT.PRICE).as("MAX_PRICE"))
                .from(PRODUCT)
                .where(PRODUCT.PRICE.lt(10000));

        statementTester()
            .assertSql("""
                select max(PR.PRICE) MAX_PRICE \
                from PRODUCT PR \
                where PR.PRICE < ?"""
            )
            .assertParams(10000)
            .assertRowCount(1)
            .assertRow(1,
                "MAX_PRICE", isBigDecimal(7122.09)
            )
            .verify(stmt);
    }

    @Test
    public void shouldHandleAggregatesWithGroupingAndOrdering() {
        PreparableStatement stmt =
            select(PRODUCT.NAME, sum(PRODUCT.PRICE.times(PURCHASE_ITEM.QUANTITY)).as("PURCHASED_VALUE"))
                .from(PRODUCT)
                .join(PRODUCT.ID.on(PURCHASE_ITEM.PRODUCT_ID))
                .groupBy(PRODUCT.NAME)
                .orderBy(alias("PURCHASED_VALUE").asc());

        statementTester()
            .assertSql("""
                select PR.NAME PR_NAME, sum(PR.PRICE * PI.QUANTITY) PURCHASED_VALUE \
                from PRODUCT PR \
                inner join PURCHASE_ITEM PI on PR.ID = PI.PRODUCT_ID \
                group by PR.NAME \
                order by PURCHASED_VALUE asc"""
            )
            .assertRowCount(2)
            .assertRow(1,
                "PR_NAME", containsString("Ekornes Stressless"),
                "PURCHASED_VALUE", isBigDecimalCloseTo(5433.5, 0.01)
            )
            .assertRow(2,
                "PR_NAME", containsString("Louis Poulsen"),
                "PURCHASED_VALUE", isBigDecimalCloseTo(11335.35, 0.01)
            )
            .verify(stmt);
    }

    @Test
    public void shouldHandleFilteredGroups() {
        PreparableStatement stmt =
            select(PRODUCT.NAME, sum(PRODUCT.PRICE.times(PURCHASE_ITEM.QUANTITY)).as("PURCHASED_VALUE"), max(PURCHASE_ITEM.QUANTITY).as("MAX_QNTY"))
                .from(PRODUCT)
                .join(PRODUCT.ID.on(PURCHASE_ITEM.PRODUCT_ID))
                .groupBy(PRODUCT.NAME)
                .having(alias("PURCHASED_VALUE").gt(1).and(max(PURCHASE_ITEM.QUANTITY).lt(100)))
                .orderBy(alias("PURCHASED_VALUE").asc());

        statementTester()
            .assertSql("""
                select PR.NAME PR_NAME, sum(PR.PRICE * PI.QUANTITY) PURCHASED_VALUE, max(PI.QUANTITY) MAX_QNTY \
                from PRODUCT PR \
                inner join PURCHASE_ITEM PI on PR.ID = PI.PRODUCT_ID \
                group by PR.NAME \
                having PURCHASED_VALUE > ? and max(PI.QUANTITY) < ? \
                order by PURCHASED_VALUE asc"""
            )
            .assertParams(1, 100)
            .assertRowCount(2)
            .assertRow(1,
                "PR_NAME", containsString("Ekornes Stressless"),
                "PURCHASED_VALUE", isBigDecimalCloseTo(5433.5, 0.01),
                "MAX_QNTY", isInteger(1)
            )
            .assertRow(2,
                "PR_NAME", containsString("Louis Poulsen"),
                "PURCHASED_VALUE", isBigDecimalCloseTo(11335.35, 0.01),
                "MAX_QNTY", isInteger(3)
            )
            .verify(stmt);
    }

    @Test
    public void shouldHandleDistinct() {
        PreparableStatement stmt =
            selectDistinct(PRODUCT.CATEGORY)
                .from(PRODUCT)
                .orderBy(colPos(1).asc());

        statementTester()
            .assertSql("""
                select distinct PR.CATEGORY PR_CATEGORY \
                from PRODUCT PR \
                order by 1 asc"""
            )
            .assertRowCount(4)
            .assertRow(1, "PR_CATEGORY", is("APPLIANCE"))
            .assertRow(2, "PR_CATEGORY", is("ELECTRONICS"))
            .assertRow(3, "PR_CATEGORY", is("FURNITURE"))
            .assertRow(4, "PR_CATEGORY", is("LAMP"))
            .verify(stmt);
    }

    @Test
    public void shouldHandleOrderByNulls() {
        PreparableStatement stmt =
            select(PURCHASE.NOTES)
                .from(PURCHASE)
                .orderBy(colPos(1).asc().nullsFirst());

        statementTester()
            .assertSql("""
                select PU.NOTES PU_NOTES \
                from PURCHASE PU \
                order by 1 asc nulls first"""
            )
            .assertRowCount(2)
            .assertRow(1, "PU_NOTES", isNull())
            .assertRow(2, "PU_NOTES", is("TBD"))
            .verify(stmt);
    }

    @Test
    public void shouldHandleForUpdate() {
        PreparableStatement stmt =
            select(CUSTOMER.LAST_NAME, CUSTOMER.FIRST_NAME)
                .from(CUSTOMER)
                .where(CUSTOMER.COUNTRY_CODE.le("XXX"))
                .forUpdate();

        statementTester()
            .assertSql("""
                select C.LAST_NAME C_LAST_NAME, C.FIRST_NAME C_FIRST_NAME \
                from CUSTOMER C \
                where C.COUNTRY_CODE <= ? \
                for update"""
            )
            .assertParams("XXX")
            .assertRowCount(3)
            .verify(stmt);
    }

    @Test
    public void shouldHandleSystemFunctions() {
        PreparableStatement stmt =
            select(CUSTOMER.ID, currentTimestamp().as("CTS"), currentTime().as("CT"), currentDate().as("CD"))
                .from(CUSTOMER);

        statementTester()
            .assertSql("""
                select C.ID C_ID, current_timestamp CTS, current_time CT, current_date CD \
                from CUSTOMER C"""
            )
            .assertRowCount(3)
            .verify(stmt);
    }

    @Test
    public void shouldHandleScalarMathFunctions() {
        PreparableStatement stmt =
            select(PRODUCT.NAME, round(PRODUCT.PRICE).as("ROUND"), abs(-1).as("ABS"), ceil(PRODUCT.PRICE).as("CEIL"), floor(PRODUCT.PRICE).as("FLOOR"), ln(Math.E).as("LN"), exp(1).as("EXP"), sqrt(4).as("SQRT"), pow($(3), 2).as("POW"))
                .from(PRODUCT)
                .orderBy(PRODUCT.NAME.asc());

        statementTester()
            .assertSql("""
                select PR.NAME PR_NAME, round(PR.PRICE) ROUND, abs(?) ABS, ceiling(PR.PRICE) CEIL, floor(PR.PRICE) FLOOR, ln(?) LN, exp(?) EXP, sqrt(?) SQRT, power(?, ?) POW \
                from PRODUCT PR \
                order by PR.NAME asc"""
            )
            .assertParams(-1, Math.E, 1, 4, 3, 2)
            .assertRowCount(5)
            .assertRow(2,
                "ROUND", isBigDecimal(5434),
                "ABS", isInteger(1),
                "CEIL", isBigDecimal(5434),
                "FLOOR", isBigDecimal(5433),
                "LN", isDouble(1.0),
                "EXP", isDoubleCloseTo(Math.E, 0.000001),
                "SQRT", isDouble(2.0),
                "POW", isDouble(9.0))
            .assertRow(3,
                "ROUND", isBigDecimal(7122))
            .verify(stmt);
    }

    @Test
    public void shouldHandleArithmeticOperators() {
        PreparableStatement stmt =
            select(PRODUCT.NAME, PRODUCT.PRICE.plus($i(1)).as("PLUS_"), PRODUCT.PRICE.minus(2).as("MINUS_"), PRODUCT.PRICE.times(3).as("TIMES_"), PRODUCT.PRICE.dividedBy(4).as("DIVIDE_"), PRODUCT.PRICE.mod(5).as("MOD_"), neg($(6).plus(7)).as("NEG_"))
                .from(PRODUCT)
                .orderBy(PRODUCT.NAME.asc());

        statementTester()
            .assertSql("""
                select PR.NAME PR_NAME, PR.PRICE + 1 PLUS_, PR.PRICE - ? MINUS_, PR.PRICE * ? TIMES_, PR.PRICE / ? DIVIDE_, PR.PRICE % ? MOD_, -(? + ?) NEG_ \
                from PRODUCT PR \
                order by PR.NAME asc"""
            )
            .assertParams(2, 3, 4, 5, 6, 7)
            .assertRowCount(5)
            .assertRow(2,
                "PLUS_", isBigDecimalCloseTo(5434.5, 0.01),
                "MINUS_", isBigDecimalCloseTo(5431.5, 0.01),
                "TIMES_", isBigDecimalCloseTo(16300.5, 0.01),
                "DIVIDE_", isBigDecimalCloseTo(1358.375, 0.0001),
                "MOD_", isBigDecimalCloseTo(3.50, 0.001),
                "NEG_", isBigDecimal(-13))
            .verify(stmt);
    }

    @Test
    void shouldHandleSetOperations() {
        SelectSetOpStatement stmt =
            select(PRODUCT.NAME.as("PR_NAME"))
                .from(PRODUCT)
                .where(PRODUCT.NAME.like("Apple").or(PRODUCT.NAME.like("Ekornes")))
                .orderBy(PRODUCT.NAME.asc())
                .intersect(
                    select(PRODUCT.NAME)
                        .from(PRODUCT)
                        .where(PRODUCT.NAME.like("Ekornes"))
                )
                .unionAll(
                    select(PRODUCT.NAME)
                        .from(PRODUCT)
                        .where(PRODUCT.NAME.like("Apple").or(PRODUCT.NAME.like("Electrolux")))
                        .orderBy(PRODUCT.NAME.asc())
                )
                .except(
                    select(PRODUCT.NAME)
                        .from(PRODUCT)
                        .where(PRODUCT.NAME.like("Electrolux"))
                )
                .orderBy(alias("PR_NAME").asc());

        statementTester()
            .assertSql("""
                (select PR.NAME PR_NAME \
                from PRODUCT PR \
                where PR.NAME like ? or PR.NAME like ? \
                order by PR.NAME asc) \
                intersect \
                (select PR.NAME PR_NAME \
                from PRODUCT PR \
                where PR.NAME like ?) \
                union all \
                (select PR.NAME PR_NAME \
                from PRODUCT PR \
                where PR.NAME like ? or PR.NAME like ? \
                order by PR.NAME asc) \
                except \
                (select PR.NAME PR_NAME \
                from PRODUCT PR \
                where PR.NAME like ?) \
                order by PR_NAME asc"""
            )
            .assertParams("%Apple%", "%Ekornes%", "%Ekornes%", "%Apple%", "%Electrolux%", "%Electrolux%")
            .assertRowCount(2)
            .assertRow(1,
                "PR_NAME", is("Apple iPad Pro tablet")
            )
            .assertRow(2,
                "PR_NAME", is("Ekornes Stressless resting chair")
            )
            .verify(stmt);
    }

    @Test
    public void shouldHandleSimpleCaseExpressions() {
        PreparableStatement stmt =
            select(
                case_(CUSTOMER.LAST_NAME)
                    .when("Nordmann").then("Norwegian")
                    .when("Hansen").then("Dane")
                    .when("Svensson").then("Swede")
                    .else_("Not Scandinavian")
                .end().as("Nationality"))
                .from(CUSTOMER)
                .orderBy(colPos(1).asc());

        statementTester()
            .assertSql("""
                select case C.LAST_NAME \
                when ? then ? \
                when ? then ? \
                when ? then ? \
                else ? \
                end Nationality \
                from CUSTOMER C \
                order by 1 asc"""
            )
            .assertParams("Nordmann", "Norwegian", "Hansen", "Dane", "Svensson", "Swede", "Not Scandinavian")
            .assertRowCount(3)
            .assertRow(1, "NATIONALITY", is("Dane"))
            .assertRow(2, "NATIONALITY", is("Norwegian"))
            .assertRow(3, "NATIONALITY", is("Swede"))
            .verify(stmt);
    }

    @Test
    public void shouldHandleSearchedCaseExpressions() {
        PreparableStatement stmt =
            select(
                case_()
                    .when(CUSTOMER.LAST_NAME.eq("Nordmann")).then("Norwegian")
                    .when(CUSTOMER.LAST_NAME.eq("Hansen")).then("Dane")
                    .when(CUSTOMER.LAST_NAME.eq("Svensson")).then("Swede")
                    .else_("Not Scandinavian")
                .end().as("Nationality"))
                .from(CUSTOMER)
                .orderBy(colPos(1).asc());

        statementTester()
            .assertSql("""
                select case \
                when C.LAST_NAME = ? then ? \
                when C.LAST_NAME = ? then ? \
                when C.LAST_NAME = ? then ? \
                else ? \
                end Nationality \
                from CUSTOMER C \
                order by 1 asc"""
            )
            .assertParams("Nordmann", "Norwegian", "Hansen", "Dane", "Svensson", "Swede", "Not Scandinavian")
            .assertRowCount(3)
            .assertRow(1, "NATIONALITY", is("Dane"))
            .assertRow(2, "NATIONALITY", is("Norwegian"))
            .assertRow(3, "NATIONALITY", is("Swede"))
            .verify(stmt);
    }

    @Test
    public void shouldHandleCastFunction() {
        PreparableStatement stmt =
            select(PRODUCT.NAME, PRODUCT.PRICE.plus(cast(1).as(decimal())).as("DEC_CAST"), cast("2").as(varchar(1)).as("STR_CAST"))
                .from(PRODUCT)
                .orderBy(PRODUCT.NAME.asc());

        statementTester()
            .assertSql("""
                select PR.NAME PR_NAME, PR.PRICE + cast(? as decimal) DEC_CAST, cast(? as varchar(1)) STR_CAST \
                from PRODUCT PR \
                order by PR.NAME asc"""
            )
            .assertParams(1, "2")
            .assertRowCount(5)
            .assertRow(2,
                "DEC_CAST", isBigDecimalCloseTo(5434.5, 0.01),
                "STR_CAST", is("2")
            )
            .verify(stmt);
    }
}
