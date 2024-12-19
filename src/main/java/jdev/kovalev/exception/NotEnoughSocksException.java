package jdev.kovalev.exception;

public class NotEnoughSocksException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "На складе недостаточно носков для Вашей операции";

    public NotEnoughSocksException() {
        super(DEFAULT_MESSAGE);
    }

    public NotEnoughSocksException(String message) {
        super(message);
    }
}
