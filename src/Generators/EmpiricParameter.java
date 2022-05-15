package Generators;

public class EmpiricParameter {
    private int min;
    private int max;
    private double p;
    private GeneratorDiscreteUniform generator;

    public EmpiricParameter(int _min, int _max, double _p) {
        this.min = _min;
        this.max = _max;
        this.p = _p;
        this.generator = new GeneratorDiscreteUniform(_min, _max);
    }

    //public double NextValue() {
    //    return this.generator.nextValue();
    //}

    public int NextValueInt() {
        return this.generator.nextValueInt();
    }

    public double getP() {
        return p;
    }
}
