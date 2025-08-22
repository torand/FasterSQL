package io.github.torand.fastersql.statement;

/**
 * Generic runtime exception thrown by the FasterSQL library,
 */
public class FasterSQLException extends RuntimeException {

    /**
     * Creates a runtime exception.
     * @param message the message.
     * @param cause the inner cause.
     */
    public FasterSQLException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a runtime exception.
     * @param message the message.
     */
    public FasterSQLException(String message) {
        super(message);
    }

    /**
     * Creates a runtime exception.
     * @param cause the inner cause.
     */
    public FasterSQLException(Throwable cause) {
        super(cause);
    }
}
