package me.jraynor.engine.asset.exceptions;

/**
 * Thrown to indicate a string is not a valid ResourceUrn.
 * @author Immortius
 */
public class InvalidUrnException extends RuntimeException {

    public InvalidUrnException() {
    }

    public InvalidUrnException(String message) {
        super(message);
    }

    public InvalidUrnException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidUrnException(Throwable cause) {
        super(cause);
    }

    public InvalidUrnException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
