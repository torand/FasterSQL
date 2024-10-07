package org.github.torand.fastersql;

import static org.github.torand.fastersql.Command.SELECT;
import static org.github.torand.fastersql.util.contract.Requires.requireNonBlank;

public abstract class Table<ENTITY extends Table<?>> {
    private final String name;
    private final String alias;
    private final TableFactory<ENTITY> tableFactory;

    protected Table(String name, TableFactory<ENTITY> tableFactory) {
        this.name = requireNonBlank(name, "No name specified");
        this.alias = defaultAlias(name);
        this.tableFactory = tableFactory;
    }

    protected Table(String name, String alias, TableFactory<ENTITY> tableFactory) {
        this.name = requireNonBlank(name, "No name specified");
        this.alias = requireNonBlank(alias, "No name specified");
        this.tableFactory = tableFactory;
    }

    public ENTITY as(String alias) {
        return tableFactory.newInstance(alias);
    }

    public Field field(String name) {
        return new Field(this, name);
    }

    public String name() {
        return name;
    }

    public String alias() {
        return alias;
    }

    public String sql(Context context) {
        if (context.isCommand(SELECT)) {
            return name + " " + alias;
        } else {
            return name;
        }
    }

    private String defaultAlias(String name) {
        return name;
    }

    @FunctionalInterface
    public interface TableFactory<ENTITY> {
        ENTITY newInstance(String alias);
    }
}
