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
package io.github.torand.fastersql.sql;

import io.github.torand.fastersql.dialect.Dialect;
import io.github.torand.fastersql.statement.SelectStatement;

import java.util.ArrayList;
import java.util.List;

import static io.github.torand.fastersql.sql.Clause.PROJECTION;
import static io.github.torand.fastersql.sql.Command.SELECT;
import static java.util.Collections.emptyList;

/**
 * Holds the context of statement SQL builders.
 */
public class Context {
    private final Dialect dialect;
    private final Command command;
    private final Clause clause;
    private final List<SelectStatement> outerStatements;

    /**
     * Creates context with specified SQL dialect.
     * @param dialect the SQL dialect.
     * @return the context.
     */
    public static Context of(Dialect dialect) {
        return new Context(dialect, null,  null, emptyList());
    }

    private Context(Dialect dialect, Command command, Clause clause, List<SelectStatement> outerStatements) {
        this.dialect = dialect;
        this.command = command;
        this.clause = clause;
        this.outerStatements = new ArrayList<>(outerStatements);
    }

    /**
     * Sets the current statement command.
     * @param command the statement command.
     * @return the modified context.
     */
    public Context withCommand(Command command) {
        return new Context(this.dialect, command, command == SELECT ? PROJECTION : null, outerStatements);
    }

    /**
     * Sets the current statement clause.
     * @param clause the clause.
     * @return the modified context.
     */
    public Context withClause(Clause clause) {
        return new Context(this.dialect, this.command, clause, outerStatements);
    }

    /**
     * Sets the current outer statement.
     * @param outerStatement the outer statement.
     * @return the modified context.
     */
    public Context withOuterStatement(SelectStatement outerStatement) {
        List<SelectStatement> newOuterStatements = new ArrayList<>(outerStatements);
        newOuterStatements.add(outerStatement);
        return new Context(this.dialect, this.command, this.clause, newOuterStatements);
    }

    /**
     * Gets the SQL dialect.
     * @return the SQL dialect.
     */
    public Dialect getDialect() {
        return this.dialect;
    }

    /**
     * Gets the current statement command.
     * @return the statement command.
     */
    public Command getCommand() {
        return command;
    }

    /**
     * Indicates whether the current statement command is a specific command.
     * @param command the command.
     * @return true if the current command matches; false if not.
     */
    public boolean isCommand(Command command) {
        return this.command == command;
    }

    /**
     * Indicates whether the current statement clause is a specific clause.
     * @param clause the clause.
     * @return true if the current clause matches; false if not.
     */
    public boolean isClause(Clause clause) {
        return this.clause == clause;
    }

    /**
     * Gets the current outer statements, if any.
     * @return the outer statements.
     */
    public List<SelectStatement> getOuterStatements() {
        return outerStatements;
    }
}
