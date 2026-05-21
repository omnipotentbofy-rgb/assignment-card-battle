/**
 * Card abstract class — бүх картны эх.
 * Бүх картын төрөл энэ классээс inherit хийх ёстой.
 */
public abstract class Card {
    protected String name;
    protected int manaCost;
    protected String description;
    protected Rarity rarity;

    /**
     * Constructor.
     */
    public Card(String name, int manaCost, String description, Rarity rarity) {
        if (manaCost < 0) {
            throw new IllegalArgumentException("Мана сөрөг байж болохгүй!");
        }
        this.name = name;
        this.manaCost = manaCost;
        this.description = description;
        this.rarity = rarity;
    }

    /**
     * Abstract method — бүх child class заавал implement хийх ёстой.
     */
    public abstract void play(Player self, Player opponent);

    // Getters
    public String getName() {
        return name;
    }

    public int getManaCost() {
        return manaCost;
    }

    public String getDescription() {
        return description;
    }

    public Rarity getRarity() {
        return rarity;
    }

    @Override
    public String toString() {
        return String.format("%s (Мана: %d) [%s] — %s", name, manaCost, rarity, description);
    }
}
