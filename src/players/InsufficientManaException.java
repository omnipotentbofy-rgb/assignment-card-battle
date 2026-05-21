/**
 * InsufficientManaException — Мана хүрэлцэхгүй үед хаях exception.
 *
 * Game loop энэ exception-ийг catch хийж, тоглогчид эелдэг мессеж харуулна.
 */
public class InsufficientManaException extends RuntimeException {
    public InsufficientManaException(String message) {
        super(message);
    }
}
