/**
 * ConsoleRenderer — тоглоомын төлөвийг консолд зурах.
 */
public class ConsoleRenderer {
    /**
     * render() — дэлгэцийг шинэчлэх.
     */
    public void render(Game game) {
        Player p1 = game.getPlayer1();
        Player p2 = game.getPlayer2();

        System.out.println("\n╔═══════════════════════════════════════════╗");
        System.out.println("║           ⚔️ CARD BATTLE ⚔️              ║");
        System.out.println("╚═══════════════════════════════════════════╝\n");

        renderPlayer(p1);
        System.out.println();
        renderPlayer(p2);
        System.out.println();
    }

    /**
     * renderPlayer() — нэг тоглогчийн төлөвийг зурах.
     */
    public void renderPlayer(Player p) {
        // HP bar
        int hpBar = (p.getHp() * 10) / p.getMaxHp();
        String hpVisual = "█".repeat(hpBar) + "░".repeat(10 - hpBar);

        // Mana bar
        int manaBar = (p.getMana() * 10) / p.getMaxMana();
        String manaVisual = "◆".repeat(manaBar) + "◇".repeat(10 - manaBar);

        System.out.printf("📊 %s%n", p.getName());
        System.out.printf("   HP:   [%s] %d/%d%n", hpVisual, p.getHp(), p.getMaxHp());
        System.out.printf("   Мана: [%s] %d/%d%n", manaVisual, p.getMana(), p.getMaxMana());
        System.out.printf("   Гар:  %d карт%n", p.getHand().size());
    }

    /**
     * showError() — алдааны мессеж харуулах.
     */
    public void showError(String message) {
        System.out.println("❌ " + message);
    }

    /**
     * showAnnouncement() — юу нэг зарлалт.
     */
    public void showAnnouncement(String message) {
        System.out.println("📢 " + message);
    }
}
