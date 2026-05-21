public abstract class Card {

    private final String name;
    private final int manaCost;
    private final String description;
    private final Rarity rarity;

    public Card(String name, int manaCost, String description, Rarity rarity) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Картны нэр хоосон байж болохгүй.");
        }
        if (manaCost < 0) {
            throw new IllegalArgumentException("Манын зардал сөрөг байж болохгүй.");
        }
        if (rarity == null) {
            throw new IllegalArgumentException("Rarity заавал байх ёстой.");
        }
        this.name = name;
        this.manaCost = manaCost;
        this.description = description != null ? description : "";
        this.rarity = rarity;
    }

    public abstract void play(Player self, Player opponent);

    public String getName()        { return name; }
    public int getManaCost()       { return manaCost; }
    public String getDescription() { return description; }
    public Rarity getRarity()      { return rarity; }

    @Override
    public String toString() {
        return String.format("%s (Мана: %d) — %s [%s]", name, manaCost, description, rarity);
    }
}
