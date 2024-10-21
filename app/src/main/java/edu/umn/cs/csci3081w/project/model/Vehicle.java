package edu.umn.cs.csci3081w.project.model;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class representing a vehicle in the transit system.
 * Vehicles have an identifier, capacity, speed, and can load and unload passengers.
 */
public abstract class Vehicle {
  private int id;
  private int capacity;
  //the speed is in distance over a time unit
  private double speed;
  private PassengerLoader loader;
  private PassengerUnloader unloader;
  private List<Passenger> passengers;
  private String name;
  private Position position;


  /**
   * Constructor for a vehicle.
   *
   * @param id       vehicle identifier
   * @param capacity vehicle capacity
   * @param speed    vehicle speed
   * @param loader   passenger loader for vehicle
   * @param unloader passenger unloader for vehicle
   */
  public Vehicle(int id, int capacity, double speed, PassengerLoader loader,
                 PassengerUnloader unloader) {
    this.id = id;
    this.capacity = capacity;
    this.speed = speed;
    this.loader = loader;
    this.unloader = unloader;
    this.passengers = new ArrayList<Passenger>();
  }

  /**
   * Reports the vehicle's information.
   *
   * @param out the stream for printing the report
   */
  public abstract void report(PrintStream out);

  /**
   * Checks if the trip is complete.
   *
   * @return true if the trip is complete, false otherwise
   */
  public abstract boolean isTripComplete();

  /**
   * Loads a new passenger onto the vehicle.
   *
   * @param newPassenger the passenger to be loaded
   * @return the number of passengers successfully loaded
   */
  public abstract int loadPassenger(Passenger newPassenger);

  /**
   * Moves the vehicle along its route.
   */
  public abstract void move();

  /**
   * Updates the state of the vehicle for the current simulation step.
   */
  public abstract void update();

  /**
   * Calculates the CO2 consumption of the vehicle.
   *
   * @return the CO2 consumption
   */
  public abstract int co2Consumption();

  /**
   * Gets the vehicle's identifier.
   *
   * @return the vehicle identifier
   */
  public int getId() {
    return id;
  }

  /**
   * Gets the vehicle's capacity.
   *
   * @return the vehicle capacity
   */
  public int getCapacity() {
    return capacity;
  }

  /**
   * Gets the vehicle's speed.
   *
   * @return the vehicle speed
   */
  public double getSpeed() {
    return speed;
  }

  /**
   * Gets the passenger loader for the vehicle.
   *
   * @return the passenger loader
   */
  public PassengerLoader getPassengerLoader() {
    return loader;
  }

  /**
   * Gets the passenger unloader for the vehicle.
   *
   * @return the passenger unloader
   */
  public PassengerUnloader getPassengerUnloader() {
    return unloader;
  }

  /**
   * Gets the list of passengers currently on the vehicle.
   *
   * @return the list of passengers
   */
  public List<Passenger> getPassengers() {
    return passengers;
  }

  /**
   * Gets the name of the vehicle.
   *
   * @return the vehicle name
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name of the vehicle.
   *
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Gets the current position of the vehicle.
   *
   * @return the vehicle's position
   */
  public Position getPosition() {
    return position;
  }

  /**
   * Sets the position of the vehicle.
   *
   * @param position the position to set
   */
  public void setPosition(Position position) {
    this.position = position;
  }
}
