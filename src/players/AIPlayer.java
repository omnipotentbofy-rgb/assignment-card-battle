import java.util.List;

/**
 * AIPlayer — Стратегийн логиктой компьютерийн тоглогч.
 *
 * Логик (дээрээс доош тэргүүлэх дараалал):
 *  1. HP бага (≤ 10) бол HealCard-г эрэмбэд тавина
 *  2. Buff байгаа бол AttackCard-тай хослуулна
 *  3. Хамгийн өндөр хохирол бүхий тоглох боломжтой AttackCard
 *  4. BuffCard (дараагийн ээлжийн бэлтгэл)
 *  5. CreatureCard
 *  6. Тоглох боломжтой аливаа карт
 *  7. -1 (pass)
 */
public class AIPlayer extends Player {

    public AIPlayer(String name, Deck deck) {
        super(name, deck);
    }

    @Override
    public int chooseCard(Player opponent) {
        List<Card> hand = getHand();
        if (hand.isEmpty()) return -1;

        int mana = getMana();

        // 1. HP бага бол эхлээд эдгэх
        if (getHp() <= 10) {
            int idx = findBestHeal(hand, mana);
            if (idx >= 0) return idx;
        }

        // 2. Buff идэвхтэй бол AttackCard
        if (getNextAttackBonus() > 0) {
            int idx = findStrongestAttack(hand, mana);
            if (idx >= 0) return idx;
        }

        // 3. Хамгийн хүчтэй AttackCard
        int attackIdx = findStrongestAttack(hand, mana);
        if (attackIdx >= 0) return attackIdx;

        // 4. BuffCard
        for (int i = 0; i < hand.size(); i++) {
            Card c = hand.get(i);
            if (c instanceof BuffCard && c.getManaCost() <= mana) return i;
        }

        // 5. CreatureCard
        for (int i = 0; i < hand.size(); i++) {
            Card c = hand.get(i);
            if (c instanceof CreatureCard && c.getManaCost() <= mana) return i;
        }

        // 6. Тоглох боломжтой аливаа карт
        for (int i = 0; i < hand.size(); i++) {
            if (hand.get(i).getManaCost() <= mana) return i;
        }

        // 7. Pass
        return -1;
    }

    // ── Private helpers ──────────────────────────────────────────────────────

    private int findBestHeal(List<Card> hand, int mana) {
        int bestIdx = -1;
        int bestHeal = -1;
        for (int i = 0; i < hand.size(); i++) {
            Card c = hand.get(i);
            if (c instanceof HealCard hc && hc.getManaCost() <= mana) {
                if (hc.getHealAmount() > bestHeal) {
                    bestHeal = hc.getHealAmount();
                    bestIdx = i;
                }
            }
        }
        return bestIdx;
    }

    private int findStrongestAttack(List<Card> hand, int mana) {
        int bestIdx = -1;
        int bestDmg = -1;
        for (int i = 0; i < hand.size(); i++) {
            Card c = hand.get(i);
            if (c instanceof AttackCard ac && ac.getManaCost() <= mana) {
                int totalDmg = ac.getDamage() + getNextAttackBonus();
                if (totalDmg > bestDmg) {
                    bestDmg = totalDmg;
                    bestIdx = i;
                }
            }
        }
        return bestIdx;
    }
}
