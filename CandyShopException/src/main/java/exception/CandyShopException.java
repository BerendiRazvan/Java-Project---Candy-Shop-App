package exception;

public class CandyShopException extends Exception {
    public CandyShopException() {

    }

    public CandyShopException(String message) {
        super(message);
    }

    public CandyShopException(String message, Throwable cause) {
        super(message, cause);
    }
}
