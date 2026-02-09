import java.util.*;

import static java.lang.Math.sqrt;

public class Main {

    static final double WINRATE_MAX_MOE = 0.01;
    static final double IPC_MAX_MOE = 1;
    static final long TIME_LIMIT_MS = 5000;
    static final int MIN_SIMS = 250000;

    static final Battle battle = Battle.DEFAULT_LAND;

    enum Battle {
        ASK,
        DEFAULT_LAND,
        DEFAULT_SEA,
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        boolean seaBattle;
        Map<UnitType, Integer> attackerMap;
        Map<UnitType, Integer> defenderMap;
        switch (battle) {
            case DEFAULT_LAND:
                seaBattle = false;
                attackerMap = new EnumMap<>(UnitType.class);
                attackerMap.put(UnitType.INFANTRY, 5);
                attackerMap.put(UnitType.TANK, 2);
                attackerMap.put(UnitType.FIGHTER, 1);
                attackerMap.put(UnitType.BOMBER, 1);
                defenderMap = new EnumMap<>(UnitType.class);
                defenderMap.put(UnitType.INFANTRY, 5);
                defenderMap.put(UnitType.TANK, 1);
                defenderMap.put(UnitType.FIGHTER, 1);
                defenderMap.put(UnitType.BOMBER, 1);
                break;
            case ASK:
            default:
                seaBattle = Input.getSeaBattle(sc);
                attackerMap = Input.readArmy(sc, "attacker", false, seaBattle);
                defenderMap = Input.readArmy(sc, "defender", true, seaBattle);
                break;
        }

        long start = System.currentTimeMillis();

        int wins = 0;
        int draws = 0;
        int losses = 0;
        List<Double> attackerLosses = new ArrayList<>();
        List<Double> defenderLosses = new ArrayList<>();

        int sims = 0;

        while (true) {
            Combat.Result r = Combat.simulateBattle(
                    Combat.buildArmy(attackerMap),
                    Combat.buildArmy(defenderMap),
                    seaBattle
            );

            wins += r.attackerWin ? 1 : 0;
            draws += r.draw ? 1 : 0;
            losses += r.defenderWin ? 1 : 0;
            attackerLosses.add((double) r.attackerIPCLoss);
            defenderLosses.add((double) r.defenderIPCLoss);
            sims++;

            if (System.currentTimeMillis() - start > TIME_LIMIT_MS) break;
            if (sims < MIN_SIMS || sims % 25 != 0) continue;

            double winMean = wins / (double) sims;
            double winMOE = 1.96 * Math.sqrt((winMean * (1 - winMean)) / sims);
            if (winMOE > WINRATE_MAX_MOE) continue;

            double ipcMean = Stats.mean(attackerLosses);
            double ipcStd = Stats.std(attackerLosses, ipcMean);
            double ipcMOE = 1.96 * ipcStd / sqrt(sims);
            if (ipcMOE <= IPC_MAX_MOE) break;
        }

        double winMean = wins / (double) sims;
        double winMOE = 1.96 * Math.sqrt((winMean * (1 - winMean)) / sims);
        double drawMean = draws / (double) sims;
        double drawMOE = 1.96 * Math.sqrt((drawMean * (1 - drawMean)) / sims);
        double lossMean = losses / (double) sims;
        double lossMOE = 1.96 * Math.sqrt((lossMean * (1 - lossMean)) / sims);

        double ipcAtkMean = Stats.mean(attackerLosses);
        double ipcAtkMOE = 1.96 * Stats.std(attackerLosses, ipcAtkMean) / Math.sqrt(sims);
        Collections.sort(attackerLosses);
        double ipcAtkLow = attackerLosses.get((int) (0.025 * sims));
        double ipcAtkHigh = attackerLosses.get((int) (0.975 * sims));

        double ipcDefMean = Stats.mean(defenderLosses);
        double ipcDefMOE = 1.96 * Stats.std(defenderLosses, ipcDefMean) / Math.sqrt(sims);
        Collections.sort(defenderLosses);
        double ipcDefLow = defenderLosses.get((int) (0.025 * sims));
        double ipcDefHigh = defenderLosses.get((int) (0.975 * sims));

        System.out.println("\n");
        System.out.println("Simulations: " + sims);
        System.out.println("Time (ms): " + (System.currentTimeMillis() - start));
        System.out.println();
        System.out.println("Attacker IPC Loss: " + ((int) (ipcAtkMean * 10)) / 10.0 + " ±" + ((int) ((ipcAtkMOE) * 10)) / 10.0);
        System.out.println("Attacker IPC Loss 95% outcome range: [" + ipcAtkLow + ", " + ipcAtkHigh + "]");
        System.out.println("Defender IPC Loss: " + ((int) (ipcDefMean * 10)) / 10.0 + " ±" + ((int) ((ipcDefMOE) * 10)) / 10.0);
        System.out.println("Defender IPC Loss 95% outcome range: [" + ipcDefLow + ", " + ipcDefHigh + "]");
        System.out.println();
        System.out.println("Attacker win rate: " + ((int) (winMean * 1000)) / 10.0 + "% ±" + ((int) ((winMOE) * 1000)) / 10.0 + "%");
        System.out.println("Defender win rate: " + ((int) (lossMean * 1000)) / 10.0 + "% ±" + ((int) ((lossMOE) * 1000)) / 10.0 + "%");
        System.out.println("Draw rate: " + ((int) (drawMean * 1000)) / 10.0 + "% ±" + ((int) ((drawMOE) * 1000)) / 10.0 + "%");
    }
}