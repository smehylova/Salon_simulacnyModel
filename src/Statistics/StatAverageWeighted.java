package Statistics;

public class StatAverageWeighted {
    private double sumValues;
    private double sumTime;
    private double newValue;

    public StatAverageWeighted() {
        this.sumValues = 0.0;
        this.sumTime = 0;
        this.newValue = 0.0;
    }

    public void addValue(double value, double time, double newValue) {
        this.sumValues += value;
        this.sumTime += time;
        this.newValue = newValue;

    }

    public double getResult() {
        return this.sumValues / this.sumTime;
    }

    public double getResultTime(double time) {
        return (this.sumValues + this.newValue /** (time - this.sumTime)*/) / time;
    }
}
