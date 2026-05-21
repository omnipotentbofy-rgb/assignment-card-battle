/**
 * AttackCard — Дайсан руу хохирол учруулах карт.
 *
 * Тоглогдох үед opponent.takeDamage(damage) дуудна.
 * Хэрэв self-д nextAttackBonus байвал тэрийг нэмж хэрэглэнэ.
 */
public class AttackCard extends Card {

    private final int damage;

    /**
     * @param name        Картны нэр
     * @param manaCost    Манын зардал
     * @param description Тайлбар
     * @param rarity      Ховор байдал
     * @param damage      Учруулах хохирол
     */
    public AttackCard(String name, int manaCost, String description, Rarity rarity, int damage) {
        super(name, manaCost, description, rarity);
        if (damage < 0) {
            throw new IllegalArgumentException("Хохирол сөрөг байж болохгүй.");
        }
        this.damage = damage;
    }

    @Override
    public void play(Player self, Player opponent) {
        int totalDamage = damage + self.getNextAttackBonus();
        self.resetAttackBonus();
        opponent.takeDamage(totalDamage);
        System.out.println("💥 " + self.getName() + " — " + getName()
                + " → дайсан руу " + totalDamage + " хохирол учирлаа!");
    }

    public int getDamage() { return damage; }

    @Override
    public String toString() {
        return String.format("%s (Мана: %d) — %d хохирол [%s]",
                getName(), getManaCost(), damage, getRarity());
    }
}
