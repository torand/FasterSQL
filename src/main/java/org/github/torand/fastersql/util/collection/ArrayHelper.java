package org.github.torand.fastersql.util.collection;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

/**
 * Helper functions for arrays
 */
public final class ArrayHelper {
    private ArrayHelper() {}

    public static boolean isEmpty(int[] array) {
        return isNull(array) || array.length == 0;
    }

    public static boolean isEmpty(long[] array) {
        return isNull(array) || array.length == 0;
    }

    public static <T> boolean isEmpty(T[] array) {
        return isNull(array) || array.length == 0;
    }

    public static boolean nonEmpty(int[] array) {
        return nonNull(array) && array.length > 0;
    }

    public static boolean nonEmpty(long[] array) {
        return nonNull(array) && array.length > 0;
    }

    public static <T> boolean nonEmpty(T[] array) {
        return nonNull(array) && array.length > 0;
    }
}
