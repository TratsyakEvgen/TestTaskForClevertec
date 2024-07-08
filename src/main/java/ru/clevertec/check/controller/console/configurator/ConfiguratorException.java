package ru.clevertec.check.controller.console.configurator;

public class ConfiguratorException extends RuntimeException {
    public ConfiguratorException() {
    }

    public ConfiguratorException(String message) {
        super(message);
    }

    public ConfiguratorException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfiguratorException(Throwable cause) {
        super(cause);
    }

    public ConfiguratorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
