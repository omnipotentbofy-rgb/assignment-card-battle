/**
 * Game class — тоглоомын loop, ээлж солих, win condition.
 */
public class Game {
    private Player player1;
    private Player player2;
    private int turnNumber = 0;
    private ConsoleRenderer renderer;

    /**
     * Constructor.
     */
    public Game(Player p1, Player p2) {
        this.player1 = p1;
        this.player2 = p2;
        this.renderer = new ConsoleRenderer();
    }

    /**
     * start() — тоглоомын үндсэн loop.
     */
    public void start() {
        System.out.println("\n🎴 Card Battle эхлэлээ! ⚔️\n");
        
        Player currentPlayer = player1;
        Player opponent = player2;

        while (player1.isAlive() && player2.isAlive()) {
            turnNumber++;
            System.out.println("\n" + "=".repeat(50));
            System.out.println("📍 Ээлж " + turnNumber + " — " + currentPlayer.getName() + "-ийн ээлж");
            System.out.println("=".repeat(50));

            currentPlayer.startTurn();
            renderer.render(this);

            // Тоглогч картыг тоглох
            boolean turnEnded = false;
            while (!turnEnded) {
                int choice = currentPlayer.chooseCard(opponent);

                if (choice == -1) {
                    System.out.println("✋ " + currentPlayer.getName() + " ээлжээ дуусгалаа.");
                    turnEnded = true;
                } else {
                    try {
                        currentPlayer.playCard(choice, opponent);
                    } catch (InsufficientManaException e) {
                        System.out.println("⚠️ Мана хүрэлцэхгүй ээ, өөр карт сонгоно уу.");
                    } catch (IllegalArgumentException e) {
                        System.out.println("❌ " + e.getMessage());
                    }
                }
            }

            currentPlayer.endTurn();

            // Win condition шалгах
            if (!opponent.isAlive()) {
                break;
            }

            // Ээлж солих
            Player temp = currentPlayer;
            currentPlayer = opponent;
            opponent = temp;
        }

        declareWinner();
    }

    /**
     * declareWinner() — ялагчийг зарлах.
     */
    private void declareWinner() {
        System.out.println("\n" + "=".repeat(50));
        if (player1.isAlive()) {
            System.out.println("🏆 " + player1.getName() + " ЯЛАЛТ ЧИНИЙ ГАРТ! 🏆");
        } else {
            System.out.println("🏆 " + player2.getName() + " ЯЛАЛТ ЧИНИЙ ГАРТ! 🏆");
        }
        System.out.println("=".repeat(50));
    }

    // Getters
    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public int getTurnNumber() {
        return turnNumber;
    }
}
