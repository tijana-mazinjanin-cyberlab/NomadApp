package exceptions;

public class NotValidException extends RuntimeException {
    public NotValidException(String message) {
        super(message);
    }
}