package com.ishvatov.exception;

/**
 * Basic runtime exception class, that is used to encapsulate
 * all exceptions that may be thrown during the work of the project.
 *
 * @author Sergey Khvatov
 */
public class CustomProjectException extends RuntimeException {

    /**
     * Constructs a new runtime exception with {@code null} as its
     * detail message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public CustomProjectException() {
    }

    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public CustomProjectException(String message) {
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
    public CustomProjectException(Class<?> clazz, String method, String message) {
        super(String.format(
            "Exception has occurred in class: [%s], in method: [%s], message: [%s]",
            clazz.getName(), method, message)
        );
    }
}
