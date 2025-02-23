package io.github.torand.fastersql.order;

import static io.github.torand.fastersql.util.contract.Requires.requireNonBlank;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

public class OrderBuilder {
    private final String alias;
    private final Integer index;

    OrderBuilder(String alias) {
        this.alias = requireNonBlank(alias, "No alias specified");
        this.index = null;
    }

    OrderBuilder(Integer index) {
        this.alias = null;
        this.index = requireNonNull(index, "No index specified");
    }

    public Order asc() {
        return nonNull(index) ? new Ascending(index) : new Ascending(alias);
    }

    public Order desc() {
        return nonNull(index) ? new Descending(index) : new Descending(alias);
    }
}
