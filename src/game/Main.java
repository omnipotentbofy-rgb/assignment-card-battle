public class Main {

    public static void main(String[] args) {
        ConsoleRenderer renderer = new ConsoleRenderer();
        InputReader reader       = new InputReader();
        GameSaver saver          = new GameSaver();

        renderer.showAnnouncement("⚔️  CARD BATTLE-д тавтай морил!");
        System.out.println("  Hearthstone-оос санаа авсан консолын карт тулааны тоглоом.");
        System.out.println();

        String playerName = reader.readString("  Таны нэрийг оруулна уу: ");
        if (playerName.isBlank()) playerName = "Баатар";

        java.io.File saveFile = new java.io.File("save.txt");
        if (saveFile.exists()) {
            boolean loadSave = reader.readYesNo("  Хадгалсан тоглоом байна. Үргэлжлүүлэх үү?");
            if (loadSave) {
                try {
                    Game loaded = saver.load("save.txt", renderer, reader);
                    loaded.start();
                    reader.close();
                    return;
                } catch (Exception e) {
                    renderer.showError("Save файл ачаалахад алдаа гарлаа: " + e.getMessage());
                    System.out.println("  Шинэ тоглоом эхэлнэ...");
                }
            }
        }

        Deck playerDeck = Game.buildDefaultDeck();
        Deck aiDeck     = Game.buildDefaultDeck();

        HumanPlayer human = new HumanPlayer(playerName, playerDeck, reader);
        AIPlayer    ai    = new AIPlayer("🤖 AI", aiDeck);

        Game game = new Game(human, ai, renderer);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                saver.save(game, "save.txt");
            } catch (Exception ignored) {}
        }));

        game.start();

        boolean again = reader.readYesNo("  Дахин тоглох уу?");
        if (again) {
            saveFile.delete();
            main(args);
        } else {
            renderer.showAnnouncement("Баяртай! Дараа уулзая 👋");
        }

        reader.close();
    }
}
