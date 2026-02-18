public class Main {

    static final double WINRATE_MAX_MOE = .01;
    static final long TIME_LIMIT_MS = 5000;
    static final int MIN_SIMS = 3000;
    static final boolean DEBUG = false;
    static final int N_DEBUG = 10;
    static final boolean BENCHMARK = true;

    static final Battle battle = Battle.LAND_1;

    public static void main(String[] args) {
        if (battle.ask) battle.getBattle();
        if (BENCHMARK && !DEBUG) {
            System.out.println("Warm up...");
            Simulator.simulate(battle, MIN_SIMS * 1000, 10 * TIME_LIMIT_MS, WINRATE_MAX_MOE,
                    false, 0);
            System.out.println("Warm Up Complete");
            int n = 2500;
            long start = System.nanoTime();
            for (int i = 0; i < n; i++)
                Simulator.simulate(battle, MIN_SIMS, TIME_LIMIT_MS, WINRATE_MAX_MOE, DEBUG, N_DEBUG);
            System.out.println("Avg Time: " + (System.nanoTime() - start) / n / 1_000 + " µs");
        } else {
            System.out.println("Battle: " + battle.name() + "\n");
            System.out.println(Simulator.simulate(battle, MIN_SIMS, TIME_LIMIT_MS, WINRATE_MAX_MOE,
                    DEBUG, N_DEBUG));
        }
    }
}