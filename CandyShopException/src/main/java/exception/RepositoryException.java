package exception;

public class RepositoryException extends CandyShopException {
    public RepositoryException() {

    }

    public RepositoryException(String message) {
        super(message);
    }

    public RepositoryException(String message, Throwable cause) {
        super(message, cause);
    }

}
