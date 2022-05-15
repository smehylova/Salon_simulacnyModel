package Generators;

import java.util.ArrayList;
import java.util.Random;

public class GeneratorEmpiric {
    private ArrayList<EmpiricParameter> distributions;
    private Random generator;

    public GeneratorEmpiric(ArrayList<EmpiricParameter> _distributions) {
        this.distributions = _distributions;
        this.generator = new Random();
    }

    /*public double nextValue() {
        double u = generator.nextDouble();
        double probability = 0.0;
        for (int i = 0; i < this.distributions.size(); i++) {
            probability += this.distributions.get(i).getP();
            if (u < probability) {
                return this.distributions.get(i).NextValueInt();
            }
        }
        return -1.0;
    }*/

    public int nextValueInt() {
        double u = generator.nextDouble();
        double probability = 0.0;
        for (int i = 0; i < this.distributions.size(); i++) {
            probability += this.distributions.get(i).getP();
            if (u < probability) {
                return this.distributions.get(i).NextValueInt();
            }
        }
        return -1;
    }
}
