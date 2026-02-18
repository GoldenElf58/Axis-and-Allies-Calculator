import java.util.Comparator;

public class Combat {

    static class Result {
        boolean attackerWin;
        boolean draw;
        boolean defenderWin;
        boolean attackerSurvives;
        boolean defenderSurvives;
        boolean attackerWin1;
        boolean defenderWin1;
        boolean attackerWin2;
        boolean defenderWin2;

        @Override
        public String toString() {
            return "Result(attackerWin=" + attackerWin + ", draw=" + draw + ", defenderWin=" +
                    defenderWin + ", attackerSurvives=" + attackerSurvives + ", defenderSurvives=" +
                    defenderSurvives + ')';
        }
    }

    public static class Hits {
        int airHits;
        int subHits;
        int otherHits;

        @SuppressWarnings("BooleanMethodIsAlwaysInverted")
        public boolean remove(boolean air, boolean sub) {
            if (sub && subHits > 0) subHits--;
            else if (air && airHits > 0) airHits--;
            else if (otherHits > 0) otherHits--;
            else return false;
            return true;
        }

        @Override
        public String toString() {
            return "Hits(" +
                    "airHits=" + airHits +
                    ", subHits=" + subHits +
                    ", otherHits=" + otherHits +
                    ')';
        }
    }

    static Result simulateBattle(OngoingBattle battle, boolean debug) {
        boolean defenderCanFight;
        boolean attackerCanFight;
        while (true) {
            if (debug) System.out.println();
            attackerCanFight = battle.attackerNumTroops() > 0;
            defenderCanFight = battle.defenderNumTroops() > 0;
            if (!attackerCanFight || !defenderCanFight) break;

            if (battle.seaBattle) {
                boolean attackerHasOnlySubs = battle.attackerNumTroops() == battle.aSub;
                boolean defenderHasOnlyAir =
                        battle.defenderNumTroops() == battle.dBom + battle.dFig;

                if (attackerHasOnlySubs && defenderHasOnlyAir) {
                    if (debug) System.out.println("Only air vs only subs");
                    break;
                }


                boolean attackerHasOnlyAir =
                        battle.attackerNumTroops() == battle.aBom + battle.aFig;
                boolean defenderHasOnlySubs = battle.defenderNumTroops() == battle.dSub;

                if (attackerHasOnlyAir && defenderHasOnlySubs) {
                    if (debug) System.out.println("Only air vs only subs");
                    break;
                }
            }

            boolean defenderHasDestroyer = false, attackerHasDestroyer = false,
                    subStrikeA = false, subStrikeD = false;
            if (battle.seaBattle) {
                defenderHasDestroyer = battle.dDes > 0;
                attackerHasDestroyer = battle.aDes > 0;
                subStrikeA = !defenderHasDestroyer && battle.aSub > 0;
                subStrikeD = !attackerHasDestroyer && battle.dSub > 0;

                Hits strikeAHits = null;
                Hits strikeDHits = null;
                if (subStrikeA) strikeAHits = battle.rollAttackerHits(true, false);
                if (subStrikeD) strikeDHits = battle.rollDefenderHits(true, false);
                if (debug) System.out.println("Sub Hits: " + strikeAHits + ", " + strikeDHits);
                if (subStrikeA) battle.applyHits(strikeAHits, true, false);
                if (subStrikeD) battle.applyHits(strikeDHits, false, false);
                if (debug) System.out.println(battle);
                if (subStrikeA) defenderHasDestroyer = battle.dDes > 0;
                if (subStrikeD) attackerHasDestroyer = battle.aDes > 0;
            }

            Hits aHits = battle.rollAttackerHits(false, subStrikeA);
            Hits dHits = battle.rollDefenderHits(false, subStrikeD);
            if (debug) System.out.println("Hits: " + aHits + ", " + dHits);
            battle.applyHits(aHits, true, attackerHasDestroyer);
            battle.applyHits(dHits, false, defenderHasDestroyer);

            if (debug) {
                System.out.println(battle);
                System.out.println("Hits: " + aHits + ", " + dHits);
            }
        }

        Result r = new Result();
        boolean aAlive = battle.attackerNumTroops() > 0;
        boolean dAlive = battle.defenderNumTroops() > 0;
        boolean aCanTake = battle.canAttackerTake();

        r.attackerWin = aAlive && !dAlive;
        r.draw = aAlive == dAlive;
        r.defenderWin = !r.attackerWin && !r.draw;
        r.attackerWin1 = r.attackerWin && aCanTake;
        r.attackerWin2 = r.attackerWin && !aCanTake;
        r.defenderWin1 = dAlive && !aAlive;
        r.defenderWin2 = r.draw;
        r.attackerSurvives = aAlive;
        r.defenderSurvives = dAlive;
        return r;
    }

    static Comparator<? super Unit> casualtyComparator(boolean defense, boolean seaBattle) {
        return Comparator.comparingDouble((Unit u) -> seaBattle ? defense ? u.seaDefOOL :
                u.seaAtkOOL : defense ? u.landDefOOL : u.landAtkOOL);
    }
}
