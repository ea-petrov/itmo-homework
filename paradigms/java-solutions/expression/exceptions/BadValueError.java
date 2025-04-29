package expression.exceptions;

public class BadValueError extends RuntimeException {
    public BadValueError(String message) {
        super(message);
    }
}
