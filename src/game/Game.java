import java.util.ArrayList;
import java.util.List;

public class Game {

    private final Player player1;
    private final Player player2;
    private int turnNumber;
    private final ConsoleRenderer renderer;
    private final List<Turn> turnLog;

    private static final int MAX_TURNS = 100;

    public Game(Player p1, Player p2, ConsoleRenderer renderer) {
        this.player1    = p1;
        this.player2    = p2;
        this.turnNumber = 1;
        this.renderer   = renderer;
        this.turnLog    = new ArrayList<>();
    }

    public void start() {
        renderer.showAnnouncement("⚔️  CARD BATTLE ЭХЭЛЛЭЭ! " + player1.getName()
                + " vs " + player2.getName());

        while (player1.isAlive() && player2.isAlive() && turnNumber <= MAX_TURNS) {
            playTurn(player1, player2);
            if (!player2.isAlive()) break;

            playTurn(player2, player1);
            if (!player1.isAlive()) break;

            turnNumber++;
        }

        declareWinner();
    }

    private void playTurn(Player current, Player opponent) {
        Turn turn = new Turn(turnNumber, current.getName());
        current.startTurn();

        renderer.render(this);
        renderer.showAnnouncement("🎯 " + current.getName() + "-ийн ээлж эхэллээ"
                + "  (Мана: " + current.getMana() + "/" + current.getMaxMana() + ")");

        boolean turnActive = true;
        while (turnActive && current.isAlive() && opponent.isAlive()) {
            renderer.renderHand(current.getHand());
            int choice = current.chooseCard(opponent);

            if (choice == -1) {
                renderer.showAnnouncement("↩️  " + current.getName() + " ээлжээ дуусгалаа.");
                turn.logAction(current.getName() + " passed.");
                turnActive = false;
            } else {
                try {
                    Card chosen = current.getHand().get(choice);
                    renderer.animateCardPlay(chosen);
                    turn.logAction(current.getName() + " played: " + chosen.getName());
                    current.playCard(choice, opponent);

                    if (!opponent.isAlive()) break;

                    if (current instanceof AIPlayer) {
                        turnActive = false;
                    }

                } catch (InsufficientManaException e) {
                    renderer.showError("Мана хүрэлцэхгүй ээ, өөр карт сонгоно уу.");
                } catch (IllegalArgumentException e) {
                    renderer.showError("Буруу сонголт, дахин оролдоно уу.");
                }
            }
        }

        current.endTurn(opponent);
        current.getBattlefield().removeIf(c -> !c.isAlive());
        opponent.getBattlefield().removeIf(c -> !c.isAlive());
        turnLog.add(turn);
    }

    private void declareWinner() {
        renderer.render(this);

        if (!player1.isAlive() && !player2.isAlive()) {
            renderer.showWinner(null, null, turnNumber);
        } else if (!player2.isAlive()) {
            renderer.showWinner(player1, player2, turnNumber);
        } else if (!player1.isAlive()) {
            renderer.showWinner(player2, player1, turnNumber);
        } else {
            if (player1.getHp() > player2.getHp()) {
                renderer.showWinner(player1, player2, turnNumber);
            } else if (player2.getHp() > player1.getHp()) {
                renderer.showWinner(player2, player1, turnNumber);
            } else {
                renderer.showWinner(null, null, turnNumber);
            }
        }

        System.out.println("\n📜 Тоглоомын хураангуй:");
        for (Turn t : turnLog) {
            System.out.print(t);
        }
    }

    public static Deck buildDefaultDeck() {
        List<Card> cards = new ArrayList<>();

        cards.add(new AttackCard("🔥 Blazing Fireball",  3, "Галын бөмбөг — 4 хохирол",        Rarity.COMMON,    4));
        cards.add(new AttackCard("⚡ Thunder Bolt",       2, "Аянга — 3 хохирол",               Rarity.COMMON,    3));
        cards.add(new AttackCard("🗡️  Shadow Strike",    4, "Харанхуйн цохилт — 6 хохирол",    Rarity.RARE,      6));
        cards.add(new AttackCard("💀 Death Coil",        7, "Мөхлийн ороомог — 10 хохирол",    Rarity.LEGENDARY, 10));
        cards.add(new AttackCard("🏹 Precise Arrow",     1, "Оновчтой сум — 2 хохирол",        Rarity.COMMON,    2));
        cards.add(new AttackCard("🌊 Tidal Wave",        5, "Далайн долгион — 7 хохирол",      Rarity.RARE,      7));
        cards.add(new AttackCard("🔥 Blazing Fireball",  3, "Галын бөмбөг — 4 хохирол",        Rarity.COMMON,    4));
        cards.add(new AttackCard("⚡ Thunder Bolt",       2, "Аянга — 3 хохирол",               Rarity.COMMON,    3));

        cards.add(new HealCard("🍃 Morning Dew",         2, "Өглөөний шүүдэр — 4 HP",          Rarity.COMMON,    4));
        cards.add(new HealCard("🌿 Ancient Herb",        1, "Эмт ургамал — 2 HP",              Rarity.COMMON,    2));
        cards.add(new HealCard("✨ Holy Light",          4, "Ариун гэрэл — 8 HP",              Rarity.RARE,      8));
        cards.add(new HealCard("💎 Legendary Potion",    6, "Домогт эм — 15 HP",               Rarity.LEGENDARY, 15));

        cards.add(new BuffCard("⚔️  Sharpened Blade",    1, "+2 хохирол дараагийн довтолгоонд", Rarity.COMMON,    2));
        cards.add(new BuffCard("🔮 Power Surge",         3, "+5 хохирол дараагийн довтолгоонд", Rarity.RARE,      5));
        cards.add(new BuffCard("⚔️  Sharpened Blade",    1, "+2 хохирол дараагийн довтолгоонд", Rarity.COMMON,    2));

        cards.add(new CreatureCard("🐺 Goblin Wolf",     2, "Хурдан дайчин",                   Rarity.COMMON,    3, 2));
        cards.add(new CreatureCard("🦅 Storm Eagle",     3, "Агаарын дайчин",                  Rarity.RARE,      4, 3));
        cards.add(new CreatureCard("🐲 Ancient Dragon",  8, "Домогт луу",                      Rarity.LEGENDARY, 8, 8));
        cards.add(new CreatureCard("🐺 Goblin Wolf",     2, "Хурдан дайчин",                   Rarity.COMMON,    3, 2));
        cards.add(new CreatureCard("🦅 Storm Eagle",     3, "Агаарын дайчин",                  Rarity.RARE,      4, 3));

        Deck deck = new Deck(cards);
        deck.shuffle();
        return deck;
    }

    public Player getPlayer1()       { return player1; }
    public Player getPlayer2()       { return player2; }
    public int getTurnNumber()       { return turnNumber; }
    public void setTurnNumber(int n) { this.turnNumber = n; }
    public List<Turn> getTurnLog()   { return turnLog; }

    public Player getCurrentPlayer() {
        return (turnNumber % 2 == 1) ? player1 : player2;
    }
}
