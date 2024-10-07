package org.github.torand.fastersql.dialect;

import java.util.EnumSet;
import java.util.Optional;

import static org.github.torand.fastersql.util.lang.StringHelper.generate;

public class OracleDialect implements Dialect {
    private static final EnumSet<Capability> SUPPORTED_CAPS = EnumSet.noneOf(Capability.class);

    @Override
    public String getProductName() {
        return "Oracle";
    }

    @Override
    public Optional<String> getRowNumLiteral() {
        return Optional.of("rownum");
    }

    @Override
    public String getToNumberFunction(String operand, int precision, int scale) {
        StringBuilder mask = new StringBuilder();
        if (precision-scale > 0) {
            mask.append(generate("9", precision-scale));
        }
        if (scale > 0) {
            mask.append(".").append(generate("9", scale));
        }

        return "to_number(" + operand + ", '" + mask + "')";
    }

    @Override
    public boolean supports(Capability capability) {
        return SUPPORTED_CAPS.contains(capability);
    }
}