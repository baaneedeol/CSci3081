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

public class RouteTest {

  private List<Stop> stops;

  /**
   * Setup operations before each test runs.
   * Create three stops for the route.
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
    PassengerFactory.DETERMINISTIC = true;
    PassengerFactory.DETERMINISTIC_NAMES_COUNT = 0;
    PassengerFactory.DETERMINISTIC_DESTINATION_COUNT = 0;
    RandomPassengerGenerator.DETERMINISTIC = true;
  }

  /**
   * Create a test route with three stops.
   */
  public Route createTestRoute() {
    List<Double> distances = new ArrayList<>();
    distances.add(0.9712663713083954);
    distances.add(0.961379387775189);
    List<Double> probabilities = new ArrayList<>();
    probabilities.add(.15);
    probabilities.add(0.3);
    probabilities.add(.0);
    PassengerGenerator generator = new RandomPassengerGenerator(stops, probabilities);
    return new Route(10, "testLine", "BUS", "testRoute",
        stops, distances, generator);
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
   * Helper method to extract the wait time of a passenger from its report.
   *
   * @param passenger Passenger to get the wait time for.
   * @return Wait time of the passenger.
   */
  private int getPassengerWaitTime(Passenger passenger) {
    String passengerReport = getPassengerReport(passenger);
    String waitTimeLine = passengerReport.split(System.lineSeparator())[3]; // Line with wait time
    String waitTimeString = waitTimeLine.split(": ")[1]; // Extract wait time value
    return Integer.parseInt(waitTimeString);
  }

  /**
   * Test the Route constructor to ensure it initializes all fields correctly.
   */
  @Test
  public void testConstructorNormal() {
    Route testRoute = createTestRoute();
    assertEquals(10, testRoute.getId());
    assertEquals("testLine", testRoute.getLineName());
    assertEquals("BUS", testRoute.getLineType());
    assertEquals("testRoute", testRoute.getName());
    assertEquals(stops, testRoute.getStops()); // Check if stops are the same
    assertEquals(0, testRoute.getDestinationStopIndex());
    assertEquals(stops.get(0), testRoute.getDestinationStop());
  }

  /**
   * Test the shallowCopy method to ensure it creates a shallow copy of the route.
   */
  @Test
  public void testShallowCopy() {
    Route testRoute = createTestRoute();
    Route shallowCopyRoute = testRoute.shallowCopy();

    assertEquals(testRoute.getId(), shallowCopyRoute.getId());
    assertEquals(testRoute.getLineName(), shallowCopyRoute.getLineName());
    assertEquals(testRoute.getLineType(), shallowCopyRoute.getLineType());
    assertEquals(testRoute.getName(), shallowCopyRoute.getName());
    assertEquals(testRoute.getStops(), shallowCopyRoute.getStops()); // Shallow copy, same stops
    assertEquals(testRoute.getDestinationStopIndex(),
        shallowCopyRoute.getDestinationStopIndex());
    assertEquals(testRoute.getDestinationStop(), shallowCopyRoute.getDestinationStop());

    // Modify the original route and check that the shallow copy reflects the changes
    testRoute.nextStop();
    assertNotEquals(testRoute.getDestinationStopIndex(),
        shallowCopyRoute.getDestinationStopIndex()); // Should be different
  }

  /**
   * Test the report method to ensure it prints the route information correctly.
   */
  @Test
  public void testReport() {
    try {
      Route testRoute = createTestRoute();
      final Charset charset = StandardCharsets.UTF_8;
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      PrintStream testStream = new PrintStream(outputStream, true, charset.name());
      testRoute.report(testStream);
      outputStream.flush();
      String data = outputStream.toString(charset);
      testStream.close();
      outputStream.close();

      String expectedOutput = "####Route Info Start####" + System.lineSeparator()
          + "ID: 10" + System.lineSeparator()
          + "Line name: testLine" + System.lineSeparator()
          + "Line type: BUS" + System.lineSeparator()
          + "Name: testRoute" + System.lineSeparator()
          + "Num stops: 3" + System.lineSeparator()
          + "****Stops Info Start****" + System.lineSeparator()
          + "++++Next Stop Info Start++++" + System.lineSeparator()
          + "####Stop Info Start####" + System.lineSeparator()
          + "ID: 0" + System.lineSeparator()
          + "Name: test stop 1" + System.lineSeparator()
          + "Position: 44.972392,-93.243774" + System.lineSeparator()
          + "****Passengers Info Start****" + System.lineSeparator()
          + "Num passengers waiting: 0" + System.lineSeparator()
          + "****Passengers Info End****" + System.lineSeparator()
          + "####Stop Info End####" + System.lineSeparator()
          + "++++Next Stop Info End++++" + System.lineSeparator()
          + "####Stop Info Start####" + System.lineSeparator()
          + "ID: 1" + System.lineSeparator()
          + "Name: test stop 2" + System.lineSeparator()
          + "Position: 44.97358,-93.235071" + System.lineSeparator()
          + "****Passengers Info Start****" + System.lineSeparator()
          + "Num passengers waiting: 0" + System.lineSeparator()
          + "****Passengers Info End****" + System.lineSeparator()
          + "####Stop Info End####" + System.lineSeparator()
          + "####Stop Info Start####" + System.lineSeparator()
          + "ID: 2" + System.lineSeparator()
          + "Name: test stop 3" + System.lineSeparator()
          + "Position: 44.975392,-93.226632" + System.lineSeparator()
          + "****Passengers Info Start****" + System.lineSeparator()
          + "Num passengers waiting: 0" + System.lineSeparator()
          + "****Passengers Info End****" + System.lineSeparator()
          + "####Stop Info End####" + System.lineSeparator()
          + "****Stops Info End****" + System.lineSeparator()
          + "####Route Info End####" + System.lineSeparator();

      assertEquals(expectedOutput, data);
    } catch (IOException ioe) {
      fail();
    }
  }

  /**
   * Test the report method to ensure it prints the route information correctly
   * with a passenger at a stop.
   */
  @Test
  public void testReportWithPassengerAtStop() {
    try {
      Route testRoute = createTestRoute();
      // Add a passenger to the first stop
      Passenger passenger = new Passenger(2, "Goldy");
      testRoute.getStops().get(0).addPassengers(passenger);

      final Charset charset = StandardCharsets.UTF_8;
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      PrintStream testStream = new PrintStream(outputStream, true, charset.name());
      testRoute.report(testStream);
      outputStream.flush();
      String data = outputStream.toString(charset);
      testStream.close();
      outputStream.close();

      String expectedOutput = "####Route Info Start####" + System.lineSeparator()
          + "ID: 10" + System.lineSeparator()
          + "Line name: testLine" + System.lineSeparator()
          + "Line type: BUS" + System.lineSeparator()
          + "Name: testRoute" + System.lineSeparator()
          + "Num stops: 3" + System.lineSeparator()
          + "****Stops Info Start****" + System.lineSeparator()
          + "++++Next Stop Info Start++++" + System.lineSeparator()
          + "####Stop Info Start####" + System.lineSeparator()
          + "ID: 0" + System.lineSeparator()
          + "Name: test stop 1" + System.lineSeparator()
          + "Position: 44.972392,-93.243774" + System.lineSeparator()
          + "****Passengers Info Start****" + System.lineSeparator()
          + "Num passengers waiting: 1" + System.lineSeparator()
          + "####Passenger Info Start####" + System.lineSeparator()
          + "Name: Goldy" + System.lineSeparator()
          + "Destination: 2" + System.lineSeparator()
          + "Wait at stop: 0" + System.lineSeparator()
          + "Time on vehicle: 0" + System.lineSeparator()
          + "####Passenger Info End####" + System.lineSeparator()
          + "****Passengers Info End****" + System.lineSeparator()
          + "####Stop Info End####" + System.lineSeparator()
          + "++++Next Stop Info End++++" + System.lineSeparator()
          + "####Stop Info Start####" + System.lineSeparator()
          + "ID: 1" + System.lineSeparator()
          + "Name: test stop 2" + System.lineSeparator()
          + "Position: 44.97358,-93.235071" + System.lineSeparator()
          + "****Passengers Info Start****" + System.lineSeparator()
          + "Num passengers waiting: 0" + System.lineSeparator()
          + "****Passengers Info End****" + System.lineSeparator()
          + "####Stop Info End####" + System.lineSeparator()
          + "####Stop Info Start####" + System.lineSeparator()
          + "ID: 2" + System.lineSeparator()
          + "Name: test stop 3" + System.lineSeparator()
          + "Position: 44.975392,-93.226632" + System.lineSeparator()
          + "****Passengers Info Start****" + System.lineSeparator()
          + "Num passengers waiting: 0" + System.lineSeparator()
          + "****Passengers Info End****" + System.lineSeparator()
          + "####Stop Info End####" + System.lineSeparator()
          + "****Stops Info End****" + System.lineSeparator()
          + "####Route Info End####" + System.lineSeparator();

      assertEquals(expectedOutput, data);
    } catch (IOException ioe) {
      fail();
    }
  }

  /**
   * Test the isAtEnd method.
   */
  @Test
  public void testIsAtEnd() {
    Route testRoute = createTestRoute();
    // Check initially not at the end.
    assertFalse(testRoute.isAtEnd());

    // Move to the end of the route.
    for (int i = 0; i < testRoute.getStops().size(); i++) {
      testRoute.nextStop();
    }
    assertTrue(testRoute.isAtEnd());
  }

  /**
   * Test the prevStop method.
   */
  @Test
  public void testPrevStop() {
    Route testRoute = createTestRoute();
    // Check initially at the first stop.
    assertEquals(stops.get(0), testRoute.prevStop());

    // Move to the second stop.
    testRoute.nextStop();
    assertEquals(stops.get(0), testRoute.prevStop());

    // Move to the end of the route.
    for (int i = 0; i < testRoute.getStops().size(); i++) {
      testRoute.nextStop();
    }
    assertEquals(stops.get(2), testRoute.prevStop()); // Should be the last stop.
  }


  /**
   * Test the nextStop method.
   */
  @Test
  public void testNextStop() {
    Route testRoute = createTestRoute();
    // Check initially at the first stop.
    assertEquals(0, testRoute.getDestinationStopIndex());
    assertEquals(stops.get(0), testRoute.getDestinationStop());

    // Move to the second stop.
    testRoute.nextStop();
    assertEquals(1, testRoute.getDestinationStopIndex());
    assertEquals(stops.get(1), testRoute.getDestinationStop());

    // Move to the end of the route.
    for (int i = 0; i < testRoute.getStops().size(); i++) {
      testRoute.nextStop();
    }
    // Index is always incremented in nextStop.
    assertEquals(4, testRoute.getDestinationStopIndex());
    assertEquals(stops.get(2), testRoute.getDestinationStop()); // Destination remains the last.
  }

  /**
   * Test the getDestinationStop method.
   */
  @Test
  public void testGetDestinationStop() {
    Route testRoute = createTestRoute();
    assertEquals(stops.get(0), testRoute.getDestinationStop());

    testRoute.nextStop();
    assertEquals(stops.get(1), testRoute.getDestinationStop());
  }

  /**
   * Test the getNextStopDistance method.
   */
  @Test
  public void testGetNextStopDistance() {
    Route testRoute = createTestRoute();
    // Check initially at the first stop.
    assertEquals(0.0, testRoute.getNextStopDistance());

    // Move to the second stop.
    testRoute.nextStop();
    assertEquals(0.9712663713083954, testRoute.getNextStopDistance());

    // Move to the last stop.
    testRoute.nextStop();
    assertEquals(0.961379387775189, testRoute.getNextStopDistance());
  }


  /**
   * Test the generateNewPassengers method.
   */
  @Test
  public void testGenerateNewPassengers() {
    // Note: This test relies on the deterministic behavior
    // of RandomPassengerGenerator for consistency.
    Route testRoute = createTestRoute();
    int passengersGenerated = testRoute.generateNewPassengers();

    // Using the deterministic values, only two passengers should be generated.
    assertEquals(2, passengersGenerated);

    // Check that passengers have been added to the stops.
    int totalPassengersAtStops = 0;
    for (Stop stop : testRoute.getStops()) {
      totalPassengersAtStops += stop.getPassengers().size();
    }
    assertEquals(passengersGenerated, totalPassengersAtStops);
  }

  /**
   * Test the update method to ensure it updates the route and its stops correctly.
   */
  @Test
  public void testUpdate() {
    Route testRoute = createTestRoute();
    // Check that initially there are no passengers at the stops.
    for (Stop stop : testRoute.getStops()) {
      assertEquals(0, stop.getPassengers().size());
    }
    // Update the route.
    testRoute.update();
    // Check that passengers have been generated and added to the stops.
    int totalPassengersAtStops = 0;
    for (Stop stop : testRoute.getStops()) {
      totalPassengersAtStops += stop.getPassengers().size();
    }
    // Using the deterministic values, only two passengers should be generated.
    assertEquals(2, totalPassengersAtStops);
  }

  /**
   * Test that the update method updates the passengers waiting at the stops.
   */
  @Test
  public void testUpdateUpdatesPassengersAtStops() {
    Route testRoute = createTestRoute();

    // Add some passengers to the stops.
    for (Stop stop : testRoute.getStops()) {
      stop.addPassengers(new Passenger(1, "Passenger at " + stop.getName()));
    }

    // Update the route.
    testRoute.update();

    // Check that the passengers' wait times have increased at each stop.
    // Note: More passengers may be generated and added to the stops.
    for (Stop stop : testRoute.getStops()) {
      for (Passenger passenger : stop.getPassengers()) {
        assertEquals(1, getPassengerWaitTime(passenger));
      }
    }
  }


}