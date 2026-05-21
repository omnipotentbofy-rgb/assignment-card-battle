/**
 * CreatureCard — Тулааны талбарт үлдэх амьтны карт (Stretch).
 *
 * Тоглогдсоны дараа тулааны талбарт нэмэгдэнэ.
 * Ээлж бүрт opponent руу attackPower хохирол учруулна.
 * Дайсны довтолгооноос health хасагдах ба 0 болвол талбараас хасагдана.
 */
public class CreatureCard extends Card {

    private int health;
    private final int maxHealth;
    private final int attackPower;

    /**
     * @param name        Картны нэр
     * @param manaCost    Манын зардал
     * @param description Тайлбар
     * @param rarity      Ховор байдал
     * @param health      Амьтны HP
     * @param attackPower Амьтны довтолгооны хүч
     */
    public CreatureCard(String name, int manaCost, String description,
                        Rarity rarity, int health, int attackPower) {
        super(name, manaCost, description, rarity);
        if (health <= 0) throw new IllegalArgumentException("Амьтны HP нь эерэг байх ёстой.");
        if (attackPower < 0) throw new IllegalArgumentException("Довтолгооны хүч сөрөг байж болохгүй.");
        this.health = health;
        this.maxHealth = health;
        this.attackPower = attackPower;
    }

    @Override
    public void play(Player self, Player opponent) {
        self.summonCreature(this);
        System.out.println("🐾 " + self.getName() + " — " + getName()
                + " тулааны талбарт гарлаа! [HP:" + health + " / ATK:" + attackPower + "]");
    }

    /**
     * Тухайн амьтан дайсан руу довтолно.
     */
    public void attack(Player opponent) {
        opponent.takeDamage(attackPower);
        System.out.println("  🐾 " + getName() + " → " + opponent.getName()
                + " руу " + attackPower + " хохирол учирлаа!");
    }

    /**
     * Дайсны довтолгооноос хохирол авна.
     * @return true хэрэв амьтан үхсэн бол
     */
    public boolean takeDamage(int amount) {
        if (amount < 0) throw new IllegalArgumentException("Хохирол сөрөг байж болохгүй.");
        health -= amount;
        return health <= 0;
    }

    public boolean isAlive() { return health > 0; }
    public int getHealth()    { return health; }
    public int getMaxHealth() { return maxHealth; }
    public int getAttackPower() { return attackPower; }

    @Override
    public String toString() {
        return String.format("%s (Мана: %d) — HP:%d ATK:%d [%s]",
                getName(), getManaCost(), health, attackPower, getRarity());
    }
}
