package expression.exceptions;

public class UnsupportedOperationError extends RuntimeException {
    public UnsupportedOperationError(String message) {
        super(message);
    }
}
