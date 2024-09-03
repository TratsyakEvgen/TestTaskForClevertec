package ru.clevertec.check.ioc.exception;

public class InjectorException extends RuntimeException {

    public InjectorException() {
    }

    public InjectorException(String message) {
        super(message);
    }

    public InjectorException(String message, Throwable cause) {
        super(message, cause);
    }

    public InjectorException(Throwable cause) {
        super(cause);
    }

    public InjectorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
