import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Deck — Картны багц.
 *
 * Encapsulation: List<Card> нь private — зөвхөн draw(), shuffle(), size(),
 * isEmpty() method-аар хандана.
 */
public class Deck {

    private final List<Card> cards;

    /**
     * @param cards Анхны картны жагсаалт (хуулбар хадгалагдана)
     */
    public Deck(List<Card> cards) {
        if (cards == null) throw new IllegalArgumentException("Картны жагсаалт null байж болохгүй.");
        this.cards = new ArrayList<>(cards);
    }

    /** Картуудыг санамсаргүй дарааллаар холино. */
    public void shuffle() {
        Collections.shuffle(cards);
    }

    /**
     * Дээд картыг татаж авна.
     * @return Карт, эсвэл deck хоосон бол null
     */
    public Card draw() {
        if (cards.isEmpty()) return null;
        return cards.remove(cards.size() - 1);
    }

    /** Үлдсэн картны тоо. */
    public int size() {
        return cards.size();
    }

    /** Deck хоосон эсэх. */
    public boolean isEmpty() {
        return cards.isEmpty();
    }

    /**
     * Deck-ийн агуулгын мөрөн жагсаалт (GameSaver-т хэрэгтэй).
     */
    public List<Card> getCards() {
        return Collections.unmodifiableList(cards);
    }
}
