/**
 * AIPlayer extends Player — өөрөө шийдэж тоглодог AI.
 */
public class AIPlayer extends Player {
    /**
     * Constructor — AIPlayer үүсгэх.
     */
    public AIPlayer(String name, Deck deck) {
        super(name, deck);
    }

    /**
     * chooseCard() override — стратегийн логиктой AI сонголт.
     */
    @Override
    public int chooseCard(Player opponent) {
        if (hand.isEmpty()) {
            System.out.println("🤖 " + name + " (AI) ээлжээ дуусгалаа.");
            return -1;
        }

        // Стратеги 1: HP бага бол HealCard сонгох
        if (hp < maxHp / 2) {
            for (int i = 0; i < hand.size(); i++) {
                if (hand.get(i) instanceof HealCard && hand.get(i).getManaCost() <= mana) {
                    Card card = hand.get(i);
                    System.out.println("🤖 " + name + " (AI) " + card.getName() + " сонголоо.");
                    return i;
                }
            }
        }

        // Стратеги 2: Мана хангалттай бол AttackCard сонгох
        for (int i = 0; i < hand.size(); i++) {
            Card card = hand.get(i);
            if (card instanceof AttackCard && card.getManaCost() <= mana) {
                System.out.println("🤖 " + name + " (AI) " + card.getName() + " сонголоо.");
                return i;
            }
        }

        // Стратеги 3: Тоглож болох картыг сонгох
        for (int i = 0; i < hand.size(); i++) {
            if (hand.get(i).getManaCost() <= mana) {
                Card card = hand.get(i);
                System.out.println("🤖 " + name + " (AI) " + card.getName() + " сонголоо.");
                return i;
            }
        }

        // Ямар ч картыг тоглож чадахгүй — ээлж дуусна
        System.out.println("🤖 " + name + " (AI) ээлжээ дуусгалаа.");
        return -1;
    }
}
