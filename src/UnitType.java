enum UnitType {
    INFANTRY    ( 3, 1, 2, 1, 1, 0, 0, true, false),
    TANK        ( 6, 3, 3, 2, 2, 0, 0, true, false),
    FIGHTER     (10, 3, 4, 3, 4, 3, 3, false, false),
    BOMBER      (12, 4, 1, 4, 3, 5, 0, true, false),
    SUBMARINE   ( 6, 2, 1, 0, 0, 1, 1, false, true),
    DESTROYER   ( 8, 2, 2, 0, 0, 2, 2, false, true),
    CARRIER     (12, 1, 2, 0, 0, 4, 4, false, true),
    BATTLESHIP  (16, 4, 4, 0, 0, 6, 5, false, true),
    TRANSPORT   ( 7, 0, 0, 0, 0, 7, 6, false, true);

    final int cost, attack, defense, landAtkOOL, landDefOOL, seaAtkOOL, seaDefOOL;
    final boolean landOnly, seaOnly;

    UnitType(int c, int a, int d, int landAtkOOL, int landDefOOL, int seaAtkOOl,
             int seaDefOOL, boolean landOnly, boolean seaOnly) {
        cost = c;
        attack = a;
        defense = d;
        this.landOnly = landOnly;
        this.seaOnly = seaOnly;
        this.landAtkOOL = landAtkOOL;
        this.landDefOOL = landDefOOL;
        this.seaAtkOOL = seaAtkOOl;
        this.seaDefOOL = seaDefOOL;
    }
}
