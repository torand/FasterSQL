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

import io.github.torand.fastersql.dialect.Dialect;
import io.github.torand.fastersql.statement.StatementTester;
import oracle.jdbc.pool.OracleDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.oracle.OracleContainer;

import java.sql.SQLException;
import java.time.Duration;

@Testcontainers
abstract class OracleTest {
    private static final Logger logger = LoggerFactory.getLogger(OracleTest.class);

    private static final String IMAGE = "gvenzl/oracle-free:23.5-slim-faststart";

    @Container
    protected static final OracleContainer oracleContainer = new OracleContainer(IMAGE)
        .withStartupTimeout(Duration.ofMinutes(3))
        .withUsername("testuser")
        .withPassword("testpwd")
        .withInitScript("oracle-init.sql")
        .withLogConsumer(new Slf4jLogConsumer(logger).withSeparateOutputStreams());

    protected static OracleDataSource ds;

    @BeforeAll
    static void setUp() throws SQLException {
        oracleContainer.start();

        ds = new OracleDataSource();
        ds.setURL(oracleContainer.getJdbcUrl());
        ds.setUser(oracleContainer.getUsername());
        ds.setPassword(oracleContainer.getPassword());
    }

    protected StatementTester statementTester() {
        return StatementTester.using(ds);
    }

    protected StatementTester statementTester(Dialect dialect) {
        return StatementTester.using(ds, dialect);
    }
}
