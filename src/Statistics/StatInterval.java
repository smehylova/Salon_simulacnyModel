package Statistics;

public class StatInterval {
    private StatDeviation deviation;
    private double tAlfa;

    public StatInterval(double _tAlfa) {
        this.deviation = new StatDeviation();
        this.tAlfa = _tAlfa;
    }

    public Interval getInterval() {
        double min, max;
        double average = this.deviation.getSumValues() / this.deviation.getN();
        min = average
                - ((this.deviation.getResult() * tAlfa) / Math.sqrt(this.deviation.getN()));
        max = average
                 + ((this.deviation.getResult() * tAlfa) / Math.sqrt(this.deviation.getN()));
        return new Interval(min, max);
    }

    public void addValue(double value) {
        this.deviation.addValue(value);
    }

    public double getAverage() {
        return this.deviation.getSumValues() / this.deviation.getN();
    }
}
