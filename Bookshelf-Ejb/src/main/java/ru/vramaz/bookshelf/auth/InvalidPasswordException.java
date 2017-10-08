package ru.vramaz.bookshelf.auth;

public class InvalidPasswordException extends Exception {

    private static final long serialVersionUID = -6733156306146407447L;

    public InvalidPasswordException() { }

    public InvalidPasswordException(String message) {
        super(message);
    }

    public InvalidPasswordException(String message, Throwable e) {
        super(message, e);
    }

}
