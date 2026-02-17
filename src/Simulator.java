public class Simulator {

    public record SimulationResult(int simulations, int timeNs, double winRate, double winMOE,
                                   double lossRate, double lossMOE, double drawRate, double drawMOE,
                                   double attackerSurviveRate, double attackerSurviveMOE,
                                   double defenderSurviveRate, double defenderSurviveMOE,
                                   double atkWin1Rate, double atkWin2Rate, double defWin1Rate,
                                   double defWin2Rate) {
            @Override
            public String toString() {
                return ("Simulations: " + simulations) + '\n' +
                        ("Time (ms): " + timeNs / 10_000 / 100.0) + '\n' + '\n' +
                        ("Attacker win rate: " + ((int) (winRate * 1000 + 0.5)) / 10.0 + "% ±" + ((int) ((winMOE) * 1000 + 0.5)) / 10.0 + "%") + '\n' +
                        ("Defender win rate: " + ((int) (lossRate * 1000 + 0.5)) / 10.0 + "% ±" + ((int) ((lossMOE) * 1000 + 0.5)) / 10.0 + "%") + '\n' +
                        ("Draw rate: " + ((int) (drawRate * 1000 + 0.5)) / 10.0 + "% ±" + ((int) ((drawMOE) * 1000 + 0.5)) / 10.0 + "%") + '\n' +
                        ("Atk survive: " + ((int) (attackerSurviveRate * 1000 + 0.5)) / 10.0 + "% ±" + ((int) ((attackerSurviveMOE) * 1000 + 0.5)) / 10.0 + "%") + '\n' +
                        ("Def survive: " + ((int) (defenderSurviveRate * 1000 + 0.5)) / 10.0 + "%" + " ±" + ((int) ((defenderSurviveMOE) * 1000 + 0.5)) / 10.0 + "%") + '\n' +
                        ("Atk 1 win rate: " + ((int) (atkWin1Rate * 1000 + 0.5)) / 10.0) + "%\n" +
                        ("Atk 2 win rate: " + ((int) (atkWin2Rate * 1000 + 0.5)) / 10.0) + "%\n" +
                        ("Def 1 win rate: " + ((int) (defWin1Rate * 1000 + 0.5)) / 10.0) + "%\n" +
                        ("Def 2 win rate: " + ((int) (defWin2Rate * 1000 + 0.5)) / 10.0) + "%\n";
            }
        }

    public static SimulationResult simulate(Battle battle, int minSims, double timeLimit,
                                            double maxMOE, boolean debug, int nDebug) {
        long start = System.nanoTime();

        int wins = 0;
        int draws = 0;
        int losses = 0;
        int atkSurvives = 0;
        int defSurvives = 0;
        int sims = 0;
        int atkWin1s = 0;
        int atkWin2s = 0;
        int defWin1s = 0;
        int defWin2s = 0;

        while (true) {
            if (debug && nDebug <= sims) break;
            if (debug) System.out.println("\n\nSim " + (sims + 1));
            OngoingBattle ongoingBattle = new OngoingBattle(battle);
            Combat.Result r = Combat.simulateBattle(ongoingBattle, debug);
            if (debug) System.out.println(r);

            if (r.attackerWin) wins++;
            if (r.draw) draws++;
            if (r.defenderWin) losses++;
            if (r.attackerSurvives) atkSurvives++;
            if (r.defenderSurvives) defSurvives++;
            if (r.attackerWin1) atkWin1s++;
            if (r.attackerWin2) atkWin2s++;
            if (r.defenderWin1) defWin1s++;
            if (r.defenderWin2) defWin2s++;

            sims++;

            if (sims % 25 == 0 && System.currentTimeMillis() - start > timeLimit) break;
            if (sims < minSims || sims % 25 != 0) continue;

            double winMean = wins / (double) sims;
            double winMOE = 1.96 * Math.sqrt((winMean * (1 - winMean)) / sims);
            if (winMOE < maxMOE) break;
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
        double atkWin1Mean = atkWin1s / (double) sims;
        double atkWin2Mean = atkWin2s / (double) sims;
        double defWin1Mean = defWin1s / (double) sims;
        double defWin2Mean = defWin2s / (double) sims;

        return new SimulationResult(sims, (int) (System.nanoTime() - start), winMean,
                winMOE, lossMean, lossMOE, drawMean, drawMOE, atkMean, atkMOE, defMean, defMOE,
                atkWin1Mean, atkWin2Mean, defWin1Mean, defWin2Mean);
    }
}
