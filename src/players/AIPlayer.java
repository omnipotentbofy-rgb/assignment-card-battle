import java.util.List;

public class AIPlayer extends Player {

    public AIPlayer(String name, Deck deck) {
        super(name, deck);
    }

    @Override
    public int chooseCard(Player opponent) {
        List<Card> hand = getHand();
        if (hand.isEmpty()) return -1;

        int mana = getMana();

        if (getHp() <= 10) {
            int idx = findBestHeal(hand, mana);
            if (idx >= 0) return idx;
        }

        if (getNextAttackBonus() > 0) {
            int idx = findStrongestAttack(hand, mana);
            if (idx >= 0) return idx;
        }

        int attackIdx = findStrongestAttack(hand, mana);
        if (attackIdx >= 0) return attackIdx;

        for (int i = 0; i < hand.size(); i++) {
            Card c = hand.get(i);
            if (c instanceof BuffCard && c.getManaCost() <= mana) return i;
        }

        for (int i = 0; i < hand.size(); i++) {
            Card c = hand.get(i);
            if (c instanceof CreatureCard && c.getManaCost() <= mana) return i;
        }

        for (int i = 0; i < hand.size(); i++) {
            if (hand.get(i).getManaCost() <= mana) return i;
        }

        return -1;
    }

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
