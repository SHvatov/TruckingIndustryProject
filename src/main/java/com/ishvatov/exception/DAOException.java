package com.ishvatov.exception;

/**
 * Defines basic exception that may occur while working with
 * the DAO layer of the program.
 *
 * @author Sergey Khvatov
 */
public class DAOException extends CustomProjectException {

    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public DAOException(String message) {
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
    public DAOException(Class<?> clazz, String method, String message) {
        super(clazz, method, message);
    }
}
