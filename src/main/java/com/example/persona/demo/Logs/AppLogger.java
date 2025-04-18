package com.example.persona.demo.Logs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppLogger {

    private static final String RESET = "\u001B[0m";
    private static final String BOLD = "\u001B[1m";

    private static final String BLUE = "\u001B[34m";
    private static final String YELLOW = "\u001B[33m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String CYAN = "\u001B[36m";
    private static final String MAGENTA = "\u001B[35m";
    private static final String GRAY = "\u001B[90m";

    private final Logger logger;
    private final String className;

    private AppLogger(Class<?> clazz) {
        this.logger = LoggerFactory.getLogger(clazz);
        this.className = clazz.getSimpleName();
    }

    public static AppLogger getLogger(Class<?> clazz) {
        return new AppLogger(clazz);
    }

    private String formatMessage(String color, String message) {
        return String.format("%s%s %s[%s]%s %s[%s]%s %s",
                color, RESET,         // emoji con color
                BOLD + CYAN, className, RESET,  // className con cian bold
                YELLOW, Thread.currentThread().getName(), RESET,  // thread name con amarillo
                message // mensaje plano
        );
    }

    public void info(String message) {
        logger.info(formatMessage( BLUE, message));
    }

    public void debug(String message) {
        logger.debug(formatMessage( CYAN, message));
    }

    public void warn(String message) {
        logger.warn(formatMessage( YELLOW, message));
    }

    public void error(String message) {
        logger.error(formatMessage( RED, message));
    }

    public void error(String message, Throwable t) {
        logger.error(formatMessage( RED, message), t);
    }

    public void success(String message) {
        logger.info(formatMessage( GREEN, message));
    }

    public void action(String message) {
        logger.info(formatMessage( MAGENTA, message));
    }
}
