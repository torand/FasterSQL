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

import io.github.torand.fastersql.statement.StatementTester;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Duration;

@Testcontainers
abstract class H2Test {
    private static final Logger logger = LoggerFactory.getLogger(H2Test.class);

    private static final String IMAGE = "torandon/h2database:2.3.232";

    @Container
    protected static final GenericContainer h2Container = new GenericContainer(IMAGE)
        .withStartupTimeout(Duration.ofMinutes(3))
        .withFileSystemBind("src/test/resources/h2-init.sql", "/docker-entrypoint-initdb.d/test-db.sql", BindMode.READ_ONLY)
        .withExposedPorts(8082, 9092)
        .withLogConsumer(new Slf4jLogConsumer(logger).withSeparateOutputStreams());

    protected static JdbcDataSource ds;

    @BeforeAll
    static void setUp() {
        h2Container.start();

        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Could not find H2 databasedriver", e);
        }

        String h2Url = "jdbc:h2:tcp://" + h2Container.getHost() + ":" + h2Container.getMappedPort(9092) + "/test-db";

        ds = new JdbcDataSource();
        ds.setURL(h2Url);
        ds.setUser("");
        ds.setPassword("");
    }

    protected StatementTester statementTester() {
        return StatementTester.using(ds);
    }
}
