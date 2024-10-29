package edu.umn.cs.csci3081w.project.model;

/**
 * Represents a storage facility for buses and trains.
 * Tracks the number of available buses and trains.
 */
public class StorageFacility {
  private int busesNum;
  private int trainsNum;

  /**
   * Constructor for StorageFacility.
   *
   * @param busesNum the number of buses available.
   * @param trainsNum the number of trains available.
   */
  public StorageFacility(int busesNum, int trainsNum) {
    this.busesNum = busesNum;
    this.trainsNum = trainsNum;
  }

  /**
   * Get the number of buses available in the storage facility.
   *
   * @return the number of buses.
   */
  public int getBusesNum() {
    return busesNum;
  }

  /**
   * Set the number of buses available in the storage facility.
   *
   * @param busesNum the number of buses to set.
   */
  public void setBusesNum(int busesNum) {
    this.busesNum = busesNum;
  }

  /**
   * Get the number of trains available in the storage facility.
   *
   * @return the number of trains.
   */
  public int getTrainsNum() {
    return trainsNum;
  }

  /**
   * Set the number of trains available in the storage facility.
   *
   * @param trainsNum the number of trains to set.
   */
  public void setTrainsNum(int trainsNum) {
    this.trainsNum = trainsNum;
  }

  @Override
  public String toString() {
    return "StorageFacility{"
        + "busesNum=" + busesNum
        + ", trainsNum=" + trainsNum
        + '}';
  }

  /**
   * Create a bus if available.
   *
   * @return true if a bus was created, false otherwise.
   */
  public synchronized boolean createBus() {
    if (busesNum > 0) {
      busesNum--;
      return true;
    }
    return false;
  }

  /**
   * Create a train if available.
   *
   * @return true if a train was created, false otherwise.
   */
  public synchronized boolean createTrain() {
    if (trainsNum > 0) {
      trainsNum--;
      return true;
    }
    return false;
  }

  /**
   * Terminate a bus and increase the available bus count.
   */
  public synchronized void terminateBus() {
    busesNum++;
  }

  /**
   * Terminate a train and increase the available train count.
   */
  public synchronized void terminateTrain() {
    trainsNum++;
  }

}
