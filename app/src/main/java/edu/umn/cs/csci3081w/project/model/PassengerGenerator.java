package edu.umn.cs.csci3081w.project.model;

import java.util.ArrayList;
import java.util.List;

/**
 * PassengerGenerator is an abstract class that generates passengers
 * based on given stops and probabilities.
 */
public abstract class PassengerGenerator {
  private List<Stop> stops;
  private List<Double> probabilities;

  /**
   * Constructor for Abstract class.
   *
   * @param stops         list of stops
   * @param probabilities list of probabilities
   */
  public PassengerGenerator(List<Stop> stops, List<Double> probabilities) {
    this.probabilities = new ArrayList<>();
    this.stops = new ArrayList<>();
    for (Stop s : stops) {
      this.stops.add(s);
    }
    for (Double probability : probabilities) {
      this.probabilities.add(probability);
    }
  }

  /**
   * Generate passengers based on the probabilities.
   *
   * @return number of passengers generated
   */
  public abstract int generatePassengers();

  /**
   * Get the list of stops.
   *
   * @return list of stops.
   */
  public List<Stop> getStops() {
    return stops;
  }

  /**
   * Get the list of probabilities.
   *
   * @return list of probabilities.
   */
  public List<Double> getProbabilities() {
    return probabilities;
  }

}