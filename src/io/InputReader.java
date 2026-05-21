import java.util.Scanner;

/**
 * InputReader — консолоос оролт авах (safe).
 */
public class InputReader {
    private Scanner scanner;

    /**
     * Constructor.
     */
    public InputReader() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * readInt() — int авах, хамрагдах мужид байна уу шалгах.
     */
    public int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            try {
                String input = scanner.nextLine().trim();
                int value = Integer.parseInt(input);
                if (value >= min && value <= max) {
                    return value;
                } else {
                    System.out.println("❌ " + min + "-" + max + " хооронд оруулна уу!");
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Хүчинтэй тоо оруулна уу!");
            }
        }
    }

    /**
     * readString() — string авах.
     */
    public String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    /**
     * readYesNo() — yes/no авах.
     */
    public boolean readYesNo(String prompt) {
        while (true) {
            String input = readString(prompt + " (тийм/үгүй): ").toLowerCase();
            if (input.equals("y") || input.equals("yes") || input.equals("тийм") || input.equals("т")) {
                return true;
            } else if (input.equals("n") || input.equals("no") || input.equals("үгүй") || input.equals("ү")) {
                return false;
            }
            System.out.println("❌ Тийм эсвэл үгүй гэж оруулна уу!");
        }
    }
}
