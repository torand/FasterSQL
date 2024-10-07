package org.github.torand.fastersql.util.lang;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public final class StringHelper {
    private StringHelper() {}

    public static boolean nonBlank(String string) {
        return nonNull(string) && !string.isEmpty();
    }

    public static boolean isBlank(String string) {
        return isNull(string) || string.isEmpty();
    }

    public static String generate(String s, int count) {
        return s.repeat(count);
    }

    public static String generate(String s, int count, String delimiter) {
        StringBuilder b = new StringBuilder();
        for (int t = 0; t < count; t++) {
            if (t != 0) {
                b.append(delimiter);
            }
            b.append(s);
        }
        return b.toString();
    }
}
