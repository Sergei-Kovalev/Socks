package jdev.kovalev.exception;

public class LoadFileDataException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Ошибка при чтении файла";

    public LoadFileDataException() {
        super(DEFAULT_MESSAGE);
    }

    public LoadFileDataException(String message) {
        super(message);
    }
}
