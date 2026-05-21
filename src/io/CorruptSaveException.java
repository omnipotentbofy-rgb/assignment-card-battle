import java.io.IOException;

/**
 * CorruptSaveException — Save файл гэмтсэн эсвэл буруу формат байвал хаяна.
 */
public class CorruptSaveException extends IOException {
    public CorruptSaveException(String message) {
        super(message);
    }
}
