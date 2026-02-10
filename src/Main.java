import java.util.*;

import static java.lang.Math.sqrt;

public class Main {

    static final double WINRATE_MAX_MOE = 0.001;
    static final double IPC_MAX_MOE = 1;
    static final long TIME_LIMIT_MS = 5000;
    static final int MIN_SIMS = 500;

    static final Battle battle = Battle.ASK;

    public static void main(String[] args) {
        Map<Unit, Integer> attackerMap = battle.getAttackerMap();
        Map<Unit, Integer> defenderMap = battle.getDefenderMap();
        boolean seaBattle = battle.isSeaBattle();

        long start = System.currentTimeMillis();

        int wins = 0;
        int draws = 0;
        int losses = 0;
        int atkSurvives = 0;
        int defSurvives = 0;
        List<Double> attackerLosses = new ArrayList<>();
        List<Double> defenderLosses = new ArrayList<>();

        int sims = 0;

        while (true) {
            Combat.Result r = Combat.simulateBattle(
                    Combat.buildArmy(attackerMap),
                    Combat.buildArmy(defenderMap),
                    seaBattle
            );

            if (r.attackerWin) wins++;
            if (r.draw) draws++;
            if (r.defenderWin) losses++;
            if (r.attackerSurvives) atkSurvives++;
            if (r.defenderSurvives) defSurvives++;

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
        double atkMean = atkSurvives / (double) sims;
        double atkMOE = 1.96 * Math.sqrt((atkMean * (1 - atkMean)) / sims);
        double defMean = defSurvives / (double) sims;
        double defMOE = 1.96 * Math.sqrt((defMean * (1 - defMean)) / sims);

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
        System.out.println("Attacker IPC Loss: " + ((int) (ipcAtkMean * 10 + 0.5)) / 10.0 + " ±" + ((int) ((ipcAtkMOE) * 10 + 0.5)) / 10.0);
        System.out.println("Attacker IPC Loss 95% outcome range: [" + ipcAtkLow + ", " + ipcAtkHigh + "]");
        System.out.println("Defender IPC Loss: " + ((int) (ipcDefMean * 10 + 0.5)) / 10.0 + " ±" + ((int) ((ipcDefMOE) * 10 + 0.5)) / 10.0);
        System.out.println("Defender IPC Loss 95% outcome range: [" + ipcDefLow + ", " + ipcDefHigh + "]");
        System.out.println();
        System.out.println("Attacker win rate: " + ((int) (winMean * 1000 + 0.5)) / 10.0 + "% ±" + ((int) ((winMOE) * 1000 + 0.5)) / 10.0 + "%");
        System.out.println("Defender win rate: " + ((int) (lossMean * 1000 + 0.5)) / 10.0 + "% ±" + ((int) ((lossMOE) * 1000 + 0.5)) / 10.0 + "%");
        System.out.println("Draw rate: " + ((int) (drawMean * 1000 + 0.5)) / 10.0 + "% ±" + ((int) ((drawMOE) * 1000 + 0.5)) / 10.0 + "%");
        System.out.println("Atk survive: " + ((int) (atkMean * 1000 + 0.5)) / 10.0 + "% ±" + ((int) ((atkMOE) * 1000 + 0.5)) / 10.0 + "%");
        System.out.println("Def survive: " + ((int) (defMean * 1000 + 0.5)) / 10.0 + "% ±" + ((int) ((defMOE) * 1000 + 0.5)) / 10.0 + "%");
    }
}