package jdev.kovalev.exception;

public class UnlogicalFilterConditionException extends RuntimeException {
    private final static String DEFAULT_MESSAGE = "Параметры фильтрации выбраны некорректно";
    public UnlogicalFilterConditionException() {
        super(DEFAULT_MESSAGE);
    }

    public UnlogicalFilterConditionException(String message) {
        super(message);
    }
}
