/**
 * InsufficientManaException — мана хүрэлцэхгүй үед throw хийгдэнэ.
 */
public class InsufficientManaException extends RuntimeException {
    public InsufficientManaException(String message) {
        super(message);
    }
}
