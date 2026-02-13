public enum Unit {
    INFANTRY    (1, 2, 1, 1, 0, 0, UnitType.LAND),
    TANK        (3, 3, 2, 2, 0, 0, UnitType.LAND),
    FIGHTER     (3, 4, 3, 4, 3, 3, UnitType.AIR),
    BOMBER      (4, 1, 4, 3, 5, 0, UnitType.AIR),
    SUBMARINE   (2, 1, 0, 0, 1, 1, UnitType.SEA),
    DESTROYER   (2, 2, 0, 0, 2, 2, UnitType.SEA),
    CARRIER     (1, 2, 0, 0, 4, 4, UnitType.SEA),
    BATTLESHIP  (4, 4, 0, 0, 6, 5, UnitType.SEA);

    final int attack, defense, landAtkOOL, landDefOOL, seaAtkOOL, seaDefOOL;
    final UnitType type;

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
}
