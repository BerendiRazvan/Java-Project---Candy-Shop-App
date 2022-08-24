package exception;

public class BuildException extends CandyShopException {
    public BuildException() {

    }

    public BuildException(String message) {
        super(message);
    }

    public BuildException(String message, Throwable cause) {
        super(message, cause);
    }
}
