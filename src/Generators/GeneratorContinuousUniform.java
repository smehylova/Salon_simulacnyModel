package Generators;

import java.util.Random;

public class GeneratorContinuousUniform {
    public Random generator;
    public double min;
    public double max;

    public GeneratorContinuousUniform(double _min, double _max) {
        generator = new Random();
        min = _min;
        max = _max;
    }

    public double nextValue() {
        return generator.nextDouble() * ((max - min)) + min;
    }

    //public int nextValueInt() {
    //    return generator.nextInt((int) (max - min)) + (int)min;
    //}
}
