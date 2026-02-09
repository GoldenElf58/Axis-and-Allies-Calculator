class Unit {
    UnitType type;
    int hits = 0;

    Unit(UnitType t) {
        type = t;
    }

    boolean isAlive() {
        return hits < 2;
    }

    @Override
    public String toString() {
        return "Unit(" + type + ", " + hits + ")";
    }
}