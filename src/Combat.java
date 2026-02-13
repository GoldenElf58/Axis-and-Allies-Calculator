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

        @Override
        public String toString() {
            return "Result(attackerWin=" + attackerWin + ", draw=" + draw + ", defenderWin=" +
                    defenderWin + ", attackerSurvives=" + attackerSurvives + ", defenderSurvives=" +
                    defenderSurvives + ')';
        }
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
            if (Main.DEBUG) System.out.println();
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

            boolean defenderHasDestroyer = false, attackerHasDestroyer = false,
                    subStrikeA = false, subStrikeD = false;
            if (seaBattle) {
                defenderHasDestroyer = hasDestroyer(defenders);
                attackerHasDestroyer = hasDestroyer(attackers);
                subStrikeA = !defenderHasDestroyer && hasSubs(attackers);
                subStrikeD = !attackerHasDestroyer && hasSubs(defenders);

                Hits strikeAHits = null;
                Hits strikeDHits = null;
                if (subStrikeA) strikeAHits = rollHits(attackers, true, true, false);
                if (subStrikeD) strikeDHits = rollHits(defenders, false, true, false);
                if (Main.DEBUG) System.out.println("Sub Hits: " + strikeAHits + ", " + strikeDHits);
                if (subStrikeA) applyHits(defenders, strikeAHits, false, true, true);
                if (subStrikeD) applyHits(attackers, strikeDHits, false, false, true);
                if (Main.DEBUG) {
                    System.out.println("Attacker: " + attackers);
                    System.out.println("Defender: " + defenders);
                }
                if (subStrikeA) defenderHasDestroyer = hasDestroyer(defenders);
                if (subStrikeD) attackerHasDestroyer = hasDestroyer(attackers);
            }

            Hits aHits = rollHits(attackers, true, false, subStrikeA);
            Hits dHits = rollHits(defenders, false, false, subStrikeD);
            if (Main.DEBUG) System.out.println("Hits: " + aHits + ", " + dHits);
            applyHits(defenders, aHits, attackerHasDestroyer, true, seaBattle);
            applyHits(attackers, dHits, defenderHasDestroyer, false, seaBattle);

            if (Main.DEBUG) {
                System.out.println("Attacker: " + attackers);
                System.out.println("Defender: " + defenders);
                System.out.println("Hits: " + aHits + ", " + dHits);
            }
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


    /**
     * Rolls hits for a list of units
     *
     * @param units List of units to roll hits for
     * @param attacking Whether the units are attacking or defending
     * @param subsOnly Whether to only roll hits for submarines
     * @return Hits object containing the number of hits for each type of unit
     */
    static Hits rollHits(List<UnitInstance> units, boolean attacking, boolean subsOnly,
                         boolean noSubs) {
        Hits hits = new Hits();
        for (UnitInstance u : units) {
            if (!u.isAlive()) continue;
            if (noSubs && u.type == Unit.SUBMARINE) continue;
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

    /**
     * Applies hits to a list of units
     *
     * @param units List of units to apply hits to
     * @param hits Hits object containing the number of hits for each type of unit
     * @param destroyersPresent Whether enemy destroyers are present to affect hit application
     */
    static void applyHits(List<UnitInstance> units, Hits hits, boolean destroyersPresent, boolean defense, boolean seaBattle) {
        if (hits.airHits + hits.subHits + hits.otherHits <= 0) return;

        while (hits.airHits + hits.subHits + hits.otherHits > 0) {
            boolean applied = false;

            for (UnitInstance u : units) {
                if (!u.isAlive()) continue;

                if (u.type == Unit.BATTLESHIP) {
                    if (hits.subHits > 0) hits.subHits--;
                    else if (hits.airHits > 0) hits.airHits--;
                    else if (hits.otherHits > 0) hits.otherHits--;
                    else return;
                    u.hits++;
                    applied = true;
                    if (u.hits == 1) units.sort(casualtyComparator(defense, seaBattle));
                    break;
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
                break;
            }

            if (!applied) break;
        }
    }

    static Comparator<UnitInstance> casualtyComparator(boolean defense, boolean seaBattle) {
        if (CHEAPEST_FIRST) return Comparator.comparingInt(u -> u.type.cost);
        
        return Comparator.comparingDouble((UnitInstance u) -> {
            if (u.type == Unit.BATTLESHIP && u.hits == 0) return -1.0;
            return seaBattle ? defense ? u.type.seaDefOOL : u.type.seaAtkOOL
                             : defense ? u.type.landDefOOL : u.type.landAtkOOL;
        });
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
