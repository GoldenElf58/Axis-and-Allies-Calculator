public class OngoingBattle {

    public final boolean seaBattle;
    public int aInf, aTank, aFig, aBom, aSub, aDes, aACC, aBat, aExtra;
    public int dInf, dTank, dFig, dBom, dSub, dDes, dACC, dBat, dExtra;

    OngoingBattle(Battle battle) {
        this.seaBattle = battle.seaBattle;
        this.aInf = battle.aInf;
        this.aTank = battle.aTank;
        this.aFig = battle.aFig;
        this.aBom = battle.aBom;
        this.aSub = battle.aSub;
        this.aDes = battle.aDes;
        this.aACC = battle.aACC;
        this.aBat = battle.aBat;
        this.aExtra = battle.aBat;
        this.dInf = battle.dInf;
        this.dTank = battle.dTank;
        this.dFig = battle.dFig;
        this.dBom = battle.dBom;
        this.dSub = battle.dSub;
        this.dDes = battle.dDes;
        this.dACC = battle.dACC;
        this.dBat = battle.dBat;
        this.dExtra = battle.dBat;
    }

    public int attackerNumTroops() {
        return aInf + aTank + aFig + aBom + aSub + aDes + aACC + aBat + aExtra;
    }

    public int defenderNumTroops() {
        return dInf + dTank + dFig + dBom + dSub + dDes + dACC + dBat + dExtra;
    }

    public Combat.Hits rollAttackerHits(boolean subsOnly, boolean noSubs) {
        Combat.Hits hits = new Combat.Hits();
        if (!noSubs) hits.subHits += Unit.SUBMARINE.rollHits(true, aSub);
        if (!subsOnly) {
            hits.otherHits += Unit.INFANTRY.rollHits(true, aInf);
            hits.otherHits += Unit.TANK.rollHits(true, aTank);
            hits.airHits += Unit.FIGHTER.rollHits(true, aFig);
            hits.airHits += Unit.BOMBER.rollHits(true, aBom);
            hits.otherHits += Unit.DESTROYER.rollHits(true, aDes);
            hits.otherHits += Unit.CARRIER.rollHits(true, aACC);
            hits.otherHits += Unit.BATTLESHIP.rollHits(true, aBat);
        }
        return hits;
    }

    public Combat.Hits rollDefenderHits(boolean subsOnly, boolean noSubs) {
        Combat.Hits hits = new Combat.Hits();
        if (!noSubs) hits.subHits += Unit.SUBMARINE.rollHits(false, dSub);
        if (!subsOnly) {
            hits.otherHits += Unit.INFANTRY.rollHits(false, dInf);
            hits.otherHits += Unit.TANK.rollHits(false, dTank);
            hits.airHits += Unit.FIGHTER.rollHits(false, dFig);
            hits.airHits += Unit.BOMBER.rollHits(false, dBom);
            hits.otherHits += Unit.DESTROYER.rollHits(false, dDes);
            hits.otherHits += Unit.CARRIER.rollHits(false, dACC);
            hits.otherHits += Unit.BATTLESHIP.rollHits(false, dBat);
        }
        return hits;
    }

    public void applyHits(Combat.Hits hits, boolean defense, boolean destroyersPresent) {
        while (hits.airHits + hits.subHits + hits.otherHits > 0) {
            boolean applied = false;
            if ((defense ? dExtra : aExtra ) > 0) {
                if (!hits.remove(true, true)) continue;
                if (defense) dExtra--;
                else aExtra--;
                continue;
            }
            for (Unit u : Unit.getUnitOrder(defense, seaBattle)) {
                if (applied) break;
                switch (u) {
                    case INFANTRY -> {
                        if ((defense ? dInf : aInf) == 0) continue;
                        if (!hits.remove(true, true)) continue;
                        if (defense) dInf--;
                        else aInf--;
                        applied = true;
                    } case TANK -> {
                        if ((defense ? dTank : aTank) == 0) continue;
                        if (!hits.remove(true, true)) continue;
                        if (defense) dTank--;
                        else aTank--;
                        applied = true;
                    } case FIGHTER -> {
                        if ((defense ? dFig : aFig) == 0) continue;
                        if (!hits.remove(true, false)) continue;
                        if (defense) dFig--;
                        else aFig--;
                        applied = true;
                    } case BOMBER -> {
                        if ((defense ? dBom : aBom) == 0) continue;
                        if (!hits.remove(true, false)) continue;
                        if (defense) dBom--;
                        else aBom--;
                        applied = true;
                    } case SUBMARINE -> {
                        if ((defense ? dSub : aSub) == 0) continue;
                        if (!hits.remove(destroyersPresent, true)) continue;
                        if (defense) dSub--;
                        else aSub--;
                        applied = true;
                    } case DESTROYER -> {
                        if ((defense ? dDes : aDes) == 0) continue;
                        if (!hits.remove(true, true)) continue;
                        if (defense) dDes--;
                        else aDes--;
                        applied = true;
                    } case CARRIER -> {
                        if ((defense ? dACC : aACC) == 0) continue;
                        if (!hits.remove(true, true)) continue;
                        if (defense) dACC--;
                        else aACC--;
                        applied = true;
                    } case BATTLESHIP -> {
                        if ((defense ? dBat : aBat) == 0) continue;
                        if (!hits.remove(true, true)) continue;
                        if (defense) dBat--;
                        else aBat--;
                        applied = true;
                    }
                }
            }
            if (!applied) break;
        }
    }

    @Override
    public String toString() {
        return "Hits(" +
                "aInf=" + aInf + ", " +
                "aTank=" + aTank + ", " +
                "aFig=" + aFig + ", " +
                "aBom=" + aBom + ", " +
                "aSub=" + aSub + ", " +
                "aDes=" + aDes + ", " +
                "aACC=" + aACC + ", " +
                "aBat=" + aBat + ", " +
                "dInf=" + dInf + ", " +
                "dTank=" + dTank + ", " +
                "dFig=" + dFig + ", " +
                "dBom=" + dBom + ", " +
                "dSub=" + dSub + ", " +
                "dDes=" + dDes + ", " +
                "dACC=" + dACC + ", " +
                "dBat=" + dBat +
                ')';
    }
}
