import java.util.ArrayList;
import java.util.List;

/**
 * Main — тоглоомыг эхлүүлэх.
 */
public class Main {
    public static void main(String[] args) {
        // 20 картны колода үүсгэх
        List<Card> deck1Cards = createDeck();
        List<Card> deck2Cards = createDeck();

        // Deck, Player үүсгэх
        Deck deck1 = new Deck(deck1Cards);
        Deck deck2 = new Deck(deck2Cards);

        Player player1 = new HumanPlayer("Та (дайчин)", deck1);
        Player player2 = new AIPlayer("AI (машин дух)", deck2);

        // Тоглоом эхлүүлэх
        Game game = new Game(player1, player2);
        game.start();
    }

    /**
     * createDeck() — 20 картны стандарт колода үүсгэх.
     */
    private static List<Card> createDeck() {
        List<Card> deck = new ArrayList<>();

        // AttackCard-ууд (6)
        deck.add(new AttackCard("🔥 Fireball", 3, "Галын бөмбөг — 4 хохирол", Rarity.COMMON, 4));
        deck.add(new AttackCard("⚡ Lightning Bolt", 2, "Салхины идэвхжүүлэлт — 3 хохирол", Rarity.COMMON, 3));
        deck.add(new AttackCard("🗡️ Sword Slash", 2, "Сэлмээр сонилцох — 2 хохирол", Rarity.COMMON, 2));
        deck.add(new AttackCard("💀 Death Strike", 7, "Үхлийн цохилт — 10 хохирол", Rarity.LEGENDARY, 10));
        deck.add(new AttackCard("🏹 Arrow Shot", 1, "Сумаар цохих — 1 хохирол", Rarity.COMMON, 1));
        deck.add(new AttackCard("🌊 Tidal Wave", 4, "Сүрэгжүүлсэн долгион — 5 хохирол", Rarity.RARE, 5));

        // HealCard-ууд (6)
        deck.add(new HealCard("🍀 Morning Dew", 1, "Өглөөний сүүлтэй — 2 HP сэргүүлэх", Rarity.COMMON, 2));
        deck.add(new HealCard("✨ Holy Light", 3, "Ариун гэрэл — 5 HP сэргүүлэх", Rarity.RARE, 5));
        deck.add(new HealCard("🌲 Forest Blessing", 2, "Ойсын асран сүтгэл — 3 HP", Rarity.COMMON, 3));
        deck.add(new HealCard("💎 Crystal Renewal", 5, "Болор нөөрөслөлт — 8 HP сэргүүлэх", Rarity.LEGENDARY, 8));
        deck.add(new HealCard("🌙 Moonlight Heal", 2, "Сарны гялбалт — 3 HP", Rarity.COMMON, 3));
        deck.add(new HealCard("❤️ Heart Mend", 1, "Дүрэм засах — 1 HP", Rarity.COMMON, 1));

        // BuffCard-ууд (5)
        deck.add(new BuffCard("⚔️ Sharpened Blade", 1, "Үй сүлхүүр байлалт — +2 damage", Rarity.COMMON, 2));
        deck.add(new BuffCard("🔱 Trident Power", 2, "Гурван хөлт идэвхжүүлэлт — +3 damage", Rarity.RARE, 3));
        deck.add(new BuffCard("⚡ Chaos Energy", 3, "Эмх замбараа эрч — +4 damage", Rarity.RARE, 4));
        deck.add(new BuffCard("🌪️ Whirlwind", 2, "Эргүүлэг салхи — +2 damage", Rarity.COMMON, 2));
        deck.add(new BuffCard("🌟 Starlight Bless", 4, "Одны ариун сэнэс — +5 damage", Rarity.LEGENDARY, 5));

        // CreatureCard-ууд (3) - Stretch
        deck.add(new CreatureCard("🐺 Goblin Wolf", 2, "Хөгжмийн чулуу үслэг — 2 HP / 2 ATK", Rarity.COMMON, 2, 2));
        deck.add(new CreatureCard("🦅 Forest Eagle", 3, "Ойсын арслан — 3 HP / 3 ATK", Rarity.RARE, 3, 3));
        deck.add(new CreatureCard("🐲 Ancient Dragon", 8, "Домогт луу — 8 HP / 8 ATK", Rarity.LEGENDARY, 8, 8));

        return deck;
    }
}
