/**
 * CreatureCard extends Card — тулааны талбарт үлдэж, ээлж бүрт довтолно (Stretch).
 */
public class CreatureCard extends Card {
    private int health;
    private int attackPower;

    /**
     * Constructor.
     */
    public CreatureCard(String name, int manaCost, String description, Rarity rarity, int health, int attackPower) {
        super(name, manaCost, description, rarity);
        if (health < 0 || attackPower < 0) {
            throw new IllegalArgumentException("Health/attack сөрөг байж болохгүй!");
        }
        this.health = health;
        this.attackPower = attackPower;
    }

    public int getHealth() {
        return health;
    }

    public int getAttackPower() {
        return attackPower;
    }

    public void takeDamage(int amount) {
        health = Math.max(0, health - amount);
    }

    public boolean isAlive() {
        return health > 0;
    }

    @Override
    public void play(Player self, Player opponent) {
        System.out.println("🐺 " + self.getName() + " " + name + " нэрвэгдүүллээ!");
    }
}
