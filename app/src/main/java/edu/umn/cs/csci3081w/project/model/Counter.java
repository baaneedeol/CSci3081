package edu.umn.cs.csci3081w.project.model;

/**
 * Counter class to generate unique IDs for routes, stops, buses, and trains.
 */
public class Counter {

  /** The route ID counter. */
  public int routeIdCounter = 10;
  /** The stop ID counter. */
  public int stopIdCounter = 100;
  /** The bus ID counter. */
  public int busIdCounter = 1000;
  /** The train ID counter. */
  public int trainIdCounter = 2000;

  /**
   * Default constructor for Counter.
   */
  public Counter() {

  }

  /**
   * Get the next route ID and increment the counter.
   *
   * @return the next route ID.
   */
  public int getRouteIdCounterAndIncrement() {
    return routeIdCounter++;
  }

  /**
   * Get the next stop ID and increment the counter.
   *
   * @return the next stop ID.
   */
  public int getStopIdCounterAndIncrement() {
    return stopIdCounter++;
  }

  /**
   * Get the next bus ID and increment the counter.
   *
   * @return the next bus ID.
   */
  public int getBusIdCounterAndIncrement() {
    return busIdCounter++;
  }

  /**
   * Get the next train ID and increment the counter.
   *
   * @return the next train ID.
   */
  public int getTrainIdCounterAndIncrement() {
    return trainIdCounter++;
  }

}
