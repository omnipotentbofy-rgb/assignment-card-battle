/**
 * Card — Бүх картны abstract эх класс.
 *
 * Шууд new Card(...) хийж болохгүй — зөвхөн AttackCard, HealCard, BuffCard,
 * CreatureCard гэх мэт child class-аар үүсгэнэ.
 */
public abstract class Card {

    private final String name;
    private final int manaCost;
    private final String description;
    private final Rarity rarity;

    /**
     * @param name        Картны нэр (жишээ: "🔥 Blazing Fireball")
     * @param manaCost    Тоглоход шаардлагатай мана (0-10)
     * @param description Картны тайлбар
     * @param rarity      COMMON / RARE / LEGENDARY
     */
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

    // ── Abstract method ──────────────────────────────────────────────────────
    /**
     * Картыг тоглох — child class бүр өөрийн логиктоор override хийнэ.
     *
     * @param self     Картыг тоглосон тоглогч
     * @param opponent Дайсан тоглогч
     */
    public abstract void play(Player self, Player opponent);

    // ── Getters ──────────────────────────────────────────────────────────────
    public String getName()       { return name; }
    public int getManaCost()      { return manaCost; }
    public String getDescription() { return description; }
    public Rarity getRarity()     { return rarity; }

    // ── toString ─────────────────────────────────────────────────────────────
    @Override
    public String toString() {
        return String.format("%s (Мана: %d) — %s [%s]", name, manaCost, description, rarity);
    }
}
