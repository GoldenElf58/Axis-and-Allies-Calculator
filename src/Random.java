public class Random {
    private static int state = 123456789; // must not be 0

    public static int nextInt() {
        int x = state;
        x ^= (x << 13);
        x ^= (x >>> 17);
        x ^= (x << 5);
        state = x;
        return x;
    }

    public static int nextInt(int n) {
        return (int)(((long)(nextInt() >>> 1) * n) >>> 31);
    }
}
