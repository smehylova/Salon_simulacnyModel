package Generators;

import java.util.Random;

public class GeneratorDiscreteUniform {
    public Random generator;
    public int min;
    public int max;

    public GeneratorDiscreteUniform(int _min, int _max) {
        generator = new Random();
        min = _min;
        max = _max;
    }

    //public double nextValue() {
    //    return generator.nextDouble() * ((max - min + 1)) + min;
    //}

    public int nextValueInt() {
        return generator.nextInt((max - min + 1)) + min;
    }
}
