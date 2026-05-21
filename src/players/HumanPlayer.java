/**
 * HumanPlayer — Консолоос оролт авдаг бодит тоглогч.
 */
public class HumanPlayer extends Player {

    private final InputReader inputReader;

    public HumanPlayer(String name, Deck deck, InputReader inputReader) {
        super(name, deck);
        this.inputReader = inputReader;
    }

    /**
     * Тоглогч гараас картын индексийг оруулна.
     * -1 оруулбал ээлж дуусна.
     */
    @Override
    public int chooseCard(Player opponent) {
        if (getHand().isEmpty()) {
            System.out.println("⚠️  Гарт карт байхгүй — ээлж дуусч байна.");
            return -1;
        }
        System.out.println("\n🃏 Гартаа буй картууд:");
        for (int i = 0; i < getHand().size(); i++) {
            Card c = getHand().get(i);
            System.out.printf("  [%d] %s%n", i, c);
        }
        System.out.println("  [-1] Ээлж дуусгах");

        return inputReader.readInt(
            "Тоглох картын дугаарыг оруулна уу (-1 дарвал ээлж дуусах): ",
            -1,
            getHand().size() - 1
        );
    }
}
