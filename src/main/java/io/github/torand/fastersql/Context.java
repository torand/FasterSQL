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
package io.github.torand.fastersql;

import io.github.torand.fastersql.dialect.Dialect;

import static io.github.torand.fastersql.Clause.PROJECTION;
import static io.github.torand.fastersql.Command.SELECT;

public class Context {
    private final Dialect dialect;
    private final Command command;
    private final Clause clause;

    public static Context of(Dialect dialect) {
        return new Context(dialect, null,  null);
    }

    private Context(Dialect dialect, Command command, Clause clause) {
        this.dialect = dialect;
        this.command = command;
        this.clause = clause;
    }

    public Context withCommand(Command command) {
        return new Context(this.dialect, command, command == SELECT ? PROJECTION : null);
    }

    public Context withClause(Clause clause) {
        return new Context(this.dialect, this.command, clause);
    }

    public Dialect getDialect() {
        return this.dialect;
    }

    public Command getCommand() {
        return command;
    }

    public boolean isCommand(Command command) {
        return this.command == command;
    }

    public boolean isClause(Clause claue) {
        return this.clause == clause;
    }
}