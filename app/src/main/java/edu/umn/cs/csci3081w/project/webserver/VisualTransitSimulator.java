package edu.umn.cs.csci3081w.project.webserver;

import edu.umn.cs.csci3081w.project.model.Bus;
import edu.umn.cs.csci3081w.project.model.Counter;
import edu.umn.cs.csci3081w.project.model.Line;
import edu.umn.cs.csci3081w.project.model.Route;
import edu.umn.cs.csci3081w.project.model.Train;
import edu.umn.cs.csci3081w.project.model.Vehicle;
import edu.umn.cs.csci3081w.project.model.StorageFacility;
import java.util.ArrayList;
import java.util.List;

public class VisualTransitSimulator {

  private static boolean LOGGING = false;
  private int numTimeSteps = 0;
  private int simulationTimeElapsed = 0;
  private Counter counter;
  private List<Route> routes;
  private List<Vehicle> activeVehicles;
  private List<Vehicle> completedTripVehicles;
  private List<Integer> vehicleStartTimings;
  private List<Integer> timeSinceLastVehicle;
  private StorageFacility storageFacility;
  private boolean paused = false;

  /**
   * Constructor for Simulation.
   *
   * @param configFile file containing the simulation configuration
   */
  public VisualTransitSimulator(String configFile) {
    this.counter = new Counter();
    ConfigManager configManager = new ConfigManager();
    configManager.readConfig(counter, configFile);
    this.routes = configManager.getRoutes();
    this.storageFacility = configManager.getStorageFacility();
    this.activeVehicles = new ArrayList<Vehicle>();
    this.completedTripVehicles = new ArrayList<Vehicle>();
    this.vehicleStartTimings = new ArrayList<Integer>();
    this.timeSinceLastVehicle = new ArrayList<Integer>();
    if (VisualTransitSimulator.LOGGING) {
      System.out.println("////Simulation Routes////");
      for (int i = 0; i < routes.size(); i++) {
        routes.get(i).report(System.out);
      }
    }
  }

  /**
   * Starts the simulation.
   *
   * @param vehicleStartTimings start timings of bus
   * @param numTimeSteps        number of time steps
   */
  public void start(List<Integer> vehicleStartTimings, int numTimeSteps) {
    this.vehicleStartTimings = vehicleStartTimings;
    this.numTimeSteps = numTimeSteps;
    for (int i = 0; i < vehicleStartTimings.size(); i++) {
      this.timeSinceLastVehicle.add(i, 0);
    }
    simulationTimeElapsed = 0;
  }

  /**
   * Updates the simulation at each step.
   */
  public void update() {
    if (paused) {
      return;
    }
    simulationTimeElapsed++;
    if (simulationTimeElapsed > numTimeSteps) {
      return;
    }
    System.out.println("~~~~The simulation time is now at time step "
        + simulationTimeElapsed + "~~~~");
    // generate vehicles
    for (int i = 0; i < timeSinceLastVehicle.size(); i++) {
      if (timeSinceLastVehicle.get(i) <= 0) {
        Route outbound = routes.get(2 * i);
        Route inbound = routes.get(2 * i + 1);
        Line line = new Line(outbound.shallowCopy(), inbound.shallowCopy());
        if (outbound.getLineType().equals(Route.BUS_LINE)
            && inbound.getLineType().equals(Route.BUS_LINE)) {
            if (storageFacility.createBus()) {  // Check if a bus is available
              activeVehicles.add(new Bus(counter.getBusIdCounterAndIncrement(), line, Bus.CAPACITY, Bus.SPEED));
              System.out.println("Bus created at time: " + simulationTimeElapsed + ". Remaining buses: " + storageFacility.getBusesNum());
            }        
            else {
              System.out.println("No buses available to create.");
            }
        } 
        else if (outbound.getLineType().equals(Route.TRAIN_LINE) && inbound.getLineType().equals(Route.TRAIN_LINE)) {
          if (storageFacility.createTrain()) {  // Check if a train is available    
            activeVehicles.add(new Train(counter.getTrainIdCounterAndIncrement(), line, Train.CAPACITY, Train.SPEED));
            System.out.println("Train created at time: " + simulationTimeElapsed + ". Remaining trains: " + storageFacility.getTrainsNum());
          }
          else {
            System.out.println("No trains available to create.");
          }
        }
        timeSinceLastVehicle.set(i, vehicleStartTimings.get(i));
      } else {
        timeSinceLastVehicle.set(i, timeSinceLastVehicle.get(i) - 1);
      }
    }

    // update vehicles
    for (int i = activeVehicles.size() - 1; i >= 0; i--) {
      Vehicle currVehicle = activeVehicles.get(i);
      currVehicle.update();
      if (currVehicle.isTripComplete()) {
        Vehicle completedTripVehicle = activeVehicles.remove(i);
        completedTripVehicles.add(completedTripVehicle);
      
        // Release the vehicle back to the storage facility
        if (completedTripVehicle instanceof Bus) {
          storageFacility.terminateBus();
          System.out.println("Bus completed its trip and returned to storage.");
        }
        else if (completedTripVehicle instanceof Train) {
          storageFacility.terminateTrain();
          System.out.println("Train completed its trip and returned to storage.");
        }
      }
      else {
        if (LOGGING) {
          currVehicle.report(System.out);
        }
      }
    }

    // update routes
    for (int i = 0; i < routes.size(); i++) {
      Route currRoute = routes.get(i);
      currRoute.update();
      if (VisualTransitSimulator.LOGGING) {
        currRoute.report(System.out);
      }
    }
  }

  public List<Route> getRoutes() {
    return routes;
  }

  public List<Vehicle> getActiveVehicles() {
    return activeVehicles;
  }

  public void togglePause() {
    paused = !paused;
  }
}
