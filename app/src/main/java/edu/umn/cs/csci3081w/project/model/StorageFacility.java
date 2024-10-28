package edu.umn.cs.csci3081w.project.model;

public class StorageFacility {
    private int busesNum;
    private int trainsNum;

    public StorageFacility(int busesNum, int trainsNum) {
        this.busesNum = busesNum;
        this.trainsNum = trainsNum;
    }

    public int getBusesNum() {
        return busesNum;
    }

    public void setBusesNum(int busesNum) {
        this.busesNum = busesNum;
    }

    public int getTrainsNum() {
        return trainsNum;
    }

    public void setTrainsNum(int trainsNum) {
        this.trainsNum = trainsNum;
    }

    @Override
    public String toString() {
        return "StorageFacility{" +
                "busesNum=" + busesNum +
                ", trainsNum=" + trainsNum +
                '}';
    }

    public synchronized boolean createBus() {
        if (busesNum > 0) {
            busesNum--;
            return true;  
        }
        return false;  
    }

    public synchronized boolean createTrain() {
        if (trainsNum > 0) {
            trainsNum--;
            return true;  
        }
        return false;  
    }

    public synchronized void terminateBus() {
        busesNum++;
    }

    public synchronized void terminateTrain() {
        trainsNum++;
    }

}
