package utils;

public class AssertionUtils {
    public static void assertNonNull(Object o) {
        if (o == null) {
            throw new NullPointerException(o + " should not be null!");
        }
    }
}
