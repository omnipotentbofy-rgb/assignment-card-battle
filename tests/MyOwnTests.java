import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

@DisplayName("MyOwnTests — Card Battle бодит ажиллагааны тестүүд")
public class MyOwnTests {

    // ── Туслах: тоглогч үүсгэх (хоосон deck, гар хоосон) ──────────────────
    private AIPlayer makePlayer(String name) {
        Deck empty = new Deck(new ArrayList<>());
        AIPlayer p = new AIPlayer(name, empty);
        p.getHand().clear(); // constructor-д 5 карт татна — цэвэрлэх
        return p;
    }

    private AIPlayer makePlayerWith(String name, List<Card> cards) {
        AIPlayer p = makePlayer(name);
        p.getHand().addAll(cards);
        return p;
    }

    // ══════════════════════════════════════════════════════════════════════
    // 1. AttackCard
    // ══════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("AttackCard тестүүд")
    class AttackCardTests {

        @Test
        @DisplayName("AttackCard тоглоход дайсны HP буурна")
        void attackCardDealsCorrectDamage() {
            AIPlayer attacker = makePlayer("Attacker");
            AIPlayer defender = makePlayer("Defender");

            AttackCard fireball = new AttackCard("Fireball", 0, "", Rarity.COMMON, 8);
            attacker.getHand().add(fireball);

            int hpBefore = defender.getHp(); // 30
            attacker.playCard(0, defender);

            assertEquals(hpBefore - 8, defender.getHp(),
                "8 хохирлын карт тоглоход дайсны HP 8-аар буурах ёстой");
        }

        @Test
        @DisplayName("AttackCard тоглосны дараа гараас хасагдана")
        void attackCardRemovedFromHandAfterPlay() {
            AIPlayer attacker = makePlayer("A");
            AIPlayer defender = makePlayer("B");

            attacker.getHand().add(new AttackCard("Arrow", 0, "", Rarity.COMMON, 2));
            assertEquals(1, attacker.getHand().size());

            attacker.playCard(0, defender);

            assertEquals(0, attacker.getHand().size(),
                "Тоглогдсон карт гараас хасагдах ёстой");
        }

        @Test
        @DisplayName("AttackCard буфтэй хамт тоглоход нийт хохирол нэмэгдэнэ")
        void attackCardAppliesBuffBonus() {
            AIPlayer attacker = makePlayer("A");
            AIPlayer defender = makePlayer("B");

            attacker.addAttackBuff(5);
            AttackCard sword = new AttackCard("Sword", 0, "", Rarity.RARE, 4);
            attacker.getHand().add(sword);

            attacker.playCard(0, defender);

            assertEquals(30 - 9, defender.getHp(),
                "Buff(5) + damage(4) = 9 нийт хохирол байх ёстой");
        }

        @Test
        @DisplayName("AttackCard тоглосны дараа buff дахин хэрэглэгдэхгүй")
        void buffResetAfterAttack() {
            AIPlayer attacker = makePlayer("A");
            AIPlayer defender = makePlayer("B");

            attacker.addAttackBuff(10);
            attacker.getHand().add(new AttackCard("First", 0, "", Rarity.COMMON, 1));
            attacker.getHand().add(new AttackCard("Second", 0, "", Rarity.COMMON, 1));

            attacker.playCard(0, defender); // 1+10 = 11
            attacker.playCard(0, defender); // 1+0 = 1 (buff ашигласан)

            assertEquals(30 - 12, defender.getHp(),
                "Buff зөвхөн нэг удаа ашиглагдана — нийт 11+1=12 хохирол");
        }

        @Test
        @DisplayName("AttackCard-д сөрөг damage оруулвал exception гарна")
        void attackCardNegativeDamageThrows() {
            assertThrows(IllegalArgumentException.class,
                () -> new AttackCard("Bad", 0, "", Rarity.COMMON, -1),
                "Сөрөг damage-тай AttackCard үүсгэвэл exception гарах ёстой");
        }

        @Test
        @DisplayName("AttackCard HP-г 0-ээс доошгүй бууруулна (overkill)")
        void attackCardDoesNotGoNegativeHp() {
            AIPlayer attacker = makePlayer("A");
            AIPlayer defender = makePlayer("B");

            attacker.getHand().add(new AttackCard("Nuke", 0, "", Rarity.LEGENDARY, 999));
            attacker.playCard(0, defender);

            assertEquals(0, defender.getHp(), "HP 0-ээс доош буурахгүй");
            assertFalse(defender.isAlive(), "999 хохирлын дараа тоглогч амьгүй байх ёстой");
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    // 2. HealCard
    // ══════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("HealCard тестүүд")
    class HealCardTests {

        @Test
        @DisplayName("HealCard тоглоход HP нэмэгдэнэ")
        void healCardRestoresHp() {
            AIPlayer player = makePlayer("P");
            AIPlayer dummy = makePlayer("D");

            player.takeDamage(10); // HP: 20
            player.getHand().add(new HealCard("Herb", 0, "", Rarity.COMMON, 6));

            player.playCard(0, dummy);

            assertEquals(26, player.getHp(), "10 хохирлын дараа 6 эдгээхэд HP 26 байх ёстой");
        }

        @Test
        @DisplayName("HealCard HP-г maxHp-аас хэтрүүлэхгүй")
        void healCardDoesNotExceedMaxHp() {
            AIPlayer player = makePlayer("P");
            AIPlayer dummy = makePlayer("D");

            // HP бүрэн (30), 20 HP heal хийх гэвэл 30-аас хэтрэхгүй
            player.getHand().add(new HealCard("BigHeal", 0, "", Rarity.LEGENDARY, 20));
            player.playCard(0, dummy);

            assertEquals(30, player.getHp(), "HP maxHp (30)-аас хэтрэхгүй байх ёстой");
        }

        @Test
        @DisplayName("HealCard-д сөрөг healAmount оруулвал exception гарна")
        void healCardNegativeAmountThrows() {
            assertThrows(IllegalArgumentException.class,
                () -> new HealCard("Bad", 0, "", Rarity.COMMON, -5),
                "Сөрөг healAmount-тай HealCard үүсгэвэл exception гарах ёстой");
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    // 3. BuffCard
    // ══════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("BuffCard тестүүд")
    class BuffCardTests {

        @Test
        @DisplayName("BuffCard тоглоход nextAttackBonus нэмэгдэнэ")
        void buffCardIncreasesNextAttackBonus() {
            AIPlayer player = makePlayer("P");
            AIPlayer dummy = makePlayer("D");

            assertEquals(0, player.getNextAttackBonus());
            player.getHand().add(new BuffCard("Buff", 0, "", Rarity.RARE, 5));
            player.playCard(0, dummy);

            assertEquals(5, player.getNextAttackBonus(),
                "BuffCard тоглосны дараа nextAttackBonus 5 байх ёстой");
        }

        @Test
        @DisplayName("Хоёр BuffCard хэрэглэхэд bonus нийлнэ")
        void twoBuffsStack() {
            AIPlayer player = makePlayer("P");
            AIPlayer dummy = makePlayer("D");

            player.getHand().add(new BuffCard("B1", 0, "", Rarity.COMMON, 3));
            player.getHand().add(new BuffCard("B2", 0, "", Rarity.COMMON, 4));
            player.playCard(0, dummy);
            player.playCard(0, dummy);

            assertEquals(7, player.getNextAttackBonus(),
                "Хоёр buff нийлж 3+4=7 байх ёстой");
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    // 4. CreatureCard
    // ══════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("CreatureCard тестүүд")
    class CreatureCardTests {

        @Test
        @DisplayName("CreatureCard тоглоход battlefield-д нэмэгдэнэ")
        void creatureCardAddedToBattlefield() {
            AIPlayer player = makePlayer("P");
            AIPlayer dummy = makePlayer("D");

            CreatureCard wolf = new CreatureCard("Wolf", 0, "", Rarity.COMMON, 3, 2);
            player.getHand().add(wolf);
            player.playCard(0, dummy);

            assertEquals(1, player.getBattlefield().size(),
                "Creature тоглосны дараа battlefield-д 1 creature байх ёстой");
            assertSame(wolf, player.getBattlefield().get(0));
        }

        @Test
        @DisplayName("CreatureCard endTurn-д дайсан руу дайрна")
        void creatureAttacksOnEndTurn() {
            AIPlayer player = makePlayer("P");
            AIPlayer enemy = makePlayer("E");

            CreatureCard eagle = new CreatureCard("Eagle", 0, "", Rarity.RARE, 4, 3);
            player.getHand().add(eagle);
            player.playCard(0, enemy); // summon

            int hpBefore = enemy.getHp(); // 30
            player.endTurn(enemy);

            assertEquals(hpBefore - 3, enemy.getHp(),
                "ATK:3 creature-н ээлж дуусахад дайсанд 3 хохирол учрах ёстой");
        }

        @Test
        @DisplayName("CreatureCard takeDamage зөв ажилладаг")
        void creatureTakesDamageCorrectly() {
            CreatureCard dragon = new CreatureCard("Dragon", 5, "", Rarity.LEGENDARY, 8, 6);

            assertFalse(dragon.takeDamage(5), "5 хохирол авахад (HP:3 үлдсэн) үхэхгүй");
            assertEquals(3, dragon.getHealth());

            assertTrue(dragon.takeDamage(3), "3 хохирол авахад (HP:0) үхнэ");
            assertFalse(dragon.isAlive(), "HP 0 болсон creature амьгүй байх ёстой");
        }

        @Test
        @DisplayName("Үхсэн creature endTurn-д дайрахгүй")
        void deadCreatureDoesNotAttack() {
            AIPlayer player = makePlayer("P");
            AIPlayer enemy = makePlayer("E");

            CreatureCard weak = new CreatureCard("Weak", 0, "", Rarity.COMMON, 1, 5);
            player.summonCreature(weak);
            weak.takeDamage(10); // HP: -9, dead

            int hpBefore = enemy.getHp();
            player.endTurn(enemy);

            assertEquals(hpBefore, enemy.getHp(),
                "Үхсэн creature ээлж дуусахад дайрахгүй байх ёстой");
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    // 5. Player
    // ══════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Player тестүүд")
    class PlayerTests {

        @Test
        @DisplayName("Player анхдагч HP 30 байна")
        void playerStartsWith30Hp() {
            AIPlayer p = makePlayer("Hero");
            assertEquals(30, p.getHp(), "Шинэ тоглогч 30 HP-тэй эхлэх ёстой");
        }

        @Test
        @DisplayName("Player анхдагч мана 3 байна")
        void playerStartsWith3Mana() {
            AIPlayer p = makePlayer("Hero");
            assertEquals(3, p.getMana(), "Шинэ тоглогч 3 мана-тай эхлэх ёстой");
        }

        @Test
        @DisplayName("startTurn-д maxMana нэмэгдэнэ, mana дүүрнэ, карт татагдана")
        void startTurnIncreasesMaxManaAndDraws() {
            List<Card> deckCards = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                deckCards.add(new AttackCard("Card" + i, 0, "", Rarity.COMMON, 1));
            }
            Deck deck = new Deck(deckCards);
            AIPlayer p = new AIPlayer("Hero", deck);
            p.getHand().clear(); // анхны 5 татсаныг цэвэрлэх

            int handSizeBefore = p.getHand().size();
            p.startTurn();

            assertEquals(4, p.getMaxMana(), "startTurn-д maxMana 3→4 болох ёстой");
            assertEquals(4, p.getMana(), "startTurn-д mana дүүрч 4 болох ёстой");
            assertEquals(handSizeBefore + 1, p.getHand().size(),
                "startTurn-д 1 карт татагдах ёстой");
        }

        @Test
        @DisplayName("playCard мана зарцуулна")
        void playCardCostsMana() {
            AIPlayer p = makePlayer("P");
            AIPlayer d = makePlayer("D");

            p.getHand().add(new AttackCard("Arrow", 2, "", Rarity.COMMON, 3));
            int manaBefore = p.getMana(); // 3

            p.playCard(0, d);

            assertEquals(manaBefore - 2, p.getMana(),
                "2 манын карт тоглосны дараа мана 2-оор буурах ёстой");
        }

        @Test
        @DisplayName("playCard — мана хүрэлцэхгүй бол InsufficientManaException")
        void playCardThrowsWhenNotEnoughMana() {
            AIPlayer p = makePlayer("P");
            AIPlayer d = makePlayer("D");

            // Мана 3, карт 7 мана шаарддаг
            p.getHand().add(new AttackCard("DeathCoil", 7, "", Rarity.LEGENDARY, 10));

            assertThrows(InsufficientManaException.class,
                () -> p.playCard(0, d),
                "Мана хүрэлцэхгүй үед InsufficientManaException гарах ёстой");
        }

        @Test
        @DisplayName("playCard — буруу индекс бол IllegalArgumentException")
        void playCardThrowsOnBadIndex() {
            AIPlayer p = makePlayer("P");
            AIPlayer d = makePlayer("D");

            assertThrows(IllegalArgumentException.class,
                () -> p.playCard(99, d),
                "Буруу индекс оруулахад IllegalArgumentException гарах ёстой");

            assertThrows(IllegalArgumentException.class,
                () -> p.playCard(-1, d),
                "Сөрөг индекс оруулахад IllegalArgumentException гарах ёстой");
        }

        @Test
        @DisplayName("takeDamage сөрөг бол IllegalArgumentException")
        void takeDamageNegativeThrows() {
            AIPlayer p = makePlayer("P");
            assertThrows(IllegalArgumentException.class,
                () -> p.takeDamage(-5),
                "Сөрөг хохирол оруулахад exception гарах ёстой");
        }

        @Test
        @DisplayName("isAlive — HP 0 болсон үед false буцаана")
        void isAliveReturnsFalseAtZeroHp() {
            AIPlayer p = makePlayer("P");
            assertTrue(p.isAlive());
            p.takeDamage(30);
            assertFalse(p.isAlive(), "30 хохирлын дараа тоглогч амьгүй байх ёстой");
        }

        @Test
        @DisplayName("Хоосон нэрийн Player үүсгэхэд exception")
        void emptyNameThrows() {
            Deck empty = new Deck(new ArrayList<>());
            assertThrows(IllegalArgumentException.class,
                () -> new AIPlayer("", empty),
                "Хоосон нэртэй Player үүсгэвэл exception гарах ёстой");
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    // 6. Deck
    // ══════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Deck тестүүд")
    class DeckTests {

        @Test
        @DisplayName("Deck draw() картыг дарааллаар буцаана ба size буурна")
        void deckDrawReducesSize() {
            List<Card> cards = new ArrayList<>();
            cards.add(new AttackCard("A", 0, "", Rarity.COMMON, 1));
            cards.add(new AttackCard("B", 0, "", Rarity.COMMON, 2));
            Deck deck = new Deck(cards);

            assertEquals(2, deck.size());
            Card drawn = deck.draw();
            assertNotNull(drawn, "Хоосон биш deck-ээс draw хийхэд карт буцаах ёстой");
            assertEquals(1, deck.size(), "Draw хийсний дараа deck 1 картнай байх ёстой");
        }

        @Test
        @DisplayName("Хоосон deck-ээс draw хийхэд null буцаана")
        void emptyDeckDrawsNull() {
            Deck empty = new Deck(new ArrayList<>());
            assertNull(empty.draw(), "Хоосон deck-ээс draw хийхэд null буцаах ёстой");
        }

        @Test
        @DisplayName("isEmpty — хоосон болсны дараа true")
        void deckIsEmptyAfterAllDrawn() {
            List<Card> cards = new ArrayList<>();
            cards.add(new HealCard("H", 0, "", Rarity.COMMON, 5));
            Deck deck = new Deck(cards);

            assertFalse(deck.isEmpty());
            deck.draw();
            assertTrue(deck.isEmpty(), "Сүүлчийн карт татсаны дараа deck хоосон байх ёстой");
        }

        @Test
        @DisplayName("shuffle() deck-ийн хэмжээг өөрчлөхгүй")
        void shuffleKeepsSize() {
            List<Card> cards = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                cards.add(new AttackCard("Card" + i, 1, "", Rarity.COMMON, i + 1));
            }
            Deck deck = new Deck(cards);

            deck.shuffle();

            assertEquals(10, deck.size(),
                "shuffle() хийсний дараа deck-ийн хэмжээ өөрчлөгдөхгүй байх ёстой");
        }

        @Test
        @DisplayName("null жагсаалтаар Deck үүсгэхэд exception")
        void nullCardsThrows() {
            assertThrows(IllegalArgumentException.class,
                () -> new Deck(null),
                "null жагсаалтаар Deck үүсгэхэд exception гарах ёстой");
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    // 7. AIPlayer стратеги
    // ══════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("AIPlayer стратегийн тестүүд")
    class AIPlayerStrategyTests {

        @Test
        @DisplayName("AI хоосон гартай бол -1 буцаана")
        void aiReturnsMinusOneWithEmptyHand() {
            AIPlayer ai = makePlayer("AI");
            AIPlayer dummy = makePlayer("D");

            assertEquals(-1, ai.chooseCard(dummy),
                "Хоосон гартай AI -1 (pass) буцаах ёстой");
        }

        @Test
        @DisplayName("AI HP бага байхад HealCard-г эхлэж сонгоно")
        void aiPrefersHealWhenLowHp() {
            AIPlayer ai = makePlayer("AI");
            AIPlayer dummy = makePlayer("D");

            ai.takeDamage(22); // HP: 8 (≤ 10)
            ai.getHand().add(new AttackCard("Attack", 0, "", Rarity.COMMON, 4));
            ai.getHand().add(new HealCard("Heal", 0, "", Rarity.COMMON, 8));

            int choice = ai.chooseCard(dummy);

            assertEquals(1, choice,
                "HP бага (8) үед AI HealCard (index 1) сонгох ёстой");
        }

        @Test
        @DisplayName("AI мана хүрэлцэхгүй картыг тоглохгүй")
        void aiSkipsCardsWithInsufficientMana() {
            AIPlayer ai = makePlayer("AI");
            AIPlayer dummy = makePlayer("D");

            // Мана 3, хоёр карт хоёулаа 5+ мана шаарддаг
            ai.getHand().add(new AttackCard("Exp1", 5, "", Rarity.RARE, 7));
            ai.getHand().add(new AttackCard("Exp2", 6, "", Rarity.LEGENDARY, 10));

            int choice = ai.chooseCard(dummy);

            assertEquals(-1, choice,
                "Бүх карт мана хүрэлцэхгүй үед AI -1 (pass) буцаах ёстой");
        }

        @Test
        @DisplayName("AI хүчтэй AttackCard-г сонгоно")
        void aiChoosesStrongestAttack() {
            AIPlayer ai = makePlayer("AI");
            AIPlayer dummy = makePlayer("D");

            ai.getHand().add(new AttackCard("Weak", 1, "", Rarity.COMMON, 2));
            ai.getHand().add(new AttackCard("Strong", 2, "", Rarity.RARE, 6));

            int choice = ai.chooseCard(dummy);

            assertEquals(1, choice, "AI хамгийн хүчтэй AttackCard (index 1) сонгох ёстой");
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    // 8. Rarity
    // ══════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Rarity enum тестүүд")
    class RarityTests {

        @Test
        @DisplayName("Rarity enum 3 утга агуулна")
        void rarityHasThreeValues() {
            assertEquals(3, Rarity.values().length,
                "Rarity enum яг 3 утгатай (COMMON, RARE, LEGENDARY) байх ёстой");
        }

        @Test
        @DisplayName("Rarity утгууд зөв нэртэй")
        void rarityValuesCorrectlyNamed() {
            assertEquals(Rarity.COMMON,    Rarity.valueOf("COMMON"));
            assertEquals(Rarity.RARE,      Rarity.valueOf("RARE"));
            assertEquals(Rarity.LEGENDARY, Rarity.valueOf("LEGENDARY"));
        }

        @Test
        @DisplayName("Card Rarity-г зөв хадгална")
        void cardStoresRarityCorrectly() {
            AttackCard rare = new AttackCard("Storm", 3, "", Rarity.RARE, 5);
            assertEquals(Rarity.RARE, rare.getRarity(),
                "Card-д оруулсан Rarity зөв хадгалагдах ёстой");
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    // 9. Card суурь
    // ══════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Card суурийн тестүүд")
    class CardBaseTests {

        @Test
        @DisplayName("Card нэр, manaCost, description зөв хадгалагдана")
        void cardStoresFields() {
            AttackCard card = new AttackCard("Fireball", 3, "галын бөмбөг", Rarity.COMMON, 4);
            assertEquals("Fireball", card.getName());
            assertEquals(3, card.getManaCost());
            assertEquals("галын бөмбөг", card.getDescription());
            assertEquals(Rarity.COMMON, card.getRarity());
            assertEquals(4, card.getDamage());
        }

        @Test
        @DisplayName("Хоосон нэртэй Card үүсгэхэд exception")
        void blankNameThrows() {
            assertThrows(IllegalArgumentException.class,
                () -> new AttackCard("", 1, "", Rarity.COMMON, 3));
            assertThrows(IllegalArgumentException.class,
                () -> new AttackCard("   ", 1, "", Rarity.COMMON, 3));
        }

        @Test
        @DisplayName("Сөрөг manaCost-той Card үүсгэхэд exception")
        void negativeManaCostThrows() {
            assertThrows(IllegalArgumentException.class,
                () -> new HealCard("H", -1, "", Rarity.COMMON, 5));
        }

        @Test
        @DisplayName("null Rarity-тай Card үүсгэхэд exception")
        void nullRarityThrows() {
            assertThrows(IllegalArgumentException.class,
                () -> new BuffCard("B", 1, "", null, 3));
        }

        @Test
        @DisplayName("Polymorphism — List<Card> дотор олон төрлийн карт play() дуудна")
        void polymorphismWorksForCardList() {
            AIPlayer caster = makePlayer("Caster");
            AIPlayer target = makePlayer("Target");

            List<Card> mixed = new ArrayList<>();
            mixed.add(new AttackCard("Fireball", 0, "", Rarity.COMMON, 5));
            mixed.add(new HealCard("Herb",   0, "", Rarity.COMMON, 3));
            mixed.add(new BuffCard("Blade",  0, "", Rarity.COMMON, 2));

            // Гурван ялгаатай карт тоглох — exception гарахгүй байх ёстой
            assertDoesNotThrow(() -> {
                for (Card c : mixed) {
                    c.play(caster, target);
                }
            }, "Polymorphic play() дуудлага exception гаргахгүй байх ёстой");

            assertEquals(30 - 5, target.getHp(), "AttackCard 5 хохирол учрах ёстой");
            assertEquals(30 + 3, 33); // heal check — max 30 болох учир
            assertEquals(30, caster.getHp(), "HealCard бүрэн HP дээр нөлөөгүй");
            assertEquals(2, caster.getNextAttackBonus(), "BuffCard +2 bonus нэмэх ёстой");
        }
    }
}
