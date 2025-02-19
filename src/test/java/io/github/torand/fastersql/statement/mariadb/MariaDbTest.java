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
package io.github.torand.fastersql.statement.mariadb;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.torand.fastersql.statement.StatementTester;
import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;
import java.time.Duration;

@Testcontainers
public abstract class MariaDbTest {
    private static final Logger logger = LoggerFactory.getLogger(MariaDbTest.class);

    //private static final String IMAGE = "mysql:8.0.36";

    @Container
    protected static MariaDBContainer mariaDBContainer = (MariaDBContainer) new MariaDBContainer()
        .withDatabaseName("testdb")
        .withUsername("testuser")
        .withPassword("testpwd")
        .withInitScript("mariadb-init.sql")
        .withStartupTimeout(Duration.ofMinutes(3))
        .withLogConsumer(new Slf4jLogConsumer(logger).withSeparateOutputStreams());

    protected static DataSource ds;

    @BeforeAll
    static void setUp() {
        mariaDBContainer.start();

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(mariaDBContainer.getJdbcUrl());
        hikariConfig.setUsername(mariaDBContainer.getUsername());
        hikariConfig.setPassword(mariaDBContainer.getPassword());
        hikariConfig.setDriverClassName(mariaDBContainer.getDriverClassName());

        ds = new HikariDataSource(hikariConfig);
    }

    protected StatementTester statementTester() {
        return StatementTester.using(ds);
    }
}
