package Statistics;

public class StatAverage {
    private double values;
    private int counts;

    public StatAverage() {
        values = 0;
        counts = 0;
    }

    public void addStatistic(double value) {
        values += value;
        counts++;
    }

    public void addStatistic(double value, int count) {
        values += value;
        counts += count;
    }

    public double getResult() {
        return counts == 0 ? 0.0 : values / (double) counts;
    }
}
