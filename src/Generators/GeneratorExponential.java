package Generators;

import java.util.Random;

public class GeneratorExponential {
    private Random generator;
    private double lambda;

    public GeneratorExponential(double _lambda) {
        lambda = _lambda;
        generator = new Random();
    }

    public double nextValue() {
        return -Math.log(generator.nextDouble())/lambda;
    }

    /*public int nextValueInt() {
        return (int)Math.round(nextValue());
    }*/
}
