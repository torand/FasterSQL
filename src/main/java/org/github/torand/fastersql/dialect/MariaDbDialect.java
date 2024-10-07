package org.github.torand.fastersql.dialect;

import java.util.EnumSet;
import java.util.Optional;

import static org.github.torand.fastersql.dialect.Capability.LIMIT_OFFSET;

public class MariaDbDialect implements Dialect {
    private static final EnumSet<Capability> SUPPORTED_CAPS = EnumSet.of(LIMIT_OFFSET);

    @Override
    public String getProductName() {
        return "MariaDB";
    }

    @Override
    public Optional<String> getRowNumLiteral() {
        return Optional.empty();
    }

    @Override
    public String getToNumberFunction(String operand, int precision, int scale) {
        return "cast(" + operand + " as decimal(" + precision + "," + scale + "))";
    }

    @Override
    public boolean supports(Capability capability) {
        return SUPPORTED_CAPS.contains(capability);
    }
}
