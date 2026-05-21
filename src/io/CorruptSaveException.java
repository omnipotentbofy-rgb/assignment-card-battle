import java.io.IOException;

public class CorruptSaveException extends IOException {
    public CorruptSaveException(String message) {
        super(message);
    }
}
