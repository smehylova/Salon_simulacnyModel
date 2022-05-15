package Generators;

import java.util.Random;

public class GeneratorTriangular {
    private Random generator;
    private double min;
    private double max;
    private double modus;

    public GeneratorTriangular(double _min, double _max, double _modus) {
        min = _min;
        max = _max;
        modus = _modus;
        generator = new Random();
    }

    public double nextValue() {
        double fc = (modus-min)/(max-min);
        double u = generator.nextDouble();
        if (u < fc) {
            return min + Math.sqrt(u * (max-min) * (modus-min));
        } else {
            return max - Math.sqrt((1-u) * (max-min) * (max-modus));
        }
    }

    //public int nextValueInt() {
    //    return (int)Math.round(nextValue());
    //}
}
