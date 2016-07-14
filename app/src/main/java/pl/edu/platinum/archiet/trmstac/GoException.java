package pl.edu.platinum.archiet.trmstac;

public class GoException extends Exception {
    public GoException() {
        super();
    }

    public GoException(String message) {
        super(message);
    }

    public GoException(String message, Throwable cause) {
        super(message, cause);
    }

    public GoException(Throwable cause) {
        super(cause);
    }
}
