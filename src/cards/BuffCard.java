public class BuffCard extends Card {

    private final int buffAmount;

    public BuffCard(String name, int manaCost, String description, Rarity rarity, int buffAmount) {
        super(name, manaCost, description, rarity);
        if (buffAmount < 0) {
            throw new IllegalArgumentException("Buff хэмжээ сөрөг байж болохгүй.");
        }
        this.buffAmount = buffAmount;
    }

    @Override
    public void play(Player self, Player opponent) {
        self.addAttackBuff(buffAmount);
        System.out.println("⚔️  " + self.getName() + " — " + getName()
                + " → дараагийн довтолгоонд +" + buffAmount + " хохирол нэмэгдлээ!");
    }

    public int getBuffAmount() { return buffAmount; }

    @Override
    public String toString() {
        return String.format("%s (Мана: %d) — +%d хохирол buff [%s]",
                getName(), getManaCost(), buffAmount, getRarity());
    }
}
