package Agent;

public class HairStylist {
    private boolean isOccupied = false;
    private double work = 0;

    private double startWork = -1;

    private int id;

    public HairStylist(int _id) {
        this.id = _id;
    }

    public void addWord(double _work) {
        this.work += _work;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }

    public double getWork() {
        return work;
    }

    public double getStartWork() {
        return startWork;
    }

    public void setStartWork(double startWork) {
        this.startWork = startWork;
    }

    public void setWork(int work) {
        this.work = work;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
