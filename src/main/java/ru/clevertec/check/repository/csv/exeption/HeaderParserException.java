package ru.clevertec.check.repository.csv.exeption;

public class HeaderParserException extends RuntimeException {

    public HeaderParserException() {
    }

    public HeaderParserException(String message) {
        super(message);
    }

    public HeaderParserException(String message, Throwable cause) {
        super(message, cause);
    }

    public HeaderParserException(Throwable cause) {
        super(cause);
    }

    public HeaderParserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
