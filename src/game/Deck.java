import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Deck class — картны колода.
 */
public class Deck {
    private List<Card> cards;

    /**
     * Constructor — картын жагсаалтаас колода үүсгэх.
     */
    public Deck(List<Card> cardList) {
        this.cards = new ArrayList<>(cardList);
        shuffle();
    }

    /**
     * shuffle() — картуудыг сэлгэх.
     */
    public void shuffle() {
        Collections.shuffle(cards);
    }

    /**
     * draw() — колодноос 1 карт авах.
     */
    public Card draw() {
        if (cards.isEmpty()) {
            return null; // Дэвтэр сүүлэхэд fatigue damage болох
        }
        return cards.remove(0);
    }

    public int size() {
        return cards.size();
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }
}
