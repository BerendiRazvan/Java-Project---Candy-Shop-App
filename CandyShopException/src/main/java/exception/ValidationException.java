package exception;

public class ValidationException extends CandyShopException {
    public ValidationException() {

    }

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
