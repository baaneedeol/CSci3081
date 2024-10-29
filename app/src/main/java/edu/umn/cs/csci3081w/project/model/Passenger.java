package edu.umn.cs.csci3081w.project.model;

import java.io.PrintStream;

/**
 * Represents a passenger in the transit system.
 */
public class Passenger {

  private String name;
  private int destinationStopId;
  private int waitAtStop;
  private int timeOnVehicle;

  /**
   * Constructor for passenger.
   *
   * @param name              name of passenger
   * @param destinationStopId destination stop id
   */
  public Passenger(int destinationStopId, String name) {
    this.name = name;
    this.destinationStopId = destinationStopId;
    this.waitAtStop = 0;
    this.timeOnVehicle = 0;
  }

  /**
   * Updates time variables for passenger.
   */
  public void pasUpdate() {
    if (isOnVehicle()) {
      timeOnVehicle++;
    } else {
      waitAtStop++;
    }
  }

  /**
   * Set passenger on vehicle.
   */
  public void setOnVehicle() {
    timeOnVehicle = 1;
  }

  /**
   * Check if passenger is on vehicle.
   *
   * @return true if passenger is on vehicle, false otherwise
   */
  public boolean isOnVehicle() {
    return timeOnVehicle > 0;
  }

  /**
   * Get destination stop id.
   *
   * @return destination stop id
   */
  public int getDestination() {
    return destinationStopId;
  }

  /**
   * Report statistics for passenger.
   *
   * @param out stream for printing
   */
  public void report(PrintStream out) {
    out.println("####Passenger Info Start####");
    out.println("Name: " + name);
    out.println("Destination: " + destinationStopId);
    out.println("Wait at stop: " + waitAtStop);
    out.println("Time on vehicle: " + timeOnVehicle);
    out.println("####Passenger Info End####");
  }

}
