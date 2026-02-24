import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    static final double WINRATE_MAX_MOE = .01;
    static final long TIME_LIMIT_MS = 1000;
    static final int MIN_SIMS = 3000;
    static final boolean DEBUG = false;
    static final int N_DEBUG = 10;
    static final boolean BENCHMARK = false;
    static final boolean SIMULATE = true;
    static final int MAX_IPC = 30;

    static final Battle battle = Battle.LAND_1;

    public static void main(String[] args) {
        if (battle.ask) battle.getBattle();
        if (BENCHMARK && !DEBUG) {
            System.out.println("Warm up...");
            Simulator.simulate(battle, MIN_SIMS * 1000, 10 * TIME_LIMIT_MS,
                    WINRATE_MAX_MOE, false, 0);
            System.out.println("Warm Up Complete");
            int n = 500;
            long start = System.nanoTime();
            for (int i = 0; i < n; i++)
                Simulator.simulate(battle, MIN_SIMS, TIME_LIMIT_MS, WINRATE_MAX_MOE, DEBUG, N_DEBUG);
            System.out.println("Avg Time: " + time((System.nanoTime() - start) / n));
        } else if (SIMULATE) {
            System.out.println("Simulating all battles...");
            long start = System.nanoTime();
            simulateAllBattles();
            System.out.println("Time: " + time(System.nanoTime() - start, 3));
        } else {
            System.out.println("Battle: " + battle.name() + "\n");
            System.out.println(Simulator.simulate(battle, MIN_SIMS, TIME_LIMIT_MS, WINRATE_MAX_MOE,
                    DEBUG, N_DEBUG));
        }
    }


    public static String time(long time, int precision) {
        String fmt = "%,." + precision + "g";
        if (time < 1_000) return String.format("%s ns", String.format(fmt, (double) time));
        if (time < 1_000_000) return String.format("%s µs", String.format(fmt, time / 1_000.0));
        if (time < 1_000_000_000)
            return String.format("%s ms", String.format(fmt, time / 1_000_000.0));
        if (time < 1_000_000_000_000L)
            return String.format("%s s", String.format(fmt, time / 1_000_000_000.0));
        return String.format("%d:%s min", (int) (time / 60_000_000_000.0), String.format(fmt,
                time % 60_000_000_000.0));
    }

    public static String time(long time) {
        return time(time, 5);
    }

    public static void simulateAllBattles() {
        try (FileWriter writer = new FileWriter("battle_results.txt")) {
            writer.write("IPC\tA_Inf\tA_Tank\tA_Fig\tA_Bom\tA_Sub\tA_Des\tA_ACC\tA_Bat\tD_Inf" +
                    "\tD_Tank\tD_Fig\tD_Bom\tD_Sub\tD_Des\tD_ACC\tD_Bat\tSea\tAtk Win 1\t" +
                    "Def Win 1\tAtk Win 2\tDef Win 2\tAC\tAA\tDC\tDD\n");

            for (byte ipc = 1; ipc <= MAX_IPC; ipc++) {
                System.out.println(ipc);
                generateAndSimulateBattles(writer, ipc, true, true);
                generateAndSimulateBattles(writer, ipc, false, true);
                generateAndSimulateBattles(writer, ipc, true, false);
                generateAndSimulateBattles(writer, ipc, false, false);
            }
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    private static void generateAndSimulateBattles(FileWriter writer, byte ipc, boolean attackersExact,
                                                   boolean landBattle) throws IOException {
        List<int[]> attackerCombos = generateUnitCombinations(ipc, attackersExact, landBattle);
        List<int[]> defenderCombos = generateUnitCombinations(ipc, !attackersExact, landBattle);
        for (int[] attackers : attackerCombos) {
            for (int[] defenders : defenderCombos) {
                if (isValidBattle(attackers, defenders, landBattle)) {
                    if (!attackersExact && calculateIPC(attackers, landBattle) == calculateIPC(defenders, landBattle))
                        continue;
                    
                    Battle battle = createBattleFromUnits(attackers, defenders, landBattle);
                    String result = Simulator.simulate(battle, MIN_SIMS, TIME_LIMIT_MS,
                            WINRATE_MAX_MOE, DEBUG, N_DEBUG).toLine();
                    String stats = "" + battle.totalAttackerIPC() + '\t' +
                            battle.totalAttackerAttack() + '\t' + battle.totalDefenderIPC() + '\t' +
                            battle.totalDefenderDefense();

                    String output = "" + ipc + '\t' + battle + '\t' + result + '\t' + stats + '\n';
                    writer.write(output);
                }
            }
        }
    }

    private static List<int[]> generateUnitCombinations(int maxIPC, boolean exact, boolean landBattle) {
        List<int[]> combinations = new ArrayList<>();
        Unit[] units = landBattle ?
                new Unit[]{Unit.INFANTRY, Unit.TANK, Unit.FIGHTER, Unit.BOMBER} :
                new Unit[]{Unit.SUBMARINE, Unit.DESTROYER, Unit.CARRIER, Unit.BATTLESHIP,
                        Unit.FIGHTER, Unit.BOMBER};

        generateCombinationsRecursive(units, 0, new int[units.length], 0, maxIPC, exact, combinations);
        return combinations;
    }

    private static void generateCombinationsRecursive(Unit[] units, int index, int[] current,
                                                      int currentIPC, int maxIPC, boolean exact,
                                                      List<int[]> combinations) {
        if (index == units.length) {
            if (exact ? currentIPC == maxIPC : currentIPC <= maxIPC)
                combinations.add(current.clone());
            return;
        }

        Unit unit = units[index];
        int maxCount = (maxIPC - (exact ? 0 : currentIPC)) / unit.cost;

        for (int count = 0; count <= maxCount; count++) {
            current[index] = count;
            int newIPC = currentIPC + (count * unit.cost);
            if (newIPC <= maxIPC)
                generateCombinationsRecursive(units, index + 1, current, newIPC, maxIPC, exact, combinations);
        }
        current[index] = 0;
    }

    private static boolean isValidBattle(int[] attackers, int[] defenders, boolean landBattle) {
        // Check if both sides have units
        boolean attackerHasUnits = hasUnits(attackers);
        boolean defenderHasUnits = hasUnits(defenders);
        if (!attackerHasUnits || !defenderHasUnits) return false;

        // For sea battles, apply defender restrictions
        if (!landBattle) {
            // No bombers for sea defenders
            int defenderBomberIndex = 5; // BOMBER is 6th in sea units array
            if (defenderBomberIndex < defenders.length && defenders[defenderBomberIndex] > 0)
                return false;

            // Every 2 fighters need 1 carrier for defenders
            int defenderFighterIndex = 4; // FIGHTER is 5th in sea units array  
            int defenderCarrierIndex = 2; // CARRIER is 3rd in sea units array
            if (defenderFighterIndex < defenders.length) {
                int fighters = defenders[defenderFighterIndex];
                int carriers = defenders[defenderCarrierIndex];
                if (fighters > carriers * 2) return false;
            }
        }

        // Skip air-only sea battles
        if (!landBattle) {
            boolean hasSeaUnits = false;
            // Check first 4 units (submarine, destroyer, carrier, battleship)
            for (int i = 0; i < 4 && i < attackers.length; i++) {
                if (attackers[i] > 0) {
                    hasSeaUnits = true;
                    break;
                }
            }
            for (int i = 0; i < 4 && i < defenders.length; i++) {
                if (defenders[i] > 0) {
                    hasSeaUnits = true;
                    break;
                }
            }
            return hasSeaUnits;
        }

        return true;
    }

    private static int calculateIPC(int[] units, boolean landBattle) {
        Unit[] unitTypes = landBattle ?
                new Unit[]{Unit.INFANTRY, Unit.TANK, Unit.FIGHTER, Unit.BOMBER} :
                new Unit[]{Unit.SUBMARINE, Unit.DESTROYER, Unit.CARRIER, Unit.BATTLESHIP, Unit.FIGHTER, Unit.BOMBER};
        
        int totalIPC = 0;
        for (int i = 0; i < units.length && i < unitTypes.length; i++)
            totalIPC += units[i] * unitTypes[i].cost;
        return totalIPC;
    }

    private static boolean hasUnits(int[] units) {
        for (int count : units) if (count > 0) return true;
        return false;
    }

    private static Battle createBattleFromUnits(int[] attackers, int[] defenders, boolean landBattle) {
        Battle battle = Battle.MANUAL;
        battle.seaBattle = !landBattle;

        if (landBattle) {
            battle.aInf = attackers[0];
            battle.aTank = attackers[1];
            battle.aFig = attackers[2];
            battle.aBom = attackers[3];
            battle.dInf = defenders[0];
            battle.dTank = defenders[1];
            battle.dFig = defenders[2];
            battle.dBom = defenders[3];
            battle.aSub = battle.aDes = battle.aACC = battle.aBat = 0;
            battle.dSub = battle.dDes = battle.dACC = battle.dBat = 0;
        } else {
            battle.aSub = attackers[0];
            battle.aDes = attackers[1];
            battle.aACC = attackers[2];
            battle.aBat = attackers[3];
            battle.aFig = attackers[4];
            battle.aBom = attackers[5];
            battle.dSub = defenders[0];
            battle.dDes = defenders[1];
            battle.dACC = defenders[2];
            battle.dBat = defenders[3];
            battle.dFig = defenders[4];
            battle.aInf = battle.aTank = 0;
            battle.dInf = battle.dTank = battle.dBom = 0;
        }

        return battle;
    }
}