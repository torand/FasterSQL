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
package io.github.torand.fastersql.statement;

import io.github.torand.fastersql.Context;
import io.github.torand.fastersql.dialect.Dialect;
import io.github.torand.fastersql.util.ResultSetTester;
import org.hamcrest.Matcher;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static io.github.torand.fastersql.util.ResultSetTester.resultSetTester;
import static io.github.torand.fastersql.util.lang.StringHelper.nonBlank;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

public class StatementTester {
    private final DataSource ds;
    private final Dialect dialect;
    private String expectedSql;
    private Object[] expectedParams;
    private ResultSetTester expectedResultSet;
    private Integer expectedAffectedRowCount;

    public static StatementTester using(DataSource ds) {
        return new StatementTester(ds, null);
    }

    public static StatementTester using(DataSource ds, Dialect dialect) {
        return new StatementTester(ds, dialect);
    }

    protected StatementTester(DataSource ds, Dialect dialect) {
        this.ds = ds;
        this.dialect = dialect;
    }

    public StatementTester assertSql(String expectedSql) {
        this.expectedSql = expectedSql;
        return this;
    }

    public StatementTester assertParams(Object... expectedParams) {
        this.expectedParams = expectedParams;
        return this;
    }

    public StatementTester assertAffectedRowCount(int expectedAffectedRowCount) {
        this.expectedAffectedRowCount = expectedAffectedRowCount;
        return this;
    }

    public StatementTester assertRowCount(int expectedRowCount) {
        if (isNull(expectedResultSet)) {
            this.expectedResultSet = resultSetTester();
        }

        this.expectedResultSet.assertRowCount(expectedRowCount);
        return this;
    }

    public StatementTester assertRow(int rowNo, String columnLabel, Matcher<?> matcher, Object... additionalColumnMatcherPairs) {
        if (isNull(expectedResultSet)) {
            this.expectedResultSet = resultSetTester();
        }

        this.expectedResultSet.assertRow(rowNo, columnLabel, matcher, additionalColumnMatcherPairs);
        return this;
    }

    public void logResultSet(PreparableStatement stmt) {
        try (Connection conn = ds.getConnection()) {
            Dialect dialect = nonNull(this.dialect) ? this.dialect : Dialect.fromConnection(conn);

            ResultSet rs = PreparedStatementBuilder
                .using(conn, dialect)
                .prepare(stmt)
                .executeQuery();

            resultSetTester().log(rs);
        } catch (SQLException e) {
            fail("Logging the result set failed: " + e.getMessage());
        }
    }

    public void verify(PreparableStatement stmt) {
        try (Connection conn = ds.getConnection()) {
            Dialect dialect = nonNull(this.dialect) ? this.dialect : Dialect.fromConnection(conn);
            Context context = Context.of(dialect);

            if (nonBlank(expectedSql)) {
                assertThat(stmt.sql(context)).describedAs("SQL").isEqualTo(expectedSql);
            }

            if (nonNull(expectedParams)) {
                assertThat(stmt.params(context)).describedAs("Parameters").containsExactly(expectedParams);
            }

            if (stmt instanceof SelectStatement) {
                if (nonNull(expectedResultSet)) {
                    ResultSet rs = PreparedStatementBuilder
                        .using(conn, dialect)
                        .prepare(stmt)
                        .executeQuery();

                    expectedResultSet.verify(rs);
                }
            } else {
                int affectedRowCount = PreparedStatementBuilder
                    .using(conn, dialect)
                    .prepare(stmt)
                    .executeUpdate();

                if (nonNull(expectedAffectedRowCount)) {
                    assertThat(affectedRowCount).describedAs("Affected row count").isEqualTo(expectedAffectedRowCount);
                }
            }
        } catch (SQLException e) {
            fail("Verifying the preparableStatement failed: " + e.getMessage());
        }
    }
}
