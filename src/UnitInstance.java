class UnitInstance {
    Unit type;
    int hits = 0;

    UnitInstance(Unit t) {
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