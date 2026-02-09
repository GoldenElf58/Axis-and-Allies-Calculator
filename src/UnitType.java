enum UnitType {
    INFANTRY    ( 3, 1, 2, 1, 1, 0, 0, true, false, false),
    TANK        ( 6, 3, 3, 2, 2, 0, 0, true, false, false),
    FIGHTER     (10, 3, 4, 3, 4, 3, 3, false, false, true),
    BOMBER      (12, 4, 1, 4, 3, 5, 0, false, false, true),
    SUBMARINE   ( 6, 2, 1, 0, 0, 1, 1, false, true, false),
    DESTROYER   ( 8, 2, 2, 0, 0, 2, 2, false, true, false),
    CARRIER     (12, 1, 2, 0, 0, 4, 4, false, true, false),
    BATTLESHIP  (16, 4, 4, 0, 0, 6, 5, false, true, false),
    TRANSPORT   ( 7, 0, 0, 0, 0, 7, 6, false, true, false);

    final int cost, attack, defense, landAtkOOL, landDefOOL, seaAtkOOL, seaDefOOL;
    final boolean landOnly, seaOnly, airUnit;

    UnitType(int c, int a, int d, int landAtkOOL, int landDefOOL, int seaAtkOOl,
             int seaDefOOL, boolean landOnly, boolean seaOnly, boolean airUnit) {
        cost = c;
        attack = a;
        defense = d;
        this.landOnly = landOnly;
        this.seaOnly = seaOnly;
        this.landAtkOOL = landAtkOOL;
        this.landDefOOL = landDefOOL;
        this.seaAtkOOL = seaAtkOOl;
        this.seaDefOOL = seaDefOOL;
        this.airUnit = airUnit;
    }
}
