package ru.clevertec.check.ioc.exception;

public class ClassScannerException extends RuntimeException {
    public ClassScannerException() {
    }

    public ClassScannerException(String message) {
        super(message);
    }

    public ClassScannerException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClassScannerException(Throwable cause) {
        super(cause);
    }

    public ClassScannerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
