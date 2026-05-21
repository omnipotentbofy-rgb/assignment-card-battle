import java.util.Scanner;

/**
 * HumanPlayer extends Player — консолоос оролт авдаг жинхэнэ хүний төлөөлөгч.
 */
public class HumanPlayer extends Player {
    private Scanner scanner;

    /**
     * Constructor — HumanPlayer үүсгэх.
     */
    public HumanPlayer(String name, Deck deck) {
        super(name, deck);
        this.scanner = new Scanner(System.in);
    }

    /**
     * chooseCard() override — консолоос картны индекс авах.
     */
    @Override
    public int chooseCard(Player opponent) {
        while (true) {
            System.out.println("\n" + name + ", картаа сонгоно уу (0-" + (hand.size() - 1) + ") эсвэл -1 дарвал ээлж дуусна:");
            
            // Гарт буй картаа харуул
            for (int i = 0; i < hand.size(); i++) {
                Card card = hand.get(i);
                System.out.printf("[%d] %s (Мана: %d)%n", i, card.getName(), card.getManaCost());
            }

            System.out.print("Оруулна уу: ");
            try {
                String input = scanner.nextLine().trim();
                int choice = Integer.parseInt(input);

                if (choice == -1) {
                    return -1; // Pass
                }

                if (choice >= 0 && choice < hand.size()) {
                    return choice;
                } else {
                    System.out.println("❌ 0-" + (hand.size() - 1) + " хооронд оруулна уу!");
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Хүчинтэй тоо оруулна уу!");
            }
        }
    }
}
