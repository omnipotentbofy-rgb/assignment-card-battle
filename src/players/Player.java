import java.util.ArrayList;
import java.util.List;

/**
 * Player — Бүх тоглогчийн abstract эх класс.
 *
 * HumanPlayer ба AIPlayer энэ классаас extends хийнэ.
 * Шууд new Player(...) хийж болохгүй.
 */
public abstract class Player {

    private final String name;
    private int hp;
    private final int maxHp;
    private int mana;
    private int maxMana;
    private final Deck deck;
    private final List<Card> hand;
    private final List<CreatureCard> battlefield;
    private int nextAttackBonus;

    private static final int STARTING_HP    = 30;
    private static final int STARTING_MANA  = 3;
    private static final int MAX_MANA_CAP   = 10;
    private static final int MAX_HAND_SIZE  = 7;
    private static final int STARTING_HAND  = 5;

    /**
     * @param name Тоглогчийн нэр
     * @param deck Тоглоомын картны багц
     */
    public Player(String name, Deck deck) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Тоглогчийн нэр хоосон байж болохгүй.");
        }
        this.name = name;
        this.hp = STARTING_HP;
        this.maxHp = STARTING_HP;
        this.mana = STARTING_MANA;
        this.maxMana = STARTING_MANA;
        this.deck = deck;
        this.hand = new ArrayList<>();
        this.battlefield = new ArrayList<>();
        this.nextAttackBonus = 0;

        // Анхны 5 картыг татна
        for (int i = 0; i < STARTING_HAND; i++) {
            drawCard();
        }
    }

    // ── Turn management ──────────────────────────────────────────────────────

    /**
     * Ээлжийн эхэнд: maxMana +1 (max 10), mana дүүргэх, карт татах.
     */
    public void startTurn() {
        if (maxMana < MAX_MANA_CAP) {
            maxMana++;
        }
        mana = maxMana;
        drawCard();
    }

    /**
     * Ээлжийн төгсгөлд: талбарын бүх creature дайсан руу довтолно.
     */
    public void endTurn(Player opponent) {
        List<CreatureCard> living = new ArrayList<>(battlefield);
        for (CreatureCard creature : living) {
            if (creature.isAlive() && opponent.isAlive()) {
                creature.attack(opponent);
            }
        }
        // Үхсэн creature-уудыг цэвэрлэх
        battlefield.removeIf(c -> !c.isAlive());
    }

    // ── Card actions ─────────────────────────────────────────────────────────

    /**
     * Deck-ээс нэг карт hand-д нэмнэ (max 7-оос хэтрэхгүй).
     */
    public void drawCard() {
        if (hand.size() >= MAX_HAND_SIZE) {
            System.out.println("⚠️  " + name + "-ийн гарт карт дүүрсэн (7/7).");
            return;
        }
        Card drawn = deck.draw();
        if (drawn != null) {
            hand.add(drawn);
        } else {
            System.out.println("💀 " + name + " — картаа барж дуусав! Хэт их байна...");
        }
    }

    /**
     * Hand-аас картыг тоглоно.
     *
     * @param handIndex   Hand-дахь картны индекс
     * @param opponent    Дайсан тоглогч
     * @throws InsufficientManaException мана хүрэлцэхгүй бол
     * @throws IllegalArgumentException  индекс буруу бол
     */
    public void playCard(int handIndex, Player opponent) {
        if (handIndex < 0 || handIndex >= hand.size()) {
            throw new IllegalArgumentException("Буруу индекс: " + handIndex);
        }
        Card card = hand.get(handIndex);
        if (card.getManaCost() > mana) {
            throw new InsufficientManaException(
                "Мана хүрэлцэхгүй (" + card.getManaCost() + " шаардлагатай, "
                + mana + " байна)."
            );
        }
        mana -= card.getManaCost();
        hand.remove(handIndex);
        card.play(this, opponent);
    }

    // ── HP / heal / damage ───────────────────────────────────────────────────

    /**
     * Хохирол авна.
     * @throws IllegalArgumentException amount сөрөг бол
     */
    public void takeDamage(int amount) {
        if (amount < 0) throw new IllegalArgumentException("Хохирол сөрөг байж болохгүй.");
        hp = Math.max(0, hp - amount);
    }

    /**
     * HP эдгэрнэ — maxHp-аас хэтрэхгүй.
     */
    public void heal(int amount) {
        if (amount < 0) throw new IllegalArgumentException("Эдгээх хэмжээ сөрөг байж болохгүй.");
        hp = Math.min(maxHp, hp + amount);
    }

    public boolean isAlive() { return hp > 0; }

    // ── Buff ─────────────────────────────────────────────────────────────────

    public void addAttackBuff(int amount)  { nextAttackBonus += amount; }
    public void resetAttackBonus()         { nextAttackBonus = 0; }
    public int getNextAttackBonus()        { return nextAttackBonus; }

    // ── Creature battlefield ─────────────────────────────────────────────────

    public void summonCreature(CreatureCard creature) {
        battlefield.add(creature);
    }

    public List<CreatureCard> getBattlefield() { return battlefield; }

    // ── Abstract ─────────────────────────────────────────────────────────────

    /**
     * Гараас тоглох картыг сонгоно.
     * HumanPlayer консолоос, AIPlayer логикоор шийднэ.
     *
     * @param opponent Дайсан тоглогч
     * @return hand-дахь индекс (0+), эсвэл -1 ("pass" / ээлж дуусгах)
     */
    public abstract int chooseCard(Player opponent);

    // ── Getters ──────────────────────────────────────────────────────────────

    public String getName()       { return name; }
    public int getHp()            { return hp; }
    public int getMaxHp()         { return maxHp; }
    public int getMana()          { return mana; }
    public int getMaxMana()       { return maxMana; }
    public List<Card> getHand()   { return hand; }
    public Deck getDeck()         { return deck; }
}
