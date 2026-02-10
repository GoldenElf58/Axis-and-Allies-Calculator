import java.util.EnumMap;
import java.util.Map;
import java.util.Scanner;

enum Battle {
    ASK,
    DEFAULT_LAND(false, 5, 2, 1, 1, 0, 0, 0, 0, 5, 1, 1, 1, 0, 0, 0, 0),
    SEA_1(true, 0, 0, 1, 1, 2, 2, 0, 0, 0, 0, 1, 0, 0, 1, 1, 1),
    SEA_2(true, 0, 0, 1, 1, 2, 2, 0, 0, 0, 0, 1, 0, 1, 0, 1, 1),
    SEA_LONG(true, 0, 0, 2, 1, 2, 2, 0, 2, 0, 0, 2, 0, 5, 2, 1, 1);

    private boolean seaBattle, askedSeaBattle = false;
    private final boolean ask;
    private final int aInf, aTank, aFig, aBom, aSub, aDes, aACC, aBat;
    private final int dInf, dTank, dFig, dBom, dSub, dDes, dACC, dBat;

    Battle(boolean seaBattle, int aInf, int aTank, int aFig, int aBom, int aSub, int aDes,
           int aACC, int aBat, int dInf, int dTank, int dFig, int dBom, int dSub, int dDes,
           int dACC, int dBat) {
        this.ask = false;
        this.seaBattle = seaBattle;
        this.aInf = aInf;
        this.aTank = aTank;
        this.aFig = aFig;
        this.aBom = aBom;
        this.aSub = aSub;
        this.aDes = aDes;
        this.aACC = aACC;
        this.aBat = aBat;
        this.dInf = dInf;
        this.dTank = dTank;
        this.dFig = dFig;
        this.dBom = dBom;
        this.dSub = dSub;
        this.dDes = dDes;
        this.dACC = dACC;
        this.dBat = dBat;
    }

    Battle() {
        this.ask = true;
        this.seaBattle = false;
        this.aInf = this.aTank = this.aFig = this.aBom = this.aSub = this.aDes = this.aACC = this.aBat = 0;
        this.dInf = this.dTank = this.dFig = this.dBom = this.dSub = this.dDes = this.dACC = this.dBat = 0;
    }

    public Map<Unit, Integer> getAttackerMap() {
        if (ask) {
            Scanner sc = new Scanner(System.in);
            if (!askedSeaBattle) seaBattle = Input.getSeaBattle(sc);
            askedSeaBattle = true;
            return Input.readArmy(sc, "attacker", false, seaBattle);
        }
        return getUnitIntegerMap(aInf, aTank, aFig, aBom, aSub, aDes, aACC, aBat);
    }

    public Map<Unit, Integer> getDefenderMap() {
        if (ask) {
            Scanner sc = new Scanner(System.in);
            if (!askedSeaBattle) seaBattle = Input.getSeaBattle(sc);
            askedSeaBattle = true;
            return Input.readArmy(sc, "defender", true, seaBattle);
        }
        return getUnitIntegerMap(dInf, dTank, dFig, dBom, dSub, dDes, dACC, dBat);
    }

    private Map<Unit, Integer> getUnitIntegerMap(int inf, int tank, int fig, int bom, int sub,
                                                 int des, int ACC, int bat) {
        Map<Unit, Integer> defenderMap = new EnumMap<>(Unit.class);
        defenderMap.put(Unit.INFANTRY, inf);
        defenderMap.put(Unit.TANK, tank);
        defenderMap.put(Unit.FIGHTER, fig);
        defenderMap.put(Unit.BOMBER, bom);
        defenderMap.put(Unit.SUBMARINE, sub);
        defenderMap.put(Unit.DESTROYER, des);
        defenderMap.put(Unit.CARRIER, ACC);
        defenderMap.put(Unit.BATTLESHIP, bat);
        return defenderMap;
    }

    public boolean isSeaBattle() {
        return seaBattle;
    }
}