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

import io.github.torand.fastersql.dialect.Dialect;
import io.github.torand.fastersql.statement.StatementTester;
import oracle.jdbc.pool.OracleDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.time.Duration;

@Testcontainers
public abstract class AccessTest {
    private static final Logger logger = LoggerFactory.getLogger(AccessTest.class);

    private static final String IMAGE = "?";

    // https://www.javaxt.com/wiki/Tutorials/JDBC/How_to_Open_a_JDBC_Connection_to_Microsoft_Access
    // https://www.codeproject.com/Articles/35018/Access-MS-Access-Databases-from-Java
    // https://www.codeproject.com/Tips/813055/Create-Microsoft-Access-Database-Programmatically

    @Container
    protected static GenericContainer oracleContainer = new GenericContainer(IMAGE)
        .withStartupTimeout(Duration.ofMinutes(3))
        //.withUsername("testuser")
        //.withPassword("testpwd")
        //.withInitScript("oracle-init.sql")
        .withLogConsumer(new Slf4jLogConsumer(logger).withSeparateOutputStreams());

    protected static DataSource ds;

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
