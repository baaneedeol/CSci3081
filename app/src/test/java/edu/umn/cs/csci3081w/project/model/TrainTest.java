package edu.umn.cs.csci3081w.project.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class TrainTest {

  private List<Stop> stops;

  /**
   * Setup operations before each test runs.
   * Create three stops for the train so that the routes
   * have same stop objects.
   */
  @BeforeEach
  public void setUp() {
    Stop stop1 = new Stop(0, "test stop 1", new Position(-93.243774, 44.972392));
    Stop stop2 = new Stop(1, "test stop 2", new Position(-93.235071, 44.973580));
    Stop stop3 = new Stop(2, "test stop 3", new Position(-93.226632, 44.975392));
    stops = new ArrayList<>();
    stops.add(stop1);
    stops.add(stop2);
    stops.add(stop3);
  }

  /**
   * Creates an alternate stop list for the train.
   *
   * @return List of stops.
   */
  public List<Stop> createAlternateStops() {
    Stop stop1 = new Stop(0, "test stop 1", new Position(-93.0, 44.0));
    Stop stop2 = new Stop(1, "test stop 2", new Position(-93.1, 44.1));
    Stop stop3 = new Stop(2, "test stop 3", new Position(-93.2, 44.2));
    List<Stop> stops = new ArrayList<>();
    stops.add(stop1);
    stops.add(stop2);
    stops.add(stop3);
    return stops;
  }

  /**
   * Create the out test route for the train. The route has three stops, and the distance
   * from each stop is added to the list of distances. The probabilities of passengers
   * getting on the train at each stop are added to the list of probabilities.
   */
  public Route createTestOutRoute() {
    List<Stop> stopsOut = new ArrayList<Stop>();
    stopsOut.add(stops.get(0));
    stopsOut.add(stops.get(1));
    stopsOut.add(stops.get(2));
    List<Double> distancesOut = new ArrayList<Double>();
    distancesOut.add(0.9712663713083954);
    distancesOut.add(0.961379387775189);
    List<Double> probabilitiesOut = new ArrayList<Double>();
    probabilitiesOut.add(.15);
    probabilitiesOut.add(0.3);
    probabilitiesOut.add(.0);
    PassengerGenerator generatorOut = new RandomPassengerGenerator(stopsOut, probabilitiesOut);
    return new Route(10, "testLine", "TRAIN", "testRouteOut",
        stopsOut, distancesOut, generatorOut);
  }

  /**
   * Create the in test route for the train.
   */
  public Route createTestInRoute() {
    List<Stop> stopsIn = new ArrayList<>();
    stopsIn.add(stops.get(2));
    stopsIn.add(stops.get(1));
    stopsIn.add(stops.get(0));
    List<Double> distancesIn = new ArrayList<>();
    distancesIn.add(0.961379387775189);
    distancesIn.add(0.9712663713083954);
    List<Double> probabilitiesIn = new ArrayList<>();
    probabilitiesIn.add(.025);
    probabilitiesIn.add(0.3);
    probabilitiesIn.add(.0);
    PassengerGenerator generatorIn = new RandomPassengerGenerator(stopsIn, probabilitiesIn);
    return new Route(11, "testLine", "TRAIN", "testRouteIn",
        stopsIn, distancesIn, generatorIn);
  }


  /**
   * Helper method to create a list of passengers with the given count.
   * @param count Number of passengers to create.
   * @return List of created passengers.
   */
  private List<Passenger> createPassengers(int count) {
    List<Passenger> passengers = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      passengers.add(new Passenger(0, "Goldy" + i));
    }
    return passengers;
  }

  /**
   * Helper method to add passengers to stops.
   * @param stops List of stops.
   * @param passengers List of passengers.
   */
  private void addPassengersToStops(List<Stop> stops, List<Passenger> passengers) {
    int stopIndex = 0;
    for (Passenger passenger : passengers) {
      stops.get(stopIndex).addPassengers(passenger);
      stopIndex = (stopIndex + 1) % stops.size(); // Cycle through stops
    }
  }

  /**
   * Helper method to check passenger updates against expected values.
   * @param passengers List of passengers.
   * @param expectedWaitAtStop List of expected wait times at stop.
   * @param expectedTimeOnVehicle List of expected time spent on vehicle.
   */
  private void checkPassengerUpdates(List<Passenger> passengers,
                                     List<Integer> expectedWaitAtStop,
                                     List<Integer> expectedTimeOnVehicle) {
    for (int i = 0; i < passengers.size(); i++) {
      Passenger passenger = passengers.get(i);
      String passengerReport = getPassengerReport(passenger);
      assertTrue(passengerReport.contains("Wait at stop: " + expectedWaitAtStop.get(i)));
      assertTrue(passengerReport.contains("Time on vehicle: " + expectedTimeOnVehicle.get(i)));
    }
  }

  /**
   * Get the string output of the report method for a passenger.
   * @param passenger Passenger to get the report for.
   * @return String output of the report method.
   */
  private String getPassengerReport(Passenger passenger) {
    try {
      // Open a stream to capture the output of the report method.
      final Charset charset = StandardCharsets.UTF_8;
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      PrintStream testStream = new PrintStream(outputStream, true, charset.name());
      passenger.report(testStream);
      String output = outputStream.toString(charset);
      testStream.close();
      outputStream.close();
      return output;
    } catch (IOException ioe) {
      return "";
    }
  }

  /**
   * Test the Train constructor to ensure it initializes all fields correctly.
   */
  @Test
  public void testConstructorNormal() {
    Route testRouteOut = createTestOutRoute();
    Route testRouteIn = createTestInRoute();
    Line testLine = new Line(testRouteOut, testRouteIn);
    Train train = new Train(0, testLine, 5, 1);
    assertEquals(0, train.getId());
    assertEquals(5, train.getCapacity());
    assertEquals(1, train.getSpeed());
    assertEquals("testRouteOut0", train.getName());
    assertEquals(-93.243774, train.getPosition().getLongitude());
    assertEquals(44.972392, train.getPosition().getLatitude());
    assertEquals(testRouteOut.getStops().getFirst(), train.getNextStop());
    assertEquals(0, train.getPassengers().size());
  }

  /**
   * Test the report method to ensure it prints the train information correctly with
   * no passengers on the train.
   */
  @Test
  public void testReportNoPassengers() {
    // In try catch block to handle Exception with PrintStream.
    try {
      Route testRouteOut = createTestOutRoute();
      Route testRouteIn = createTestInRoute();
      Line testLine = new Line(testRouteOut, testRouteIn);
      Train train = new Train(0, testLine, 5, 1);
      // Stream to capture the output of the report method.
      final Charset charset = StandardCharsets.UTF_8;
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      PrintStream testStream = new PrintStream(outputStream, true, charset.name());
      train.report(testStream);
      String output = outputStream.toString(charset);
      testStream.close();
      outputStream.close();
      String expectedOutput = "####Train Info Start####" + System.lineSeparator()
          + "ID: 0" + System.lineSeparator()
          + "Name: testRouteOut0" + System.lineSeparator()
          + "Speed: 1.0" + System.lineSeparator()
          + "Capacity: 5" + System.lineSeparator()
          + "Position: 44.972392,-93.243774" + System.lineSeparator()
          + "Distance to next stop: 0.0" + System.lineSeparator()
          + "****Passengers Info Start****" + System.lineSeparator()
          + "Num of passengers: 0" + System.lineSeparator()
          + "****Passengers Info End****" + System.lineSeparator()
          + "####Train Info End####" + System.lineSeparator();
      assertEquals(expectedOutput, output);
    } catch (IOException ioe) {
      fail();
    }
  }

  /**
   * Test the report method to ensure it prints the train information correctly with
   * a passenger on the train.
   */
  @Test
  public void testReportWithPassenger() {
    // In try catch block to handle Exception with PrintStream.
    try {
      Route testRouteOut = createTestOutRoute();
      Route testRouteIn = createTestInRoute();
      Line testLine = new Line(testRouteOut, testRouteIn);
      Train train = new Train(0, testLine, 5, 1);
      // Add a passenger to the train as it's info should be reported.
      Passenger passenger = new Passenger(1, "Goldy");
      train.loadPassenger(passenger);
      // Stream to capture the output of the report method.
      final Charset charset = StandardCharsets.UTF_8;
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      PrintStream testStream = new PrintStream(outputStream, true, charset.name());
      train.report(testStream);
      String output = outputStream.toString(charset);
      testStream.close();
      outputStream.close();
      String expectedOutput = "####Train Info Start####" + System.lineSeparator()
          + "ID: 0" + System.lineSeparator()
          + "Name: testRouteOut0" + System.lineSeparator()
          + "Speed: 1.0" + System.lineSeparator()
          + "Capacity: 5" + System.lineSeparator()
          + "Position: 44.972392,-93.243774" + System.lineSeparator()
          + "Distance to next stop: 0.0" + System.lineSeparator()
          + "****Passengers Info Start****" + System.lineSeparator()
          + "Num of passengers: 1" + System.lineSeparator()
          + "####Passenger Info Start####" + System.lineSeparator()
          + "Name: Goldy" + System.lineSeparator()
          + "Destination: 1" + System.lineSeparator()
          + "Wait at stop: 0" + System.lineSeparator()
          + "Time on vehicle: 1" + System.lineSeparator()
          + "####Passenger Info End####" + System.lineSeparator()
          + "****Passengers Info End****" + System.lineSeparator()
          + "####Train Info End####" + System.lineSeparator();
      assertEquals(expectedOutput, output);
    } catch (IOException ioe) {
      fail();
    }
  }

  /**
   * Test the isTripComplete method to ensure it returns true when the train has reached the end of
   * both routes.
   */
  @Test
  public void testIsTripComplete() {
    Route testRouteOut = createTestOutRoute();
    Route testRouteIn = createTestInRoute();
    Line testLine = new Line(testRouteOut, testRouteIn);
    Train train = new Train(0, testLine, 5, 1);
    // Train currently at start of outbound (stop 0)
    assertFalse(train.isTripComplete());
    train.move();
    // Train currently at outbound test stop 1
    assertFalse(train.isTripComplete());
    train.move();
    // Train currently at outbound test stop 2
    assertFalse(train.isTripComplete());
    train.move();
    // Train currently at end of outbound route (test stop 3)
    assertFalse(train.isTripComplete());
    train.move();
    // Train currently at start of inbound route (test stop 3)
    assertFalse(train.isTripComplete());
    train.move();
    // Train currently at inbound test stop 2
    assertFalse(train.isTripComplete());
    train.move();
    // Train currently at inbound test stop 1 and trip is done
    assertTrue(train.isTripComplete());
  }


  /**
   * Test the loadPassenger method with different passenger counts.
   * @param passengerCount Number of passengers to load.
   */
  @ParameterizedTest
  @ValueSource(ints = {0, 1, 4, 5, 6})
  public void testLoadPassenger(int passengerCount) {
    Route testRouteOut = createTestOutRoute();
    Route testRouteIn = createTestInRoute();
    Line testLine = new Line(testRouteOut, testRouteIn);
    Train train = new Train(0, testLine, 5, 1);
    List<Passenger> passengers = createPassengers(passengerCount);

    int loadedCount = 0;
    for (Passenger passenger : passengers) {
      loadedCount += train.loadPassenger(passenger);
    }

    int expectedLoadedCount = Math.min(passengerCount, 5); // Capacity is 5
    assertEquals(expectedLoadedCount, loadedCount);
    assertEquals(expectedLoadedCount, train.getPassengers().size());
  }

  /**
   * Test unloading passengers at a stop.
   */
  @Test
  public void testUnloadPassengersAtStop() {
    Route testRouteOut = createTestOutRoute();
    Route testRouteIn = createTestInRoute();
    Line testLine = new Line(testRouteOut, testRouteIn);
    Train train = new Train(0, testLine, 5, 1);

    List<Passenger> passengers = createPassengers(3);
    train.getPassengers().addAll(passengers); // Directly add passengers to the train

    Stop toBeStop = testRouteOut.getStops().getFirst();
    train.move(); // Move to the next stop

    assertEquals(0, train.getPassengers().size());
    assertEquals(0, toBeStop.getPassengers().size()); // Stop should have no passengers
  }

  /**
   * Test move method to ensure the train moves to the next stop.
   */
  @Test
  public void testMove() {
    Route testRouteOut = createTestOutRoute();
    Route testRouteIn = createTestInRoute();
    Line testLine = new Line(testRouteOut, testRouteIn);
    Train train = new Train(0, testLine, 5, 1);
    // Train currently at start of outbound (stop 0)
    // NOTE: When the train is created, the train position is set to the first stop,
    // but the train is not actually at the first stop since the first stop in the route is
    // also set as the next stop.
    assertEquals(-93.243774, train.getPosition().getLongitude());
    assertEquals(44.972392, train.getPosition().getLatitude());
    // Test the next stop is the first stop in the route.
    assertEquals(testRouteOut.getStops().getFirst(), train.getNextStop());
    train.move();
    // Train currently at outbound test stop 1
    assertEquals(-93.243774, train.getPosition().getLongitude());
    assertEquals(44.972392, train.getPosition().getLatitude());
    // Test the next stop is the second stop in the route.
    assertEquals(testRouteOut.getStops().get(1), train.getNextStop());
    train.move();
    // Train currently at outbound test stop 2
    assertEquals(-93.235071, train.getPosition().getLongitude());
    assertEquals(44.973580, train.getPosition().getLatitude());
    // Test the next stop is the third stop in the route.
    assertEquals(testRouteOut.getStops().get(2), train.getNextStop());
  }


  /**
   * Test the move method with passengers on the train to ensure they are updated correctly.
   * The train should get more passengers at each stop, but the train should not be able to
   * load more passengers if it is full.
   */
  @Test
  public void testMoveHandlesPassengersCorrectly() {
    Route testRouteOut = createTestOutRoute();
    Route testRouteIn = createTestInRoute();
    List<Passenger> passengers = createPassengers(6);

    // Add passengers to the stops
    addPassengersToStops(testRouteOut.getStops(), passengers);

    Line testLine = new Line(testRouteOut, testRouteIn);
    Train train = new Train(0, testLine, 5, 1);

    // Move the train through the outbound route
    train.move(); // Stop 0 -> Stop 1 (loads 1 passenger)
    assertEquals(2, train.getPassengers().size());
    train.move(); // Stop 1 -> Stop 2 (loads 2 passengers, total 3)
    assertEquals(4, train.getPassengers().size());
    train.move(); // Stop 2 -> Stop 3 (loads 2 passengers, reaches capacity 5)
    assertEquals(5, train.getPassengers().size());

    // Check that the train doesn't load more than its capacity
    assertEquals(1, testRouteOut.getStops().get(2).getPassengers().size()); // 1 left at the stop

    // Move the train through the inbound route (no more loading should occur)
    train.move(); // Stop 3 (outbound end) -> Stop 3 (inbound start)
    assertEquals(5, train.getPassengers().size());
    train.move(); // Stop 3 -> Stop 2
    assertEquals(5, train.getPassengers().size());
    train.move(); // Stop 2 -> Stop 1 (all passengers should unload)
    assertEquals(0, train.getPassengers().size());

    // Make sure the passengers are updated correctly
    List<Integer> expectedWaitAtStop = List.of(0, 0, 0, 0, 0, 0);
    List<Integer> expectedTimeOnVehicle = List.of(6, 5, 4, 6, 5, 0);
    checkPassengerUpdates(passengers, expectedWaitAtStop, expectedTimeOnVehicle);
  }


  /**
   * Test the update method to ensure it updates the train and passengers correctly.
   */
  @Test
  public void testUpdate() {
    // Use alternate stops to make update tests different from move tests
    this.stops = createAlternateStops();
    Route testRouteOut = createTestOutRoute();
    Route testRouteIn = createTestInRoute();
    Line testLine = new Line(testRouteOut, testRouteIn);
    Train train = new Train(0, testLine, 5, 1);
    // Train currently at start of outbound (stop 0)
    // NOTE: When the train is created, the train position is set to the first stop,
    // but the train is not actually at the first stop since the first stop in the route is
    // also set as the next stop.
    assertEquals(-93.0, train.getPosition().getLongitude());
    assertEquals(44.0, train.getPosition().getLatitude());
    // Test the next stop is the first stop in the route.
    assertEquals(testRouteOut.getStops().getFirst(), train.getNextStop());
    train.update();
    // Train currently at outbound test stop 1
    assertEquals(-93.0, train.getPosition().getLongitude());
    assertEquals(44.0, train.getPosition().getLatitude());
    // Test the next stop is the second stop in the route.
    assertEquals(testRouteOut.getStops().get(1), train.getNextStop());
    train.update();
    // Train currently at outbound test stop 2
    assertEquals(-93.1, train.getPosition().getLongitude());
    assertEquals(44.1, train.getPosition().getLatitude());
    // Test the next stop is the third stop in the route.
    assertEquals(testRouteOut.getStops().get(2), train.getNextStop());
  }

  /**
   * Test the update method with passengers on the train to ensure they are updated correctly.
   */
  @Test
  public void testUpdateHandlesPassengersCorrectly() {
    Route testRouteOut = createTestOutRoute();
    Route testRouteIn = createTestInRoute();
    List<Passenger> passengers = createPassengers(4);

    Line testLine = new Line(testRouteOut, testRouteIn);
    Train train = new Train(0, testLine, 3, 1);

    // Update the train through the outbound route. No passengers should be loaded.
    train.update(); // Stop 0 -> Stop 1 (no passengers loaded)
    assertEquals(0, train.getPassengers().size());
    train.update(); // Stop 1 -> Stop 2 (no passengers loaded)
    assertEquals(0, train.getPassengers().size());
    train.update(); // Stop 2 -> Stop 3 (loads no passengers)
    assertEquals(0, train.getPassengers().size());

    // Add passengers to the first two stops of the inbound route
    addPassengersToStops(testRouteIn.getStops().subList(0, 2), passengers);

    // Train switches to the inbound route
    train.update(); // Stop 3 (outbound end) -> Stop 3 (inbound start) (loads 2 passengers)
    assertEquals(2, train.getPassengers().size());
    train.update(); // Stop 3 -> Stop 2 (loads 1 passenger, reaches capacity 3)
    assertEquals(3, train.getPassengers().size());
    assertEquals(1, testRouteIn.getStops().get(1).getPassengers().size()); // 1 left at the stop
    train.update(); // Stop 2 -> Stop 1 (all passengers should unload)
    assertEquals(0, train.getPassengers().size());

    // Make sure the passengers are updated correctly
    List<Integer> expectedWaitAtStop = List.of(0, 0, 0, 0);
    List<Integer> expectedTimeOnVehicle = List.of(3, 2, 3, 0);
    checkPassengerUpdates(passengers, expectedWaitAtStop, expectedTimeOnVehicle);
  }

  /**
   * Test that the train correctly switches to the inbound route after reaching the end of
   * the outbound route.
   */
  @Test
  public void testRouteSwitching() {
    Route testRouteOut = createTestOutRoute();
    Route testRouteIn = createTestInRoute();
    Line testLine = new Line(testRouteOut, testRouteIn);
    Train train = new Train(0, testLine, 5, 1);

    // Move the train to the end of the outbound route
    for (int i = 0; i < testRouteOut.getStops().size(); i++) {
      train.move();
    }

    // Check that the next stop is the first stop of the inbound route
    assertEquals(testRouteIn.getStops().get(0), train.getNextStop());

    // Move the train one more step and verify it's on the inbound route
    train.move();
    assertEquals(testRouteIn.getStops().get(1), train.getNextStop());
  }

  /**
   * Test the move method with different speeds.
   * @param speed Speed of the train.
   */
  @ParameterizedTest
  @ValueSource(doubles = {0.5, 0.98, 1.0, 2.0, -1.0, 0.0})
  public void testMoveWithDifferentSpeeds(double speed) {
    Route testRouteOut = createTestOutRoute();
    Route testRouteIn = createTestInRoute();
    Line testLine = new Line(testRouteOut, testRouteIn);
    Train train = new Train(0, testLine, 5, speed);

    train.move(); // Move to first stop

    Stop nextStop = train.getNextStop();
    Double distanceToNextStop = testRouteOut.getNextStopDistance();

    train.move();

    if (speed > distanceToNextStop) {
      assertNotEquals(nextStop, train.getNextStop());
    } else if (speed < distanceToNextStop) {
      // Should not move if speed is negative
      assertEquals(nextStop, train.getNextStop());
    } else {
      // Should not move if speed is zero
      assertEquals(nextStop, train.getNextStop());
    }
  }
}