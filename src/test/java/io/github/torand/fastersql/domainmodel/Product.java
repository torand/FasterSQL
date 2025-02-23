package io.github.torand.fastersql.domainmodel;

import java.util.UUID;

public record Product(
    UUID id,
    String name,
    String description,
    ProductCategory category,
    double price,
    int stock_count
) {
}
