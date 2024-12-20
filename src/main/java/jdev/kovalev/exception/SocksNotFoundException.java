package jdev.kovalev.exception;

public class SocksNotFoundException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "На складе нет носков с таким id";

    public SocksNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public SocksNotFoundException(String message) {
        super(message);
    }
}
