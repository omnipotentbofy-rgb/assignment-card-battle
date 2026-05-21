import java.util.List;

public class ConsoleRenderer {

    private static final String RESET  = "\u001B[0m";
    private static final String RED    = "\u001B[91m";
    private static final String GREEN  = "\u001B[92m";
    private static final String YELLOW = "\u001B[93m";
    private static final String CYAN   = "\u001B[96m";
    private static final String BOLD   = "\u001B[1m";
    private static final String DIM    = "\u001B[2m";

    public void render(Game game) {
        clearScreen();
        renderBanner(game.getTurnNumber(), game.getCurrentPlayer().getName());
        System.out.println();
        renderPlayer(game.getPlayer2());
        renderCreatures(game.getPlayer2());
        System.out.println(DIM + "  ─────────────────────────────────────────────────" + RESET);
        renderCreatures(game.getPlayer1());
        renderPlayer(game.getPlayer1());
        System.out.println();
    }

    private void renderBanner(int turn, String playerName) {
        System.out.println(BOLD + CYAN);
        System.out.println("  ╔══════════════════════════════════════════╗");
        System.out.printf ("  ║   ⚔️   CARD BATTLE   ⚔️                  ║%n");
        System.out.printf ("  ║   Ээлж %-3d  —  %-20s    ║%n", turn, playerName + "-ийн ээлж");
        System.out.println("  ╚══════════════════════════════════════════╝");
        System.out.print(RESET);
    }

    public void renderPlayer(Player p) {
        String hpBar   = buildBar(p.getHp(),  p.getMaxHp(),  20, '█', '░');
        String manaBar = buildBar(p.getMana(), p.getMaxMana(), 10, '◆', '◇');
        String hpColor = p.getHp() <= 10 ? RED : GREEN;

        System.out.printf("  %s%-16s%s  HP: %s%s%s %d/%d   Мана: %s%s%s %d/%d%n",
            BOLD, p.getName(), RESET,
            hpColor, hpBar, RESET, p.getHp(), p.getMaxHp(),
            CYAN, manaBar, RESET, p.getMana(), p.getMaxMana()
        );

        if (p.getNextAttackBonus() > 0) {
            System.out.printf("  %s  ⚔️  Buff идэвхтэй: +%d хохирол дараагийн довтолгоонд%s%n",
                YELLOW, p.getNextAttackBonus(), RESET);
        }
    }

    private void renderCreatures(Player p) {
        List<CreatureCard> creatures = p.getBattlefield();
        if (creatures.isEmpty()) return;
        System.out.print("  " + DIM + p.getName() + "-ийн амьтад: " + RESET);
        for (CreatureCard c : creatures) {
            System.out.printf("[%s HP:%d ATK:%d] ", c.getName(), c.getHealth(), c.getAttackPower());
        }
        System.out.println();
    }

    public void renderHand(List<Card> hand) {
        if (hand.isEmpty()) {
            System.out.println("  (Гарт карт байхгүй)");
            return;
        }
        System.out.println();
        int cols = Math.min(hand.size(), 5);
        String[] lines = new String[6];
        for (int i = 0; i < lines.length; i++) lines[i] = "";

        for (int i = 0; i < cols; i++) {
            String[] box = buildCardBox(i, hand.get(i));
            for (int l = 0; l < 6; l++) lines[l] += box[l] + "  ";
        }
        for (String line : lines) System.out.println("  " + line);

        if (hand.size() > cols) {
            System.out.println();
            String[] lines2 = new String[6];
            for (int i = 0; i < lines2.length; i++) lines2[i] = "";
            for (int i = cols; i < hand.size(); i++) {
                String[] box = buildCardBox(i, hand.get(i));
                for (int l = 0; l < 6; l++) lines2[l] += box[l] + "  ";
            }
            for (String line : lines2) System.out.println("  " + line);
        }
        System.out.println();
    }

    private String[] buildCardBox(int index, Card c) {
        String rarityColor = switch (c.getRarity()) {
            case LEGENDARY -> YELLOW;
            case RARE      -> CYAN;
            default        -> RESET;
        };
        String name  = truncate(c.getName(), 14);
        String mana  = "Мана: " + c.getManaCost();
        String extra = "";
        if (c instanceof AttackCard ac)        extra = "DMG:  " + ac.getDamage();
        else if (c instanceof HealCard hc)     extra = "Heal: " + hc.getHealAmount();
        else if (c instanceof BuffCard bc)     extra = "Buff: +" + bc.getBuffAmount();
        else if (c instanceof CreatureCard cc) extra = "HP/ATK:" + cc.getHealth() + "/" + cc.getAttackPower();

        return new String[]{
            String.format("┌──────────────────┐"),
            String.format("│[%d] %-14s│", index, name),
            String.format("│ %-16s │", mana),
            String.format("│ %-16s │", extra),
            String.format("│ %s%-14s%s │", rarityColor, "[" + c.getRarity() + "]", RESET),
            String.format("└──────────────────┘")
        };
    }

    public void showError(String message) {
        System.out.println(RED + "  ✗ " + message + RESET);
    }

    public void showAnnouncement(String message) {
        System.out.println();
        System.out.println(BOLD + YELLOW + "  ★ " + message + RESET);
        System.out.println();
    }

    public void showWinner(Player winner, Player loser, int totalTurns) {
        System.out.println();
        System.out.println(BOLD + YELLOW);
        System.out.println("  ╔══════════════════════════════════════════╗");
        System.out.println("  ║           🏆  ТОГЛООМ ДУУСЛАА  🏆         ║");
        System.out.println("  ╠══════════════════════════════════════════╣");
        if (winner != null) {
            System.out.printf("  ║   🎉 %-36s ║%n", winner.getName() + " ХОЖЛОО!");
            System.out.printf("  ║   💔 %-36s ║%n", loser.getName() + " ялагдлаа.");
        } else {
            System.out.println("  ║           🤝  ТЭНЦЛЭЭ!                   ║");
        }
        System.out.printf("  ║   ⚔️  Нийт %-3d ээлж өнгөрлөө            ║%n", totalTurns);
        System.out.println("  ╚══════════════════════════════════════════╝");
        System.out.print(RESET);
        System.out.println();
    }

    public void animateCardPlay(Card card) {
        System.out.println();
        System.out.println(BOLD + "  ✨ " + card.getName() + " тоглогдож байна..." + RESET);
        try {
            Thread.sleep(300);
            System.out.println("     ╔══════════╗");
            Thread.sleep(150);
            System.out.println("     ║  " + YELLOW + "★★★★★★" + RESET + "  ║");
            Thread.sleep(150);
            System.out.println("     ╚══════════╝");
            Thread.sleep(300);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }

    private String buildBar(int current, int max, int width, char full, char empty) {
        if (max <= 0) return String.valueOf(empty).repeat(width);
        int filled = (int) Math.round((double) current / max * width);
        filled = Math.max(0, Math.min(filled, width));
        return String.valueOf(full).repeat(filled) + String.valueOf(empty).repeat(width - filled);
    }

    private String truncate(String s, int maxLen) {
        if (s == null) return "";
        return s.length() <= maxLen ? s : s.substring(0, maxLen - 1) + "…";
    }

    private void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
