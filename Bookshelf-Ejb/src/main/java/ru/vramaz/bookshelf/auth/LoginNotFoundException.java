package ru.vramaz.bookshelf.auth;

public class LoginNotFoundException extends Exception {

    private static final long serialVersionUID = -6461897490201791065L;

    public LoginNotFoundException() { }

    public LoginNotFoundException(String message) {
        super(message);
    }

    public LoginNotFoundException(String message, Throwable e) {
        super(message, e);
    }

}
