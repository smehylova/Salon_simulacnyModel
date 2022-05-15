package Statistics;

public class StatDeviation {
    private double sumValues = 0;
    private double sumDoubleValues = 0;
    private int n = 0;

    public double getResult() {
        double x = 1/(double) (n - 1);
        double y1 = (x * sumDoubleValues);
        double y2 = Math.pow(x * sumValues, 2);
        double y = Math.sqrt(y1 - y2);
        return y;
    }

    public void addValue(double value) {
        sumValues += value;
        sumDoubleValues += (value * value);
        n++;
    }

    public double getSumValues() {
        return sumValues;
    }

    public void setSumValues(double sumValues) {
        this.sumValues = sumValues;
    }

    public double getSumDoubleValues() {
        return sumDoubleValues;
    }

    public void setSumDoubleValues(double sumDoubleValues) {
        this.sumDoubleValues = sumDoubleValues;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }
}
