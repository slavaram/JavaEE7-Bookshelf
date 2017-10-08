package ru.vramaz.bookshelf.core;

public class PersistenceException extends RuntimeException {

    private static final long serialVersionUID = -8537897510449478213L;

    public PersistenceException() { }

    public PersistenceException(String message) {
        super(message);
    }

    public PersistenceException(String message, Throwable e) {
        super(message, e);
    }
}
