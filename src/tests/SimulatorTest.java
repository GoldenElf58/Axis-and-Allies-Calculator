import org.junit.Test;

import static java.lang.Math.abs;
import static org.junit.Assert.assertTrue;

public class SimulatorTest {
    
    public final static double MOE_MULTIPLIER = 1.5;
    public final static double DEFAULT_MAX_MOE = .002;
    public final static double EPSILON = .00001;

    @Test
    public void land1Battle() {
        double expectedAttackerSurviveRate = .718;
        double expectedDefenderSurviveRate = .245;
        double maxMOE = DEFAULT_MAX_MOE;
        Simulator.SimulationResult r = Simulator.simulate(Battle.LAND_1, 500, 5000, maxMOE, false,
                10);
        assertTrue("Attacker survive rate " + r.attackerSurviveRate() + " not close enough to " + expectedAttackerSurviveRate,
                abs(r.attackerSurviveRate() - expectedAttackerSurviveRate) < maxMOE * MOE_MULTIPLIER);
        assertTrue("Attacker survive rate " + r.defenderSurviveRate() + " not close enough to " + expectedDefenderSurviveRate,
                abs(r.defenderSurviveRate() - expectedDefenderSurviveRate) < maxMOE * MOE_MULTIPLIER);
        assertTrue("Attacker survive MOE " + r.attackerSurviveMOE() + " over " + maxMOE,
                r.attackerSurviveMOE() - EPSILON <= maxMOE);
    }

    @Test
    public void sea1Battle() {
        double expectedAttackerSurviveRate = .760;
        double expectedDefenderSurviveRate = .186;
        double maxMOE = DEFAULT_MAX_MOE;
        Simulator.SimulationResult r = Simulator.simulate(Battle.SEA_1, 500, 5000, maxMOE, false, 10);
        assertTrue("Attacker survive rate " + r.attackerSurviveRate() + " not close enough to " + expectedAttackerSurviveRate,
                abs(r.attackerSurviveRate() - expectedAttackerSurviveRate) < maxMOE * MOE_MULTIPLIER);
        assertTrue("Defender survive rate " + r.defenderSurviveRate() + " not close enough to " + expectedDefenderSurviveRate,
                abs(r.defenderSurviveRate() - expectedDefenderSurviveRate) < maxMOE * MOE_MULTIPLIER);
        assertTrue("Attacker survive MOE " + r.attackerSurviveMOE() + " over " + maxMOE,
                r.attackerSurviveMOE() - EPSILON <= maxMOE);
    }

    @Test
    public void sea2Battle() {
        double expectedAttackerSurviveRate = .801;
        double expectedDefenderSurviveRate = .154;
        double maxMOE = DEFAULT_MAX_MOE;
        Simulator.SimulationResult r = Simulator.simulate(Battle.SEA_2, 500, 5000, maxMOE, false, 10);
        assertTrue("Attacker survive rate " + r.attackerSurviveRate() + " not close enough to " + expectedAttackerSurviveRate,
                abs(r.attackerSurviveRate() - expectedAttackerSurviveRate) < maxMOE * MOE_MULTIPLIER);
        assertTrue("Defender survive rate " + r.defenderSurviveRate() + " not close enough to " + expectedDefenderSurviveRate,
                abs(r.defenderSurviveRate() - expectedDefenderSurviveRate) < maxMOE * MOE_MULTIPLIER);
        assertTrue("Attacker survive MOE " + r.attackerSurviveMOE() + " over " + maxMOE,
                r.attackerSurviveMOE() - EPSILON <= maxMOE);
    }

    @Test
    public void sea3Battle() {
        double expectedAttackerSurviveRate = .894;
        double expectedDefenderSurviveRate = .070;
        double maxMOE = DEFAULT_MAX_MOE;
        Simulator.SimulationResult r = Simulator.simulate(Battle.SEA_3, 500, 5000, maxMOE, false, 10);
        assertTrue("Attacker survive rate " + r.attackerSurviveRate() + " not close enough to " + expectedAttackerSurviveRate,
                abs(r.attackerSurviveRate() - expectedAttackerSurviveRate) < maxMOE * MOE_MULTIPLIER);
        assertTrue("Defender survive rate " + r.defenderSurviveRate() + " not close enough to " + expectedDefenderSurviveRate,
                abs(r.defenderSurviveRate() - expectedDefenderSurviveRate) < maxMOE * MOE_MULTIPLIER);
        assertTrue("Attacker survive MOE " + r.attackerSurviveMOE() + " over " + maxMOE,
                r.attackerSurviveMOE() - EPSILON <= maxMOE);
    }

    @Test
    public void sea4Battle() {
        double expectedAttackerSurviveRate = .403;
        double expectedDefenderSurviveRate = .541;
        double maxMOE = DEFAULT_MAX_MOE;
        Simulator.SimulationResult r = Simulator.simulate(Battle.SEA_4, 500, 5000, maxMOE, false, 10);
        assertTrue("Attacker survive rate " + r.attackerSurviveRate() + " not close enough to " + expectedAttackerSurviveRate,
                abs(r.attackerSurviveRate() - expectedAttackerSurviveRate) < maxMOE * MOE_MULTIPLIER);
        assertTrue("Defender survive rate " + r.defenderSurviveRate() + " not close enough to " + expectedDefenderSurviveRate,
                abs(r.defenderSurviveRate() - expectedDefenderSurviveRate) < maxMOE * MOE_MULTIPLIER);
        assertTrue("Attacker survive MOE " + r.attackerSurviveMOE() + " over " + maxMOE,
                r.attackerSurviveMOE() - EPSILON <= maxMOE);
    }

    @Test
    public void sea5Battle() {
        double expectedAttackerSurviveRate = .528;
        double expectedDefenderSurviveRate = .437;
        double maxMOE = DEFAULT_MAX_MOE;
        Simulator.SimulationResult r = Simulator.simulate(Battle.SEA_5, 500, 5000, maxMOE, false, 10);
        assertTrue("Attacker survive rate " + r.attackerSurviveRate() + " not close enough to " + expectedAttackerSurviveRate,
                abs(r.attackerSurviveRate() - expectedAttackerSurviveRate) < maxMOE * MOE_MULTIPLIER);
        assertTrue("Defender survive rate " + r.defenderSurviveRate() + " not close enough to " + expectedDefenderSurviveRate,
                abs(r.defenderSurviveRate() - expectedDefenderSurviveRate) < maxMOE * MOE_MULTIPLIER);
        assertTrue("Attacker survive MOE " + r.attackerSurviveMOE() + " over " + maxMOE,
                r.attackerSurviveMOE() - EPSILON <= maxMOE);
    }

    @Test
    public void sea6Battle() {
        double expectedAttackerSurviveRate = .220;
        double expectedDefenderSurviveRate = .702;
        double maxMOE = DEFAULT_MAX_MOE;
        Simulator.SimulationResult r = Simulator.simulate(Battle.SEA_6, 500, 5000, maxMOE, false, 10);
        assertTrue("Attacker survive rate " + r.attackerSurviveRate() + " not close enough to " + expectedAttackerSurviveRate,
                abs(r.attackerSurviveRate() - expectedAttackerSurviveRate) < maxMOE * MOE_MULTIPLIER);
        assertTrue("Defender survive rate " + r.defenderSurviveRate() + " not close enough to " + expectedDefenderSurviveRate,
                abs(r.defenderSurviveRate() - expectedDefenderSurviveRate) < maxMOE * MOE_MULTIPLIER);
        assertTrue("Attacker survive MOE " + r.attackerSurviveMOE() + " over " + maxMOE,
                r.attackerSurviveMOE() - EPSILON <= maxMOE);
    }

    @Test
    public void sea7Battle() {
        double expectedAttackerSurviveRate = .493;
        double expectedDefenderSurviveRate = .466;
        double maxMOE = DEFAULT_MAX_MOE;
        Simulator.SimulationResult r = Simulator.simulate(Battle.SEA_7, 500, 5000, maxMOE, false, 10);
        assertTrue("Attacker survive rate " + r.attackerSurviveRate() + " not close enough to " + expectedAttackerSurviveRate,
                abs(r.attackerSurviveRate() - expectedAttackerSurviveRate) < maxMOE * MOE_MULTIPLIER);
        assertTrue("Defender survive rate " + r.defenderSurviveRate() + " not close enough to " + expectedDefenderSurviveRate,
                abs(r.defenderSurviveRate() - expectedDefenderSurviveRate) < maxMOE * MOE_MULTIPLIER);
        assertTrue("Attacker survive MOE " + r.attackerSurviveMOE() + " over " + maxMOE,
                r.attackerSurviveMOE() - EPSILON <= maxMOE);
    }

    @Test
    public void seaLongBattle() {
        double expectedAttackerSurviveRate = .509;
        double expectedDefenderSurviveRate = .453;
        double maxMOE = DEFAULT_MAX_MOE;
        Simulator.SimulationResult r = Simulator.simulate(Battle.SEA_LONG, 500, 5000, maxMOE, false,
                10);
        assertTrue("Attacker survive rate " + r.attackerSurviveRate() + " not close enough to " + expectedAttackerSurviveRate,
                abs(r.attackerSurviveRate() - expectedAttackerSurviveRate) < maxMOE * MOE_MULTIPLIER);
        assertTrue("Defender survive rate " + r.defenderSurviveRate() + " not close enough to " + expectedDefenderSurviveRate,
                abs(r.defenderSurviveRate() - expectedDefenderSurviveRate) < maxMOE * MOE_MULTIPLIER);
        assertTrue("Attacker survive MOE " + r.attackerSurviveMOE() + " over " + maxMOE,
                r.attackerSurviveMOE() - EPSILON <= maxMOE);
    }
}