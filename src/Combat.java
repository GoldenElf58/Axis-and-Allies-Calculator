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
        boolean attackerSurvives;
        boolean defenderSurvives;
    }

    static class Hits {
        int airHits;
        int subHits;
        int otherHits;

        @Override
        public String toString() {
            return "Hits(" +
                    "airHits=" + airHits +
                    ", subHits=" + subHits +
                    ", otherHits=" + otherHits +
                    ')';
        }
    }

    static Result simulateBattle(List<UnitInstance> attackers, List<UnitInstance> defenders, boolean seaBattle) {
        attackers.sort(casualtyComparator(false, seaBattle));
        defenders.sort(casualtyComparator(true, seaBattle));

        boolean defenderCanFight;
        boolean attackerCanFight;
        while (true) {
            attackerCanFight = canFight(attackers);
            defenderCanFight = canFight(defenders);
            if (!attackerCanFight || !defenderCanFight) break;

            if (seaBattle) {
                boolean attackerHasOnlySubs = attackers.stream()
                        .anyMatch(u -> u.isAlive() && u.type == Unit.SUBMARINE)
                        && attackers.stream()
                        .noneMatch(u -> u.isAlive() && u.type != Unit.SUBMARINE);

                if (attackerHasOnlySubs && defenders.stream()
                        .anyMatch(u -> u.isAlive() && u.type.type == UnitType.AIR)
                        && defenders.stream()
                        .noneMatch(u -> u.isAlive() && u.type.type != UnitType.AIR)) break;


                boolean attackerHasOnlyAir = attackers.stream()
                        .anyMatch(u -> u.isAlive() && u.type.type == UnitType.AIR)
                        && attackers.stream()
                        .noneMatch(u -> u.isAlive() && u.type.type != UnitType.AIR);

                if (attackerHasOnlyAir && defenders.stream()
                        .anyMatch(u -> u.isAlive() && u.type == Unit.SUBMARINE)
                        && defenders.stream()
                        .noneMatch(u -> u.isAlive() && u.type != Unit.SUBMARINE)) break;
            }

            boolean defenderHasDestroyer = false, attackerHasDestroyer = false;
            if (seaBattle) {
                defenderHasDestroyer = hasDestroyer(defenders);
                attackerHasDestroyer = hasDestroyer(attackers);
                boolean subStrikeA = !defenderHasDestroyer && hasSubs(attackers);
                boolean subStrikeD = !attackerHasDestroyer && hasSubs(defenders);

                Hits strikeAHits = null;
                Hits strikeDHits = null;
                if (subStrikeA) strikeAHits = rollHits(attackers, true, true);
                if (subStrikeD) strikeDHits = rollHits(defenders, false, true);
                if (subStrikeA) applyHits(defenders, strikeAHits, false);
                if (subStrikeD) applyHits(attackers, strikeDHits, false);
            }

            Hits aHits = rollHits(attackers, true, false);
            Hits dHits = rollHits(defenders, false, false);
            applyHits(defenders, aHits, attackerHasDestroyer);
            applyHits(attackers, dHits, defenderHasDestroyer);
        }

        Result r = new Result();
        boolean aAlive = isAlive(attackers);
        boolean dAlive = isAlive(defenders);

        r.attackerWin = aAlive && !defenderCanFight;
        r.draw = attackerCanFight == defenderCanFight;
        r.defenderWin = !r.attackerWin && !r.draw;
        r.attackerSurvives = aAlive;
        r.defenderSurvives = dAlive;
        return r;
    }


    static Hits rollHits(List<UnitInstance> units, boolean attacking, boolean subsOnly) {
        Hits hits = new Hits();
        for (UnitInstance u : units) {
            if (!u.isAlive()) continue;
            if (subsOnly && u.type != Unit.SUBMARINE) continue;

            int power = attacking ? u.type.attack : u.type.defense;
            if (power == 0) continue;
            if (ThreadLocalRandom.current().nextInt(6) + 1 <= power) {
                if (u.type == Unit.SUBMARINE) hits.subHits++;
                else if (u.type.type == UnitType.AIR) hits.airHits++;
                else hits.otherHits++;
            }
        }
        return hits;
    }

    static void applyHits(List<UnitInstance> units, Hits hits, boolean destroyersPresent) {
        if (hits.airHits + hits.subHits + hits.otherHits <= 0) return;

        while (hits.airHits + hits.subHits + hits.otherHits > 0) {
            boolean applied = false;

            for (int i = 0; i < units.size(); i++) {
                UnitInstance u = units.get(i);
                if (!u.isAlive()) continue;

                // Transports can only be hit if nothing else is alive
                if (u.type == Unit.TRANSPORT && hasOtherAlive(units)) continue;

                if (u.type == Unit.BATTLESHIP) {
                    if (hits.subHits > 0) hits.subHits--;
                    else if (hits.airHits > 0) hits.airHits--;
                    else if (hits.otherHits > 0) hits.otherHits--;
                    else return;
                    u.hits++;
                    applied = true;
                    continue;
                }

                if (u.type.type == UnitType.AIR) {
                    if (hits.airHits > 0) hits.airHits--;
                    else if (hits.otherHits > 0) hits.otherHits--;
                    else if (hits.subHits == 0) return;
                    else continue;
                } else if (u.type == Unit.SUBMARINE) {
                    if (hits.subHits > 0) hits.subHits--;
                    else if (destroyersPresent && hits.airHits > 0) hits.airHits--;
                    else if (hits.otherHits > 0) hits.otherHits--;
                    else if (hits.airHits == 0) return;
                    else continue;
                } else {
                    if (hits.subHits > 0) hits.subHits--;
                    else if (hits.airHits > 0) hits.airHits--;
                    else if (hits.otherHits > 0) hits.otherHits--;
                    else return;
                }
                u.hits = 2;

                applied = true;
            }

            // If we looped and could not apply a hit, stop to avoid infinite loop
            if (!applied) break;
        }
    }

    static Comparator<UnitInstance> casualtyComparator(boolean defense, boolean seaBattle) {
        if (CHEAPEST_FIRST) return Comparator.comparingInt(u -> u.type.cost);
        return Comparator.comparingDouble(u -> seaBattle ? defense ? u.type.seaDefOOL :
                u.type.seaAtkOOL : defense ? u.type.landDefOOL : u.type.landAtkOOL);
    }

    static boolean hasOtherAlive(List<UnitInstance> units) {
        for (UnitInstance u : units)
            if (u.type != Unit.TRANSPORT && u.isAlive()) return true;
        return false;
    }

    static boolean canFight(List<UnitInstance> units) {
        for (UnitInstance u : units)
            if (u.isAlive() && u.type.attack > 0) return true;
        return false;
    }

    static boolean isAlive(List<UnitInstance> units) {
        for (UnitInstance u : units)
            if (u.isAlive()) return true;
        return false;
    }

    static boolean hasSubs(List<UnitInstance> u) {
        return u.stream().anyMatch(x -> x.type == Unit.SUBMARINE && x.isAlive());
    }

    static boolean hasDestroyer(List<UnitInstance> u) {
        return u.stream().anyMatch(x -> x.type == Unit.DESTROYER && x.isAlive());
    }

    static List<UnitInstance> buildArmy(Map<Unit, Integer> map) {
        List<UnitInstance> list = new ArrayList<>();
        for (var e : map.entrySet())
            for (int i = 0; i < e.getValue(); i++)
                list.add(new UnitInstance(e.getKey()));
        return list;
    }
}
