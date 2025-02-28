package io.github.torand.fastersql.projection;

public final class Projections {
    private Projections() {}

    public static ColumnPosition colPos(int position) {
        return new ColumnPosition(position);
    }
}
