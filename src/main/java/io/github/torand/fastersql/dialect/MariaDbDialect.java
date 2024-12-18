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
package io.github.torand.fastersql.dialect;

import java.util.EnumSet;
import java.util.Optional;

import static io.github.torand.fastersql.dialect.Capability.LIMIT_OFFSET;

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
