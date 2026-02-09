enum Unit {
    INFANTRY    ( 3, 1, 2, 1, 1, 0, 0, UnitType.LAND),
    TANK        ( 6, 3, 3, 2, 2, 0, 0, UnitType.LAND),
    FIGHTER     (10, 3, 4, 3, 4, 3, 3, UnitType.AIR),
    BOMBER      (12, 4, 1, 4, 3, 5, 0, UnitType.AIR),
    SUBMARINE   ( 6, 2, 1, 0, 0, 1, 1, UnitType.SEA),
    DESTROYER   ( 8, 2, 2, 0, 0, 2, 2, UnitType.SEA),
    CARRIER     (12, 1, 2, 0, 0, 4, 4, UnitType.SEA),
    BATTLESHIP  (16, 4, 4, 0, 0, 6, 5, UnitType.SEA),
    TRANSPORT   ( 7, 0, 0, 0, 0, 7, 6, UnitType.SEA);

    final int cost, attack, defense, landAtkOOL, landDefOOL, seaAtkOOL, seaDefOOL;
    final UnitType type;

    Unit(int c, int a, int d, int landAtkOOL, int landDefOOL, int seaAtkOOl,
         int seaDefOOL, UnitType type) {
        cost = c;
        attack = a;
        defense = d;
        this.landAtkOOL = landAtkOOL;
        this.landDefOOL = landDefOOL;
        this.seaAtkOOL = seaAtkOOl;
        this.seaDefOOL = seaDefOOL;
        this.type = type;
    }
}
