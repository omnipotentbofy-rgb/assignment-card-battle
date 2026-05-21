import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * GameSaver — Тоглоомын төлөвийг файлд хадгалах ба буцааж унших (Stretch).
 *
 * Формат (text):
 *   # Card Battle Save v1
 *   TURN=5
 *   P1=Bat,hp=18,mana=4,maxMana=5
 *   P1_HAND=Fireball:3:ATTACK:4:COMMON;Morning Dew:2:HEAL:3:COMMON
 *   P2=AI,hp=22,mana=5,maxMana=5
 *   P2_HAND=...
 */
public class GameSaver {

    private static final String SAVE_VERSION = "# Card Battle Save v1";

    /**
     * Тоглоомын төлөвийг файлд хадгална.
     *
     * @param game     Хадгалах тоглоом
     * @param filepath Файлын зам
     * @throws IOException файл бичих алдаа
     */
    public void save(Game game, String filepath) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filepath))) {
            bw.write(SAVE_VERSION);
            bw.newLine();
            bw.write("TURN=" + game.getTurnNumber());
            bw.newLine();

            writePlayer(bw, "P1", game.getPlayer1());
            writePlayer(bw, "P2", game.getPlayer2());
        }
        System.out.println("💾 Тоглоом хадгалагдлаа: " + filepath);
    }

    private void writePlayer(BufferedWriter bw, String prefix, Player p) throws IOException {
        bw.write(prefix + "=" + p.getName()
                + ",hp=" + p.getHp()
                + ",mana=" + p.getMana()
                + ",maxMana=" + p.getMaxMana());
        bw.newLine();

        // Hand-ийн картуудыг бичих
        StringBuilder handSb = new StringBuilder(prefix + "_HAND=");
        List<Card> hand = p.getHand();
        for (int i = 0; i < hand.size(); i++) {
            handSb.append(encodeCard(hand.get(i)));
            if (i < hand.size() - 1) handSb.append(";");
        }
        bw.write(handSb.toString());
        bw.newLine();
    }

    private String encodeCard(Card c) {
        // Format: name:manaCost:TYPE:value:rarity
        String type;
        int value;
        if (c instanceof AttackCard ac)       { type = "ATTACK";   value = ac.getDamage(); }
        else if (c instanceof HealCard hc)    { type = "HEAL";     value = hc.getHealAmount(); }
        else if (c instanceof BuffCard bc)    { type = "BUFF";     value = bc.getBuffAmount(); }
        else if (c instanceof CreatureCard cc){ type = "CREATURE"; value = cc.getAttackPower(); }
        else                                  { type = "UNKNOWN";  value = 0; }

        return escapeSemicolon(c.getName()) + ":"
                + c.getManaCost() + ":"
                + type + ":"
                + value + ":"
                + c.getRarity().name();
    }

    /**
     * Файлаас тоглоомын төлөвийг буцааж уншина.
     *
     * @param filepath Файлын зам
     * @param renderer ConsoleRenderer (Game-д шаардлагатай)
     * @param reader   InputReader (HumanPlayer-т шаардлагатай)
     * @return Буцаасан Game объект
     * @throws IOException            файл унших алдаа
     * @throws CorruptSaveException   файл гэмтсэн бол
     */
    public Game load(String filepath, ConsoleRenderer renderer, InputReader reader)
            throws IOException {
        File file = new File(filepath);
        if (!file.exists()) {
            throw new FileNotFoundException("Save файл олдсонгүй: " + filepath);
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String firstLine = br.readLine();
            if (firstLine == null || !firstLine.equals(SAVE_VERSION)) {
                throw new CorruptSaveException("Save файлын версо таарахгүй байна.");
            }

            int turnNumber = 0;
            String p1Name = "Player1"; int p1Hp = 30; int p1Mana = 3; int p1MaxMana = 3;
            String p2Name = "AI";      int p2Hp = 30; int p2Mana = 3; int p2MaxMana = 3;
            List<Card> p1HandCards = new ArrayList<>();
            List<Card> p2HandCards = new ArrayList<>();

            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("TURN=")) {
                    turnNumber = Integer.parseInt(line.substring(5));
                } else if (line.startsWith("P1=")) {
                    String[] parts = line.substring(3).split(",");
                    p1Name = parts[0];
                    for (String part : parts) {
                        if (part.startsWith("hp="))      p1Hp      = Integer.parseInt(part.substring(3));
                        if (part.startsWith("mana="))    p1Mana    = Integer.parseInt(part.substring(5));
                        if (part.startsWith("maxMana=")) p1MaxMana = Integer.parseInt(part.substring(8));
                    }
                } else if (line.startsWith("P2=")) {
                    String[] parts = line.substring(3).split(",");
                    p2Name = parts[0];
                    for (String part : parts) {
                        if (part.startsWith("hp="))      p2Hp      = Integer.parseInt(part.substring(3));
                        if (part.startsWith("mana="))    p2Mana    = Integer.parseInt(part.substring(5));
                        if (part.startsWith("maxMana=")) p2MaxMana = Integer.parseInt(part.substring(8));
                    }
                } else if (line.startsWith("P1_HAND=")) {
                    p1HandCards = parseHand(line.substring(8));
                } else if (line.startsWith("P2_HAND=")) {
                    p2HandCards = parseHand(line.substring(8));
                }
            }

            // Rebuild players with empty decks (hand cards restored separately)
            Deck emptyDeck1 = new Deck(new ArrayList<>());
            Deck emptyDeck2 = new Deck(new ArrayList<>());

            HumanPlayer p1 = new HumanPlayer(p1Name, emptyDeck1, reader);
            AIPlayer p2 = new AIPlayer(p2Name, emptyDeck2);

            // Restore state via reflection-free approach: use setters via hand manipulation
            // Since we can't set HP directly, the saved game starts from restored state
            // We apply damage to reach saved HP
            int p1DmgToApply = 30 - p1Hp;
            if (p1DmgToApply > 0) p1.takeDamage(p1DmgToApply);
            int p2DmgToApply = 30 - p2Hp;
            if (p2DmgToApply > 0) p2.takeDamage(p2DmgToApply);

            // Clear auto-drawn hand, add saved hand
            p1.getHand().clear();
            p1.getHand().addAll(p1HandCards);
            p2.getHand().clear();
            p2.getHand().addAll(p2HandCards);

            Game game = new Game(p1, p2, renderer);
            game.setTurnNumber(turnNumber);
            System.out.println("📂 Тоглоом ачаалагдлаа: " + filepath);
            return game;

        } catch (NumberFormatException e) {
            throw new CorruptSaveException("Save файлын тоо буруу: " + e.getMessage());
        }
    }

    private List<Card> parseHand(String encoded) {
        List<Card> cards = new ArrayList<>();
        if (encoded == null || encoded.isBlank()) return cards;
        String[] parts = encoded.split(";");
        for (String part : parts) {
            if (part.isBlank()) continue;
            String[] tokens = part.split(":");
            if (tokens.length < 5) continue;
            String name     = unescapeSemicolon(tokens[0]);
            int manaCost    = Integer.parseInt(tokens[1]);
            String type     = tokens[2];
            int value       = Integer.parseInt(tokens[3]);
            Rarity rarity   = Rarity.valueOf(tokens[4]);

            Card card = switch (type) {
                case "ATTACK"   -> new AttackCard(name, manaCost, "", rarity, value);
                case "HEAL"     -> new HealCard(name, manaCost, "", rarity, value);
                case "BUFF"     -> new BuffCard(name, manaCost, "", rarity, value);
                case "CREATURE" -> new CreatureCard(name, manaCost, "", rarity, 3, value);
                default         -> new AttackCard(name, manaCost, "", rarity, 1);
            };
            cards.add(card);
        }
        return cards;
    }

    private String escapeSemicolon(String s) { return s.replace(";", "\\;").replace(":", "\\:"); }
    private String unescapeSemicolon(String s){ return s.replace("\\:", ":").replace("\\;", ";"); }
}
