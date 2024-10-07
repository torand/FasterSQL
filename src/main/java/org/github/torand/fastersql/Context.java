package org.github.torand.fastersql;

import org.github.torand.fastersql.dialect.Dialect;

public class Context {
    private final Dialect dialect;
    private final Command command;

    public static Context of(Dialect dialect) {
        return new Context(dialect, null);
    }

    private Context(Dialect dialect, Command command) {
        this.dialect = dialect;
        this.command = command;
    }

    public Context withCommand(Command command) {
        return new Context(this.dialect, command);
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
}