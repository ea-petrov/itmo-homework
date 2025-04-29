package expression.exceptions;

public class BadBracketsError extends RuntimeException {
    public BadBracketsError(String message) {
        super(message);
    }
}