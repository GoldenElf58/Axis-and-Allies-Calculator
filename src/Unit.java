import java.util.stream.Stream;

public enum Unit {
    INFANTRY    ( 3, 1, 2, 1, 1, 0, 0, UnitType.LAND),
    TANK        ( 6, 3, 3, 2, 2, 0, 0, UnitType.LAND),
    FIGHTER     (10, 3, 4, 3, 4, 3, 3, UnitType.AIR),
    BOMBER      (12, 4, 1, 4, 3, 5, 0, UnitType.AIR),
    SUBMARINE   ( 6, 2, 1, 0, 0, 1, 1, UnitType.SEA),
    DESTROYER   ( 8, 2, 2, 0, 0, 2, 2, UnitType.SEA),
    CARRIER     (12, 1, 2, 0, 0, 4, 4, UnitType.SEA),
    BATTLESHIP  (16, 4, 4, 0, 0, 6, 5, UnitType.SEA);

    final byte cost, attack, defense, landAtkOOL, landDefOOL, seaAtkOOL, seaDefOOL;
    final UnitType type;
    public static final int[] seaAtkOOLUnitsInt = Stream.of(values())
            .filter(x -> x.type == UnitType.SEA || x.type == UnitType.AIR)
            .sorted(Combat.casualtyComparator(false, true))
            .mapToInt(Enum::ordinal)
            .toArray();
    public static final int[] seaDefOOLUnitsInt = Stream.of(values())
            .filter(x -> x.type == UnitType.SEA || x == FIGHTER)
            .sorted(Combat.casualtyComparator(true, true))
            .mapToInt(Enum::ordinal)
            .toArray();
    public static final int[] landAtkOOLUnitsInt = Stream.of(values())
            .filter(x -> x.type == UnitType.LAND || x.type == UnitType.AIR)
            .sorted(Combat.casualtyComparator(false, false))
            .mapToInt(Enum::ordinal)
            .toArray();
    public static final int[] landDefOOLUnitsInt = Stream.of(values())
            .filter(x -> x.type == UnitType.LAND || x.type == UnitType.AIR)
            .sorted(Combat.casualtyComparator(true, false))
            .mapToInt(Enum::ordinal)
            .toArray();
    public static final Unit[] seaAtkOOLUnits = Stream.of(values())
            .filter(x -> x.type == UnitType.SEA || x.type == UnitType.AIR)
            .sorted(Combat.casualtyComparator(false, true))
            .toArray(Unit[]::new);
    public static final Unit[] seaDefOOLUnits = Stream.of(values())
            .filter(x -> x.type == UnitType.SEA || x == FIGHTER)
            .sorted(Combat.casualtyComparator(true, true))
            .toArray(Unit[]::new);
    public static final Unit[] landAtkOOLUnits = Stream.of(values())
            .filter(x -> x.type == UnitType.LAND || x.type == UnitType.AIR)
            .sorted(Combat.casualtyComparator(false, false))
            .toArray(Unit[]::new);
    public static final Unit[] landDefOOLUnits = Stream.of(values())
            .filter(x -> x.type == UnitType.LAND || x.type == UnitType.AIR)
            .sorted(Combat.casualtyComparator(true, false))
            .toArray(Unit[]::new);

    Unit(int c, int a, int d, int landAtkOOL, int landDefOOL, int seaAtkOOl,
         int seaDefOOL, UnitType type) {
        cost = (byte) c;
        attack = (byte) a;
        defense = (byte) d;
        this.landAtkOOL = (byte) landAtkOOL;
        this.landDefOOL = (byte) landDefOOL;
        this.seaAtkOOL = (byte) seaAtkOOl;
        this.seaDefOOL = (byte) seaDefOOL;
        this.type = type;
    }

    int rollHits(boolean attacking, int numRolls) {
        byte power = attacking ? attack : defense;
        int hits = 0;
        for (int i = numRolls; i > 0; i--)
            if (Random.nextInt(6) < power) hits++;
        return hits;
    }

    public static int[] getUnitIntOrder(boolean defense, boolean seaBattle) {
        return seaBattle ? defense ? seaDefOOLUnitsInt : seaAtkOOLUnitsInt :
                defense ? landDefOOLUnitsInt : landAtkOOLUnitsInt;
    }

    public static Unit[] getUnitOrder(boolean defense, boolean seaBattle) {
        return seaBattle ? defense ? seaDefOOLUnits : seaAtkOOLUnits :
                defense ? landDefOOLUnits : landAtkOOLUnits;
    }
}
