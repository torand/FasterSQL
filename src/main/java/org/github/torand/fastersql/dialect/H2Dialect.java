package org.github.torand.fastersql.dialect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.EnumSet;
import java.util.Optional;

import static org.github.torand.fastersql.dialect.Capability.LIMIT_OFFSET;
import static org.github.torand.fastersql.util.lang.StringHelper.generate;

public class H2Dialect implements Dialect {
    private static final EnumSet<Capability> SUPPORTED_CAPS = EnumSet.of(LIMIT_OFFSET);

    @Override
    public String getProductName() {
        return "H2";
    }

    @Override
    public Optional<String> getRowNumLiteral() {
        return Optional.of("rownum()");
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

    public static void setupCustomizations(Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("CREATE ALIAS IF NOT EXISTS TO_NUMBER FOR \"org.github.torand.fastersql.dialect.H2CustomFunctions.toNumber\"")) {
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to setup H2 customizations", e);
        }
    }
}
