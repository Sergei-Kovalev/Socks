package jdev.kovalev.exception;

public class WrongUUIDFormatException extends RuntimeException{
    private static final String DEFAULT_MESSAGE = "Неверный формат id - он должен быть формата UUID";

    public WrongUUIDFormatException() {
        super(DEFAULT_MESSAGE);
    }

    public WrongUUIDFormatException(String message) {
        super(message);
    }
}
