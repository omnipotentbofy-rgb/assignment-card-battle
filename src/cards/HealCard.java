public class HealCard extends Card {

    private final int healAmount;

    public HealCard(String name, int manaCost, String description, Rarity rarity, int healAmount) {
        super(name, manaCost, description, rarity);
        if (healAmount < 0) {
            throw new IllegalArgumentException("Эдгээх хэмжээ сөрөг байж болохгүй.");
        }
        this.healAmount = healAmount;
    }

    @Override
    public void play(Player self, Player opponent) {
        self.heal(healAmount);
        System.out.println("💚 " + self.getName() + " — " + getName()
                + " → " + healAmount + " HP эдгэрлээ!");
    }

    public int getHealAmount() { return healAmount; }

    @Override
    public String toString() {
        return String.format("%s (Мана: %d) — %d HP эдгэрнэ [%s]",
                getName(), getManaCost(), healAmount, getRarity());
    }
}
