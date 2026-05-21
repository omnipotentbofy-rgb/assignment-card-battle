import java.util.ArrayList;
import java.util.List;

/**
 * Battlefield — Хоёр талын creature-уудыг хадгалах класс (Stretch).
 *
 * CreatureCard-ууд энд байрлана — ээлж бүрт довтолж, хохирол авна.
 */
public class Battlefield {

    private final List<CreatureCard> player1Creatures;
    private final List<CreatureCard> player2Creatures;

    public Battlefield() {
        this.player1Creatures = new ArrayList<>();
        this.player2Creatures = new ArrayList<>();
    }

    /**
     * Тоглогчийн creature-г тулааны талбарт нэмнэ.
     */
    public void summon(Player owner, Player player1, CreatureCard card) {
        if (owner == player1) {
            player1Creatures.add(card);
        } else {
            player2Creatures.add(card);
        }
    }

    /**
     * Attacker-ийн бүх creature нь defender руу дайрна.
     */
    public void resolveAttacks(Player attacker, Player defender,
                                List<CreatureCard> attackerCreatures) {
        for (CreatureCard creature : new ArrayList<>(attackerCreatures)) {
            if (creature.isAlive() && defender.isAlive()) {
                creature.attack(defender);
            }
        }
    }

    /**
     * HP ≤ 0 болсон creature-уудыг хоёр талаас цэвэрлэнэ.
     */
    public void removeDead() {
        player1Creatures.removeIf(c -> !c.isAlive());
        player2Creatures.removeIf(c -> !c.isAlive());
    }

    public List<CreatureCard> getPlayer1Creatures() { return player1Creatures; }
    public List<CreatureCard> getPlayer2Creatures() { return player2Creatures; }
}
