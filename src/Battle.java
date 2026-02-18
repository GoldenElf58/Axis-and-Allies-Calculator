import java.util.EnumMap;
import java.util.Map;
import java.util.Scanner;

public enum Battle {
    ASK,
    LAND_1(false, 5, 2, 1, 1, 0, 0, 0, 0, 5, 1, 1, 1, 0, 0, 0, 0),
    SEA_1(true, 0, 0, 1, 1, 2, 2, 0, 0, 0, 0, 1, 0, 0, 1, 1, 1),
    SEA_2(true, 0, 0, 1, 1, 2, 2, 0, 0, 0, 0, 1, 0, 1, 0, 1, 1),
    SEA_3(true, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 1, 0, 0, 1, 1, 0),
    SEA_4(true, 0, 0, 0, 0, 2, 2, 0, 0, 0, 0, 0, 0, 1, 0, 1, 1),
    SEA_5(true, 0, 0, 0, 0, 2, 2, 0, 0, 0, 0, 0, 0, 1, 2, 1, 0),
    SEA_6(true, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 1, 0, 0, 1, 1, 1),
    SEA_7(true, 0, 0, 1, 1, 2, 2, 0, 0, 0, 0, 2, 0, 1, 2, 1, 0),
    SEA_8(true, 0, 0, 1, 0, 2, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1),
    SEA_9(true, 0, 0, 1, 0, 2, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1),
    SEA_LONG(true, 0, 0, 2, 1, 2, 2, 0, 2, 0, 0, 2, 0, 5, 2, 1, 1);

    public final boolean ask;
    public boolean seaBattle;
    public byte aInf, aTank, aFig, aBom, aSub, aDes, aACC, aBat;
    public byte dInf, dTank, dFig, dBom, dSub, dDes, dACC, dBat;

    Battle(boolean seaBattle, int aInf, int aTank, int aFig, int aBom, int aSub, int aDes,
           int aACC, int aBat, int dInf, int dTank, int dFig, int dBom, int dSub, int dDes,
           int dACC, int dBat) {
        this.ask = false;
        this.seaBattle = seaBattle;
        this.aInf = (byte) aInf;
        this.aTank = (byte) aTank;
        this.aFig = (byte) aFig;
        this.aBom = (byte) aBom;
        this.aSub = (byte) aSub;
        this.aDes = (byte) aDes;
        this.aACC = (byte) aACC;
        this.aBat = (byte) aBat;
        this.dInf = (byte) dInf;
        this.dTank = (byte) dTank;
        this.dFig = (byte) dFig;
        this.dBom = (byte) dBom;
        this.dSub = (byte) dSub;
        this.dDes = (byte) dDes;
        this.dACC = (byte) dACC;
        this.dBat = (byte) dBat;
    }

    Battle() {
        this.ask = true;
    }

    public void getBattle() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Is this a sea battle? (y/N): ");
        this.seaBattle = sc.nextLine().trim().equalsIgnoreCase("y");
        Map<Unit, Integer> attackers = new EnumMap<>(Unit.class);
        Map<Unit, Integer> defenders = new EnumMap<>(Unit.class);
        for (Unit u : Unit.getUnitOrder(false, seaBattle)) {
            System.out.print("Attacker " + u.name().toLowerCase() + ": ");
            String s = sc.nextLine().trim();
            attackers.put(u, s.isEmpty() ? 0 : Integer.parseInt(s));
        }
        for (Unit u : Unit.getUnitOrder(true, seaBattle)) {
            System.out.print("Defender " + u.name().toLowerCase() + ": ");
            String s = sc.nextLine().trim();
            defenders.put(u, s.isEmpty() ? 0 : Integer.parseInt(s));
        }
        this.aInf = (byte) (int) attackers.getOrDefault(Unit.INFANTRY, 0);
        this.aTank = (byte) (int) attackers.getOrDefault(Unit.TANK, 0);
        this.aFig = (byte) (int) attackers.getOrDefault(Unit.FIGHTER, 0);
        this.aBom = (byte) (int) attackers.getOrDefault(Unit.BOMBER, 0);
        this.aSub = (byte) (int) attackers.getOrDefault(Unit.SUBMARINE, 0);
        this.aDes = (byte) (int) attackers.getOrDefault(Unit.DESTROYER, 0);
        this.aACC = (byte) (int) attackers.getOrDefault(Unit.CARRIER, 0);
        this.aBat = (byte) (int) attackers.getOrDefault(Unit.BATTLESHIP, 0);
        this.dInf = (byte) (int) defenders.getOrDefault(Unit.INFANTRY, 0);
        this.dTank = (byte) (int) defenders.getOrDefault(Unit.TANK, 0);
        this.dFig = (byte) (int) defenders.getOrDefault(Unit.FIGHTER, 0);
        this.dBom = (byte) (int) defenders.getOrDefault(Unit.BOMBER, 0);
        this.dSub = (byte) (int) defenders.getOrDefault(Unit.SUBMARINE, 0);
        this.dDes = (byte) (int) defenders.getOrDefault(Unit.DESTROYER, 0);
        this.dACC = (byte) (int) defenders.getOrDefault(Unit.CARRIER, 0);
        this.dBat = (byte) (int) defenders.getOrDefault(Unit.BATTLESHIP, 0);
    }
}