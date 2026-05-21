/**
 * BuffCard extends Card — дараагийн attack-ын damage-ыг нэмэх.
 */
public class BuffCard extends Card {
    private int buffAmount;

    /**
     * Constructor.
     */
    public BuffCard(String name, int manaCost, String description, Rarity rarity, int buffAmount) {
        super(name, manaCost, description, rarity);
        if (buffAmount < 0) {
            throw new IllegalArgumentException("Buff хэмжээ сөрөг байж болохгүй!");
        }
        this.buffAmount = buffAmount;
    }

    public int getBuffAmount() {
        return buffAmount;
    }

    @Override
    public void play(Player self, Player opponent) {
        self.addAttackBuff(buffAmount);
        System.out.println("⚔️ " + self.getName() + " " + name + " тоглолоо! Дараагийн attack +" + buffAmount + " damage!");
    }
}
