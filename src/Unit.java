import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Unit {
    INFANTRY(1, 2, 1, 1, 0, 0, UnitType.LAND),
    TANK(3, 3, 2, 2, 0, 0, UnitType.LAND),
    FIGHTER(3, 4, 3, 4, 3, 3, UnitType.AIR),
    BOMBER(4, 1, 4, 3, 5, 0, UnitType.AIR),
    SUBMARINE(2, 1, 0, 0, 1, 1, UnitType.SEA),
    DESTROYER(2, 2, 0, 0, 2, 2, UnitType.SEA),
    CARRIER(1, 2, 0, 0, 4, 4, UnitType.SEA),
    BATTLESHIP(4, 4, 0, 0, 6, 5, UnitType.SEA);

    final int attack, defense, landAtkOOL, landDefOOL, seaAtkOOL, seaDefOOL;
    final UnitType type;
    public static final List<Unit> seaAtkOOLUnits = Stream.of(values())
            .filter(x -> x.type == UnitType.SEA || x.type == UnitType.AIR)
            .sorted(Combat.casualtyComparator(false, true))
            .collect(Collectors.toList());
    public static final List<Unit> seaDefOOLUnits = Stream.of(values())
            .filter(x -> x.type == UnitType.SEA || x == FIGHTER)
            .sorted(Combat.casualtyComparator(true, true))
            .collect(Collectors.toList());
    public static final List<Unit> landAtkOOLUnits = Stream.of(values())
            .filter(x -> x.type == UnitType.LAND || x.type == UnitType.AIR)
            .sorted(Combat.casualtyComparator(false, false))
            .collect(Collectors.toList());
    public static final List<Unit> landDefOOLUnits = Stream.of(values())
            .filter(x -> x.type == UnitType.LAND || x.type == UnitType.AIR)
            .sorted(Combat.casualtyComparator(true, false))
            .collect(Collectors.toList());

    Unit(int a, int d, int landAtkOOL, int landDefOOL, int seaAtkOOl,
         int seaDefOOL, UnitType type) {
        attack = a;
        defense = d;
        this.landAtkOOL = landAtkOOL;
        this.landDefOOL = landDefOOL;
        this.seaAtkOOL = seaAtkOOl;
        this.seaDefOOL = seaDefOOL;
        this.type = type;
    }

    int rollHits(boolean attacking, int numRolls) {
        int power = attacking ? attack : defense;
        int hits = 0;
        for (int i = 0; i < numRolls; i++)
            if (Random.nextInt(6) < power) hits++;
        return hits;
    }

    public static List<Unit> getUnitOrder(boolean defense, boolean seaBattle) {
        return seaBattle ? defense ? seaDefOOLUnits : seaAtkOOLUnits :
                defense ? landDefOOLUnits : landAtkOOLUnits;
    }
}
