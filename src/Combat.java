import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class Combat {

    static final boolean CHEAPEST_FIRST = false;

    static class Result {
        boolean attackerWin;
        boolean draw;
        boolean defenderWin;
        int attackerIPCLoss;
        int defenderIPCLoss;
    }

    static Result simulateBattle(List<Unit> attackers, List<Unit> defenders, boolean seaBattle) {
        int attackerStartIPC = ipcSum(attackers);
        int defenderStartIPC = ipcSum(defenders);

        attackers.sort(casualtyComparator(false, seaBattle));
        defenders.sort(casualtyComparator(true, seaBattle));

        while (true) {
            boolean attackerCanFight = canFight(attackers);
            boolean defenderCanFight = canFight(defenders);

            if (!attackerCanFight || !defenderCanFight) break;

            boolean subStrikeA = hasSubs(attackers) && hasNoDestroyer(defenders);
            boolean subStrikeD = hasSubs(defenders) && hasNoDestroyer(attackers);

            int strikeAHits = rollHits(attackers, true, true);
            int strikeDHits = rollHits(defenders, false, true);
            if (subStrikeA) applyHits(defenders, strikeAHits);
            if (subStrikeD) applyHits(attackers, strikeDHits);

            int aHits = rollHits(attackers, true, false);
            int dHits = rollHits(defenders, false, false);
            applyHits(defenders, aHits);
            applyHits(attackers, dHits);
        }

        Result r = new Result();
        boolean aAlive = isAlive(attackers);
        boolean aCanFight = canFight(attackers);
        boolean dCanFight = canFight(defenders);

        r.attackerWin = aAlive && !dCanFight;
        r.draw = aCanFight == dCanFight;
        r.defenderWin = !r.attackerWin && !r.draw;

        r.attackerIPCLoss = attackerStartIPC - ipcSum(attackers);
        r.defenderIPCLoss = defenderStartIPC - ipcSum(defenders);
        return r;
    }


    static int rollHits(List<Unit> units, boolean attacking, boolean subsOnly) {
        int hits = 0;
        for (Unit u : units) {
            if (!u.isAlive()) continue;
            if (subsOnly && u.type != UnitType.SUBMARINE) continue;

            int power = attacking ? u.type.attack : u.type.defense;
            if (power == 0) continue;
            if (ThreadLocalRandom.current().nextInt(6) + 1 <= power) hits++;
        }
        return hits;
    }

    static void applyHits(List<Unit> units, int hits) {
        if (hits <= 0) return;

        while (hits > 0) {
            boolean applied = false;

            for (int i = 0; i < units.size() && hits > 0; i++) {
                Unit u = units.get(i);
                if (!u.isAlive()) continue;

                // Transports can only be hit if nothing else is alive
                if (u.type == UnitType.TRANSPORT && hasOtherAlive(units)) continue;

                if (u.type == UnitType.BATTLESHIP && u.hits == 0) u.hits = 1; // first hit (damaged)
                else u.hits = 2; // destroyed
                hits--;

                applied = true;
            }

            // If we looped and could not apply a hit, stop to avoid infinite loop
            if (!applied) break;
        }
    }

    static Comparator<Unit> casualtyComparator(boolean defense, boolean seaBattle) {
        if (CHEAPEST_FIRST) return Comparator.comparingInt(u -> u.type.cost);
        return Comparator.comparingDouble(u -> seaBattle ? defense ? u.type.seaDefOOL :
                u.type.seaAtkOOL : defense ? u.type.landDefOOL : u.type.landAtkOOL);
    }

    static boolean hasOtherAlive(List<Unit> units) {
        for (Unit u : units)
            if (u.type != UnitType.TRANSPORT && u.isAlive()) return true;
        return false;
    }

    static boolean canFight(List<Unit> units) {
        for (Unit u : units)
            if (u.isAlive() && u.type.attack > 0) return true;
        return false;
    }

    static boolean isAlive(List<Unit> units) {
        for (Unit u : units)
            if (u.isAlive()) return true;
        return false;
    }

    static boolean hasSubs(List<Unit> u) {
        return u.stream().anyMatch(x -> x.type == UnitType.SUBMARINE && x.isAlive());
    }

    static boolean hasNoDestroyer(List<Unit> u) {
        return u.stream().noneMatch(x -> x.type == UnitType.DESTROYER && x.isAlive());
    }

    static int ipcSum(List<Unit> units) {
        int s = 0;
        for (Unit u : units)
            if (u.isAlive()) s += u.type.cost;
        return s;
    }

    static List<Unit> buildArmy(Map<UnitType, Integer> map) {
        List<Unit> list = new ArrayList<>();
        for (var e : map.entrySet())
            for (int i = 0; i < e.getValue(); i++)
                list.add(new Unit(e.getKey()));
        return list;
    }
}
