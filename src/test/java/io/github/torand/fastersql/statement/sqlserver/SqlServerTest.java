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
package io.github.torand.fastersql.statement.sqlserver;

import com.microsoft.sqlserver.jdbc.SQLServerXADataSource;
import io.github.torand.fastersql.dialect.Dialect;
import io.github.torand.fastersql.statement.StatementTester;
import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.MSSQLServerContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.startupcheck.MinimumDurationRunningStartupCheckStrategy;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Duration;

@Testcontainers
abstract class SqlServerTest {
    private static final Logger logger = LoggerFactory.getLogger(SqlServerTest.class);

    private static final String IMAGE = "mcr.microsoft.com/mssql/server:2022-latest";

    @Container
    protected static final MSSQLServerContainer sqlServerContainer = new MSSQLServerContainer<>(IMAGE)
        .acceptLicense()
        .withPassword("TestPassw0rd")
        .withInitScript("sqlserver-init.sql")
        .withStartupCheckStrategy(new MinimumDurationRunningStartupCheckStrategy(Duration.ofSeconds(5)))
        .withLogConsumer(new Slf4jLogConsumer(logger).withSeparateOutputStreams());

    protected static SQLServerXADataSource ds;

    @BeforeAll
    static void setUp() {
        sqlServerContainer.start();

        ds = new SQLServerXADataSource();
        ds.setURL(sqlServerContainer.getJdbcUrl());
        ds.setUser(sqlServerContainer.getUsername());
        ds.setPassword(sqlServerContainer.getPassword());
    }

    protected StatementTester statementTester() {
        return StatementTester.using(ds);
    }

    protected StatementTester statementTester(Dialect dialect) {
        return StatementTester.using(ds, dialect);
    }
}
