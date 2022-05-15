package Simulation;

import Agent.Cosmetitian;
import Agent.Customer;
import Agent.HairStylist;
import Agent.Receptionist;
import Event.SimEvent;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public abstract class SimCore {
    private int countReplications = -1;
    private int actualReplication = -1;
    private int pause;
    private int jumpRepl = 1;

    protected ISimData guiListener;
    private boolean isRunning = false;
    private boolean isPaused = false;

    private PriorityQueue<SimEvent> calendar;
    private double actualTime;
    private double maxTime;

    private boolean isSpeeded;
    private int speed;
    private int speedTime;

    public void simulate(int replications) {
        isRunning = true;
        pause = (int)(0.3 * replications);
        if ((replications - pause) / 1000 != 0) {
            jumpRepl = (replications - pause) / 1000;
        } else {
            jumpRepl = 1;
        }
        countReplications = replications;
        beforeReplications();
        for (actualReplication = 0; actualReplication < replications; actualReplication++) {
            guiListener.newSimulation();
            beforeReplication();

            while (calendar.size() > 0) {
                while (isPaused && isRunning) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (!isRunning) {
                    break;
                }
                SimEvent event = calendar.poll();
                actualTime = event.getTime();
                event.execute();
                //guiListener.refresh(this);
            }
            if (!isRunning) {
                break;
            }

            afterReplication();
            guiListener.refresh(this);
        }
        afterReplications();
        isRunning = false;
        guiListener.refresh(this);

    }

    protected abstract void beforeReplications();
    protected abstract void beforeReplication();
    protected abstract void afterReplication();
    protected abstract void afterReplications();

    public void stopSimulation() {
        isRunning = false;
    }

    public void pauseSimulation() {
        isPaused = true;
    }

    public void continueSimulation() {
        isPaused = false;
    }

    public void addGuiListener(ISimData _guiListener) {
        guiListener = _guiListener;
    }

    public ISimData getGuiListener() {
        return guiListener;
    }

    public int getCountReplications() {
        return countReplications;
    }

    public void setCountReplications(int countReplications) {
        this.countReplications = countReplications;
    }

    public int getActualReplication() {
        return actualReplication;
    }

    public void setActualReplication(int actualReplication) {
        this.actualReplication = actualReplication;
    }

    public int getPause() {
        return pause;
    }

    public void setPause(int pause) {
        this.pause = pause;
    }

    public int getJumpRepl() {
        return jumpRepl;
    }

    public void setJumpRepl(int jumpRepl) {
        this.jumpRepl = jumpRepl;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public PriorityQueue<SimEvent> getCalendar() {
        return calendar;
    }

    public void setCalendar(PriorityQueue<SimEvent> calendar) {
        this.calendar = calendar;
    }

    public double getActualTime() {
        return actualTime;
    }

    public void setActualTime(double actualTime) {
        this.actualTime = actualTime;
    }

    public double getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(double maxTime) {
        this.maxTime = maxTime;
    }

    public boolean isSpeeded() {
        return isSpeeded;
    }

    public void setSpeeded(boolean speeded) {
        isSpeeded = speeded;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getSpeedTime() {
        return speedTime;
    }

    public void setSpeedTime(int speedTime) {
        this.speedTime = speedTime;
    }
}
