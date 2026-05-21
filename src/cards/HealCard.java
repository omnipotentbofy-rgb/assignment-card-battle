/**
 * HealCard extends Card — өөрийгөө эдгээх.
 */
public class HealCard extends Card {
    private int healAmount;

    /**
     * Constructor.
     */
    public HealCard(String name, int manaCost, String description, Rarity rarity, int healAmount) {
        super(name, manaCost, description, rarity);
        if (healAmount < 0) {
            throw new IllegalArgumentException("Эдгээх хэмжээ сөрөг байж болохгүй!");
        }
        this.healAmount = healAmount;
    }

    public int getHealAmount() {
        return healAmount;
    }

    @Override
    public void play(Player self, Player opponent) {
        self.heal(healAmount);
        System.out.println("🍀 " + self.getName() + " " + name + " тоглолоо! " + healAmount + " HP сэргүүллээ!");
    }
}
