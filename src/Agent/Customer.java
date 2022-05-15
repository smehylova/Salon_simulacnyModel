package Agent;

public class Customer {
    private int id;
    private String status;

    private boolean hair = false;
    private boolean clean = false;
    private boolean consmetic = false;

    //statistiky
    private double startTime;
    private double startTimeQueue;

    public Customer(int id, String status, double _startTime, double _startTimeQueue) {
        this.id = id;
        this.status = status;
        this.startTime = _startTime;
        this.startTimeQueue = _startTimeQueue;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public double getStartTimeQueue() {
        return startTimeQueue;
    }

    public void setStartTimeQueue(double startTimeQueue) {
        this.startTimeQueue = startTimeQueue;
    }

    public boolean isHair() {
        return hair;
    }

    public void setHair(boolean hair) {
        this.hair = hair;
    }

    public boolean isClean() {
        return clean;
    }

    public void setClean(boolean clean) {
        this.clean = clean;
    }

    public boolean isConsmetic() {
        return consmetic;
    }

    public void setConsmetic(boolean consmetic) {
        this.consmetic = consmetic;
    }
}
