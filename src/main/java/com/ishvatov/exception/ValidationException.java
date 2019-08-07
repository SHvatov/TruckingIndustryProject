package com.ishvatov.exception;

/**
 * Defines a basic exception, that may occur during the validation of the
 * user's input.
 *
 * @author Sergey Khvatov
 */
public class ValidationException extends CustomProjectException {

    /**
     * Constructs a new runtime exception with {@code null} as its
     * detail message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public ValidationException() {
        super("Validation exception.");
    }

    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public ValidationException(String message) {
        super(message);
    }

    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param clazz   class of the object, where exception was thrown.
     * @param method  method, where exception was thrown.
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public ValidationException(Class<?> clazz, String method, String message) {
        super(clazz, method, message);
    }
}
