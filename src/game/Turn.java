import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Turn — Нэг ээлжийн snapshot (Stretch).
 *
 * Тоглоомын явцыг log хийх, save/replay-д ашиглана.
 */
public class Turn {

    private final int turnNumber;
    private final String activePlayerName;
    private final List<String> actionsLog;

    public Turn(int turnNumber, String activePlayerName) {
        this.turnNumber = turnNumber;
        this.activePlayerName = activePlayerName;
        this.actionsLog = new ArrayList<>();
    }

    /** Ээлжийн үйлдлийг бүртгэнэ. */
    public void logAction(String action) {
        actionsLog.add(action);
    }

    public int getTurnNumber()         { return turnNumber; }
    public String getActivePlayerName(){ return activePlayerName; }
    public List<String> getActionsLog(){ return Collections.unmodifiableList(actionsLog); }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("— Ээлж %d (%s) —%n", turnNumber, activePlayerName));
        if (actionsLog.isEmpty()) {
            sb.append("  (үйлдэл байхгүй)\n");
        } else {
            for (String a : actionsLog) {
                sb.append("  ").append(a).append("\n");
            }
        }
        return sb.toString();
    }
}
