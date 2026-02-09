import java.util.List;

import static java.lang.Math.sqrt;

public class Stats {

    static double mean(List<Double> v) {
        return v.stream().mapToDouble(x -> x).average().orElse(0);
    }

    static double std(List<Double> v, double m) {
        double s = 0;
        for (double x : v) s += (x - m) * (x - m);
        return sqrt(s / (v.size() - 1));
    }

}
