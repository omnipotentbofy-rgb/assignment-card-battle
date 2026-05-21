import java.util.ArrayList;
import java.util.List;

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

    private static final int STARTING_HP   = 30;
    private static final int STARTING_MANA = 3;
    private static final int MAX_MANA_CAP  = 10;
    private static final int MAX_HAND_SIZE = 7;
    private static final int STARTING_HAND = 5;

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
        for (int i = 0; i < STARTING_HAND; i++) {
            drawCard();
        }
    }

    public void startTurn() {
        if (maxMana < MAX_MANA_CAP) {
            maxMana++;
        }
        mana = maxMana;
        drawCard();
    }

    public void endTurn(Player opponent) {
        List<CreatureCard> living = new ArrayList<>(battlefield);
        for (CreatureCard creature : living) {
            if (creature.isAlive() && opponent.isAlive()) {
                creature.attack(opponent);
            }
        }
        battlefield.removeIf(c -> !c.isAlive());
    }

    public void drawCard() {
        if (hand.size() >= MAX_HAND_SIZE) {
            System.out.println("⚠️  " + name + "-ийн гарт карт дүүрсэн (7/7).");
            return;
        }
        Card drawn = deck.draw();
        if (drawn != null) {
            hand.add(drawn);
        } else {
            System.out.println("💀 " + name + " — картаа барж дуусав!");
        }
    }

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

    public void takeDamage(int amount) {
        if (amount < 0) throw new IllegalArgumentException("Хохирол сөрөг байж болохгүй.");
        hp = Math.max(0, hp - amount);
    }

    public void heal(int amount) {
        if (amount < 0) throw new IllegalArgumentException("Эдгээх хэмжээ сөрөг байж болохгүй.");
        hp = Math.min(maxHp, hp + amount);
    }

    public boolean isAlive() { return hp > 0; }

    public void addAttackBuff(int amount) { nextAttackBonus += amount; }
    public void resetAttackBonus()        { nextAttackBonus = 0; }
    public int getNextAttackBonus()       { return nextAttackBonus; }

    public void summonCreature(CreatureCard creature) {
        battlefield.add(creature);
    }

    public List<CreatureCard> getBattlefield() { return battlefield; }

    public abstract int chooseCard(Player opponent);

    public String getName()      { return name; }
    public int getHp()           { return hp; }
    public int getMaxHp()        { return maxHp; }
    public int getMana()         { return mana; }
    public int getMaxMana()      { return maxMana; }
    public List<Card> getHand()  { return hand; }
    public Deck getDeck()        { return deck; }
}
