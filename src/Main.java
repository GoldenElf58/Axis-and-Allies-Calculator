public class Main {

    static final double WINRATE_MAX_MOE = .01;
    static final long TIME_LIMIT_MS = 5000;
    static final int MIN_SIMS = 3000;
    static final boolean DEBUG = false;
    static final int N_DEBUG = 10;

    static final Battle battle = Battle.SEA_7;

    public static void main(String[] args) {
        System.out.println(Simulator.simulate(battle, MIN_SIMS, TIME_LIMIT_MS, WINRATE_MAX_MOE,
                DEBUG, N_DEBUG));
    }
}