/**
 * AttackCard extends Card — дайсан руу хохирол өгөх.
 */
public class AttackCard extends Card {
    private int damage;

    /**
     * Constructor.
     */
    public AttackCard(String name, int manaCost, String description, Rarity rarity, int damage) {
        super(name, manaCost, description, rarity);
        if (damage < 0) {
            throw new IllegalArgumentException("Хохирол сөрөг байж болохгүй!");
        }
        this.damage = damage;
    }

    public int getDamage() {
        return damage;
    }

    @Override
    public void play(Player self, Player opponent) {
        // BuffCard-ийн бонус нэмэх
        int actualDamage = damage + self.getNextAttackBonus();
        opponent.takeDamage(actualDamage);
        System.out.println("💥 " + self.getName() + " " + name + " тоглолоо! " + opponent.getName() + " " + actualDamage + " хохирол авлаа!");
        self.resetAttackBonus(); // Buff хэрэгдүүлэх
    }
}
