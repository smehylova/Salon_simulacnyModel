package Event;

import Simulation.SimCore;

public abstract class SimEvent implements Comparable<SimEvent> {
    private double time;
    private SimCore simCore;

    public abstract void execute();

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public SimCore getSimCore() {
        return simCore;
    }

    public void setSimCore(SimCore simCore) {
        this.simCore = simCore;
    }

    @Override
    public int compareTo(SimEvent o) {
        if (this.time > o.getTime()) {
            return 1;
        } else if (this.time < o.getTime()) {
            return -1;
        } else {
            return 0;
        }
    }
}
