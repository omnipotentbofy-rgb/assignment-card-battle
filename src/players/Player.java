import java.util.ArrayList;
import java.util.List;

/**
 * Player abstract class — тоглогчийн үндсэн төлөв + механик.
 */
public abstract class Player {
    protected String name;
    protected int hp;
    protected int maxHp = 30;
    protected int mana;
    protected int maxMana = 3;
    protected Deck deck;
    protected List<Card> hand;
    protected int nextAttackBonus = 0; // BuffCard-ийн бонус
    protected List<CreatureCard> battlefield; // Stretch

    /**
     * Constructor.
     */
    public Player(String name, Deck deck) {
        this.name = name;
        this.hp = maxHp;
        this.mana = maxMana;
        this.deck = deck;
        this.hand = new ArrayList<>();
        this.battlefield = new ArrayList<>();
        
        // Анхны 5 картыг татах
        for (int i = 0; i < 5; i++) {
            drawCard();
        }
    }

    /**
     * Deck-ээс 1 карт draw хийх (max 7).
     */
    public void drawCard() {
        if (hand.size() < 7) {
            Card card = deck.draw();
            if (card != null) {
                hand.add(card);
            }
        }
    }

    /**
     * playCard — картыг тоглох.
     */
    public void playCard(int handIndex, Player opponent) {
        if (handIndex < 0 || handIndex >= hand.size()) {
            throw new IllegalArgumentException("Буруу картны индекс!");
        }
        Card card = hand.get(handIndex);
        if (mana < card.getManaCost()) {
            throw new InsufficientManaException("Мана хүрэлцэхгүй!");
        }
        mana -= card.getManaCost();
        card.play(this, opponent);
        hand.remove(handIndex);
    }

    /**
     * takeDamage — хохирол авах.
     */
    public void takeDamage(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Хохирол сөрөг байж болохгүй!");
        }
        hp = Math.max(0, hp - amount);
    }

    /**
     * heal — сэргүүлэх (maxHp-аас хэтрэхгүй).
     */
    public void heal(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Сэргүүлэх хэмжээ сөрөг байж болохгүй!");
        }
        hp = Math.min(maxHp, hp + amount);
    }

    /**
     * startTurn — ээлжийн эхэнд мана нэмэгдэх, карт татах.
     */
    public void startTurn() {
        maxMana = Math.min(10, maxMana + 1);
        mana = maxMana;
        drawCard();
    }

    /**
     * endTurn — ээлж дуусах үед creature-ууд дайрна (stretch).
     */
    public void endTurn() {
        // Stretch: creature attack
        for (CreatureCard creature : battlefield) {
            if (creature.isAlive()) {
                // creature attack logic болно
            }
        }
    }

    /**
     * isAlive — амьд байгаа эсэх.
     */
    public boolean isAlive() {
        return hp > 0;
    }

    /**
     * Abstract method — хүүхдүүд өөрсдийн аргаар implement хийнэ.
     */
    public abstract int chooseCard(Player opponent);

    // BuffCard support
    public void addAttackBuff(int amount) {
        nextAttackBonus += amount;
    }

    public int getNextAttackBonus() {
        return nextAttackBonus;
    }

    public void resetAttackBonus() {
        nextAttackBonus = 0;
    }

    // Getters
    public String getName() {
        return name;
    }

    public int getHp() {
        return hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getMana() {
        return mana;
    }

    public int getMaxMana() {
        return maxMana;
    }

    public List<Card> getHand() {
        return hand;
    }

    public List<CreatureCard> getBattlefield() {
        return battlefield;
    }
}
