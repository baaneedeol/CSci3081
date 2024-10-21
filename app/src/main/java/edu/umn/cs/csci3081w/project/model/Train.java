package edu.umn.cs.csci3081w.project.model;

import java.io.PrintStream;


/**
 * Represents a train vehicle in the transit system.
 * The train operates on a specified line and follows a route with defined stops.
 */
public class Train extends Vehicle {

  /** Constant representing the train vehicle type. */
  public static final String TRAIN_VEHICLE = "TRAIN_VEHICLE";

  /** Default speed of the train (in some unit, e.g., km/h). */
  public static final double SPEED = 1;

  /** Maximum capacity of the train. */
  public static final int CAPACITY = 120;
  private Line line;
  private double distanceRemaining;
  private Stop nextStop;

  /**
   * Constructor for a train.
   *
   * @param id       train identifier
   * @param line     Line object containing outbound and inbound routes
   * @param capacity capacity of the train
   * @param speed    speed of the train
   */
  public Train(int id, Line line, int capacity, double speed) {
    super(id, capacity, speed, new PassengerLoader(), new PassengerUnloader());
    this.line = line;
    this.distanceRemaining = 0;
    this.nextStop = line.getOutboundRoute().getNextStop();
    setName(line.getOutboundRoute().getName() + id);
    setPosition(new Position(nextStop.getPosition().getLongitude(),
            nextStop.getPosition().getLatitude()));
  }

  /**
   * Report statistics for the train.
   *
   * @param out stream for printing
   */
  public void report(PrintStream out) {
    out.println("####Train Info Start####");
    out.println("ID: " + getId());
    out.println("Name: " + getName());
    out.println("Speed: " + getSpeed());
    out.println("Capacity: " + getCapacity());
    out.println("Position: " + (getPosition().getLatitude() + "," + getPosition().getLongitude()));
    out.println("Distance to next stop: " + distanceRemaining);
    out.println("****Passengers Info Start****");
    out.println("Num of passengers: " + getPassengers().size());
    for (Passenger pass : getPassengers()) {
      pass.report(out);
    }
    out.println("****Passengers Info End****");
    out.println("####Train Info End####");
  }

  /**
   * Checks if the trip is complete, which occurs when both outbound and inbound routes have been completed.
   *
   * @return true if the trip is complete, false otherwise
   */
  public boolean isTripComplete() {
    return line.getOutboundRoute().isAtEnd() && line.getInboundRoute().isAtEnd();
  }
  /**
   * Loads a new passenger onto the train.
   *
   * @param newPassenger the passenger to be loaded
   * @return the result of the loading operation
   */
  public int loadPassenger(Passenger newPassenger) {
    return getPassengerLoader().loadPassenger(newPassenger, getCapacity(), getPassengers());
  }

  /**
   * Moves the train on its route.
   */
  public void move() {
    // update passengers FIRST
    // new passengers will get "updated" when getting on the train
    for (Passenger passenger : getPassengers()) {
      passenger.pasUpdate();
    }
    //actually move
    double speed = updateDistance();
    if (!isTripComplete() && distanceRemaining <= 0) {
      //load & unload
      int passengersHandled = handleTrainStop();
      if (passengersHandled >= 0) {
        // if we spent time unloading/loading
        // we don't get to count excess distance towards next stop
        distanceRemaining = 0;
      }
      //switch to next stop
      toNextStop();
    }

    // Get the correct route and early exit
    Route currentRoute = line.getOutboundRoute(); // Update to use line
    if (line.getOutboundRoute().isAtEnd()) {
      if (line.getInboundRoute().isAtEnd()) {
        return;
      }
      currentRoute = line.getInboundRoute();
    }

    Stop prevStop = currentRoute.prevStop();
    Stop nextStop = currentRoute.getNextStop();
    double distanceBetween = currentRoute.getNextStopDistance();
    // the ratio shows us how far from the previous stop are we in a ratio from 0 to 1
    double ratio;
    // check if we are at the first stop
    if (distanceBetween - 0.00001 < 0) {
      ratio = 1;
    } else {
      ratio = distanceRemaining / distanceBetween;
      if (ratio < 0) {
        ratio = 0;
        distanceRemaining = 0;
      }
    }
    double newLongitude = nextStop.getPosition().getLongitude() * (1 - ratio)
        + prevStop.getPosition().getLongitude() * ratio;
    double newLatitude = nextStop.getPosition().getLatitude() * (1 - ratio)
        + prevStop.getPosition().getLatitude() * ratio;
    setPosition(new Position(newLongitude, newLatitude));
  }

  /**
   * Update the simulation state for this train.
   */
  public void update() {
    move();
  }

  /**
   * Unloads passengers at the next stop.
   *
   * @return the number of passengers unloaded
   */
  private int unloadPassengers() {
    return getPassengerUnloader().unloadPassengers(getPassengers(), nextStop);
  }

  /**
   * Handles the arrival of the train at a stop, managing passenger unloading and loading.
   *
   * @return the total number of passengers handled
   */
  private int handleTrainStop() {
    // This function handles arrival at a train stop
    int passengersHandled = 0;
    // unloading passengers
    passengersHandled += unloadPassengers();
    // loading passengers
    passengersHandled += nextStop.loadPassengers(this);
    // if passengers were unloaded or loaded, it means we made
    // a stop to do the unload/load operation. In this case, the
    // distance remaining to the stop is 0 because we are at the stop.
    // If no unload/load operation was made and the distance is negative,
    // this means that we did not stop and keep going further.
    if (passengersHandled != 0) {
      distanceRemaining = 0;
    }
    return passengersHandled;
  }

  /**
   * Advances the train to the next stop on its route.
   */
  private void toNextStop() {
    //current stop
    currentRoute().nextStop();
    if (!isTripComplete()) {
      // it's important we call currentRoute() again,
      // as nextStop() may have caused it to change.
      nextStop = currentRoute().getNextStop();
      distanceRemaining +=
          currentRoute().getNextStopDistance();
      // note, if distanceRemaining was negative because we
      // had extra time left over, that extra time is
      // effectively counted towards the next stop
    } else {
      nextStop = null;
      distanceRemaining = 999;
    }
  }

  /**
   * Updates the distance remaining for the train and returns the effective speed.
   *
   * @return the effective speed of the train
   */
  private double updateDistance() {
    // Updates the distance remaining and returns the effective speed of the train
    // Train does not move if speed is negative or train is at end of route
    if (isTripComplete()) {
      return 0;
    }
    if (getSpeed() < 0) {
      return 0;
    }
    distanceRemaining -= getSpeed();
    return getSpeed();
  }

  /**
   * Determines the current route of the train (outbound or inbound).
   *
   * @return the current route
   */
  private Route currentRoute() {
    // Figure out if we're on the outgoing or incoming route
    if (!line.getOutboundRoute().isAtEnd()) {
      return line.getOutboundRoute();
    }
    return line.getInboundRoute();
  }
  /**
   * Gets the next stop the train will approach.
   *
   * @return the next stop
   */
  public Stop getNextStop() {
    return nextStop;
  }

}
