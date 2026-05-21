import java.util.Scanner;

public class InputReader {

    private final Scanner scanner;

    public InputReader() {
        this.scanner = new Scanner(System.in);
    }

    public int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String raw = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(raw);
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.printf("  ⚠️  %d–%d хооронд тоо оруулна уу.%n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("  ⚠️  Тоо оруулна уу.");
            }
        }
    }

    public String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    public boolean readYesNo(String prompt) {
        while (true) {
            System.out.print(prompt + " (т/ү): ");
            String raw = scanner.nextLine().trim().toLowerCase();
            switch (raw) {
                case "y", "yes", "тийм", "т", "1" -> { return true; }
                case "n", "no", "үгүй", "ү", "0"  -> { return false; }
                default -> System.out.println("  ⚠️  'т' эсвэл 'ү' оруулна уу.");
            }
        }
    }

    public void close() {
        scanner.close();
    }
}
