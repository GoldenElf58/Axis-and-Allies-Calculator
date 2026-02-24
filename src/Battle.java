import java.util.EnumMap;
import java.util.Map;
import java.util.Scanner;

public enum Battle {
    ASK(true),
    MANUAL(false),
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
    public int aInf, aTank, aFig, aBom, aSub, aDes, aACC, aBat;
    public int dInf, dTank, dFig, dBom, dSub, dDes, dACC, dBat;

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

    Battle(boolean ask) {
        this.ask = ask;
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
        this.aInf = attackers.getOrDefault(Unit.INFANTRY, 0);
        this.aTank = attackers.getOrDefault(Unit.TANK, 0);
        this.aFig = attackers.getOrDefault(Unit.FIGHTER, 0);
        this.aBom = attackers.getOrDefault(Unit.BOMBER, 0);
        this.aSub = attackers.getOrDefault(Unit.SUBMARINE, 0);
        this.aDes = attackers.getOrDefault(Unit.DESTROYER, 0);
        this.aACC = attackers.getOrDefault(Unit.CARRIER, 0);
        this.aBat = attackers.getOrDefault(Unit.BATTLESHIP, 0);
        this.dInf = defenders.getOrDefault(Unit.INFANTRY, 0);
        this.dTank = defenders.getOrDefault(Unit.TANK, 0);
        this.dFig = defenders.getOrDefault(Unit.FIGHTER, 0);
        this.dBom = defenders.getOrDefault(Unit.BOMBER, 0);
        this.dSub = defenders.getOrDefault(Unit.SUBMARINE, 0);
        this.dDes = defenders.getOrDefault(Unit.DESTROYER, 0);
        this.dACC = defenders.getOrDefault(Unit.CARRIER, 0);
        this.dBat = defenders.getOrDefault(Unit.BATTLESHIP, 0);
    }

    @Override
    public String toString() {
        return "" + aInf + '\t' + aTank + '\t' + aFig + '\t' + aBom + '\t' + aSub + '\t' + aDes +
                '\t' + aACC + '\t' + aBat + '\t' + dInf + '\t' + dTank + '\t' + dFig + '\t' + dBom +
                '\t' + dSub + '\t' + dDes + '\t' + dACC + '\t' + dBat + '\t' + seaBattle;
    }

    public int totalAttackerIPC() {
        return aInf * Unit.INFANTRY.cost + aTank * Unit.TANK.cost + aFig * Unit.FIGHTER.cost +
                aBom * Unit.BOMBER.cost + aSub * Unit.SUBMARINE.cost + aDes * Unit.DESTROYER.cost +
                aACC * Unit.CARRIER.cost + aBat * Unit.BATTLESHIP.cost;
    }

    public int totalDefenderIPC() {
        return dInf * Unit.INFANTRY.cost + dTank * Unit.TANK.cost + dFig * Unit.FIGHTER.cost +
                dBom * Unit.BOMBER.cost + dSub * Unit.SUBMARINE.cost + dDes * Unit.DESTROYER.cost +
                dACC * Unit.CARRIER.cost + dBat * Unit.BATTLESHIP.cost;
    }

    public int totalAttackerAttack() {
        return aInf * Unit.INFANTRY.attack + aTank * Unit.TANK.attack + aFig * Unit.FIGHTER.attack +
                aBom * Unit.BOMBER.attack + aSub * Unit.SUBMARINE.attack + aDes * Unit.DESTROYER.attack +
                aACC * Unit.CARRIER.attack + aBat * Unit.BATTLESHIP.attack;
    }

    public int totalDefenderDefense() {
        return dInf * Unit.INFANTRY.defense + dTank * Unit.TANK.defense + dFig * Unit.FIGHTER.defense +
                dBom * Unit.BOMBER.defense + dSub * Unit.SUBMARINE.defense + dDes * Unit.DESTROYER.defense +
                dACC * Unit.CARRIER.defense + dBat * Unit.BATTLESHIP.defense;
    }
}