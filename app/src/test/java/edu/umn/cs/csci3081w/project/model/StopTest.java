package edu.umn.cs.csci3081w.project.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

public class StopTest {

  /**
   * Setup operations before each test runs.
   */
  @BeforeEach
  public void setUp() {
    PassengerFactory.DETERMINISTIC = true;
    PassengerFactory.DETERMINISTIC_NAMES_COUNT = 0;
    PassengerFactory.DETERMINISTIC_DESTINATION_COUNT = 0;
    RandomPassengerGenerator.DETERMINISTIC = true;
  }

  /**
   * Create a bus with outgoing and incoming routes and three stops per route.
   */
  public Bus createBus() {
    Stop stop1 = new Stop(0, "test stop 1", new Position(-93.243774, 44.972392));
    Stop stop2 = new Stop(1, "test stop 2", new Position(-93.235071, 44.973580));
    Stop stop3 = new Stop(2, "test stop 2", new Position(-93.226632, 44.975392));
    List<Stop> stopsOut = new ArrayList<Stop>();
    stopsOut.add(stop1);
    stopsOut.add(stop2);
    stopsOut.add(stop3);
    List<Double> distancesOut = new ArrayList<Double>();
    distancesOut.add(0.9712663713083954);
    distancesOut.add(0.961379387775189);
    List<Double> probabilitiesOut = new ArrayList<Double>();
    probabilitiesOut.add(.15);
    probabilitiesOut.add(0.3);
    probabilitiesOut.add(.0);
    PassengerGenerator generatorOut = new RandomPassengerGenerator(stopsOut, probabilitiesOut);
    Route testRouteOut = new Route(10, "testLine", "BUS", "testRouteOut",
        stopsOut, distancesOut, generatorOut);
    List<Stop> stopsIn = new ArrayList<>();
    stopsIn.add(stop3);
    stopsIn.add(stop2);
    stopsIn.add(stop1);
    List<Double> distancesIn = new ArrayList<>();
    distancesIn.add(0.961379387775189);
    distancesIn.add(0.9712663713083954);
    List<Double> probabilitiesIn = new ArrayList<>();
    probabilitiesIn.add(.025);
    probabilitiesIn.add(0.3);
    probabilitiesIn.add(.0);
    PassengerGenerator generatorIn = new RandomPassengerGenerator(stopsIn, probabilitiesIn);
    Route testRouteIn = new Route(11, "testLine", "BUS", "testRouteIn",
        stopsIn, distancesIn, generatorIn);
    Line testLine = new Line(testRouteOut, testRouteIn);
    return new Bus(0, testLine, 5, 1);
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
   * Testing state after using constructor.
   */
  @Test
  public void testConstructorNormal() {
    Stop stop = new Stop(0, "test stop", new Position(-93.243774, 44.972392));
    assertEquals(0, stop.getId());
    assertEquals(44.972392, stop.getPosition().getLatitude());
    assertEquals(-93.243774, stop.getPosition().getLongitude());
    assertEquals(0, stop.getPassengers().size());
  }

  /**
   * Testing state of stop after adding passenger.
   */
  @Test
  public void testAddPassengers() {
    Passenger passenger = new Passenger(1, "Goldy");
    Stop stop = new Stop(0, "test stop", new Position(-93.243774, 44.972392));
    int added = stop.addPassengers(passenger);
    assertEquals(1, added);
    assertEquals(1, stop.getPassengers().size());
  }

  /**
   * Tests if passengers can be loaded onto an empty bus correctly from an empty stop
   * nothing should happen, loadPassengers returns 0.
   */
  @Test
  public void testLoadPassengersEmptyStopEmptyBus() {
    Bus testBus = createBus();
    int passengersLoaded = testBus.getNextStop().loadPassengers(testBus);
    assertEquals(0, passengersLoaded);
    long passengersOnBus = testBus.getPassengers().size();
    assertEquals(0, passengersOnBus);
  }

  /**
   * Tests if passengers can be loaded onto an full bus correctly from an empty stop
   * nothing should happen, loadPassengers returns 0.
   */
  @Test
  public void testLoadPassengersEmptyStopFullBus() {
    Bus testBus = createBus();
    Passenger passenger = new Passenger(1, "Goldy");
    Passenger passenger2 = new Passenger(1, "Gopher");
    Passenger passenger3 = new Passenger(1, "The Entire School");
    Passenger passenger4 = new Passenger(1, "Walked onto");
    Passenger passenger5 = new Passenger(1, "This Bus");
    testBus.loadPassenger(passenger);
    testBus.loadPassenger(passenger2);
    testBus.loadPassenger(passenger3);
    testBus.loadPassenger(passenger4);
    testBus.loadPassenger(passenger5);
    int passengersLoaded = testBus.getNextStop().loadPassengers(testBus);
    assertEquals(0, passengersLoaded);
    long passengersOnBus = testBus.getPassengers().size();
    assertEquals(5, passengersOnBus);
  }

  /**
   * Tests if passengers can be loaded onto a full bus correctly from a non-empty stop
   * nothing should happen, loadPassengers returns 0.
   */
  @Test
  public void testLoadPassengersNonEmptyStopFullBus() {
    Bus testBus = createBus();
    Passenger passenger = new Passenger(1, "Goldy");
    Passenger passenger2 = new Passenger(1, "Gopher");
    Passenger passenger3 = new Passenger(1, "The Entire School");
    Passenger passenger4 = new Passenger(1, "Walked onto");
    Passenger passenger5 = new Passenger(1, "This Bus");
    testBus.loadPassenger(passenger);
    testBus.loadPassenger(passenger2);
    testBus.loadPassenger(passenger3);
    testBus.loadPassenger(passenger4);
    testBus.loadPassenger(passenger5);
    Passenger passenger6 = new Passenger(1, "I never leave the stop");
    testBus.getNextStop().addPassengers(passenger6);
    int passengersLoaded = testBus.getNextStop().loadPassengers(testBus);
    assertEquals(0, passengersLoaded);
    long passengersOnBus = testBus.getPassengers().size();
    assertEquals(5, passengersOnBus);
  }

  /**
   * Tests if passengers can be loaded onto a bus with space correctly from a stop
   * loadPassengers returns the number of passengers at stop.
   */
  @Test
  public void testLoadPassengersNonEmptyStopEmptyBus() {
    Bus testBus = createBus();
    Passenger passenger = new Passenger(1, "I never leave the stop");
    testBus.getNextStop().addPassengers(passenger);
    long passengersOnBusStart = testBus.getPassengers().size();
    assertEquals(0, passengersOnBusStart);
    int passengersLoaded = testBus.getNextStop().loadPassengers(testBus);
    assertEquals(1, passengersLoaded);
    long passengersOnBusEnd = testBus.getPassengers().size();
    assertEquals(1, passengersOnBusEnd);
  }

  /**
   * Testing reporting functionality with no passenger.
   */
  @Test
  public void testStopReportNoPassengers() {
    try {
      Stop stop = new Stop(0, "test stop", new Position(-93.243774, 44.972392));
      final Charset charset = StandardCharsets.UTF_8;
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      PrintStream testStream = new PrintStream(outputStream, true, charset.name());
      stop.report(testStream);
      outputStream.flush();
      String data = new String(outputStream.toByteArray(), charset);
      testStream.close();
      outputStream.close();
      String strToCompare =
          "####Stop Info Start####" + System.lineSeparator()
              + "ID: 0" + System.lineSeparator()
              + "Name: test stop" + System.lineSeparator()
              + "Position: 44.972392,-93.243774" + System.lineSeparator()
              + "****Passengers Info Start****" + System.lineSeparator()
              + "Num passengers waiting: 0" + System.lineSeparator()
              + "****Passengers Info End****" + System.lineSeparator()
              + "####Stop Info End####" + System.lineSeparator();
      assertEquals(data, strToCompare);
    } catch (IOException ioe) {
      fail();
    }
  }

  /**
   * Testing reporting functionality with passenger.
   */
  @Test
  public void testStopReportWithPassenger() {
    try {
      Stop stop = new Stop(0, "test stop", new Position(-93.243774, 44.972392));
      Passenger passenger = new Passenger(1, "Goldy");
      stop.addPassengers(passenger);
      final Charset charset = StandardCharsets.UTF_8;
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      PrintStream testStream = new PrintStream(outputStream, true, charset.name());
      stop.report(testStream);
      outputStream.flush();
      String data = new String(outputStream.toByteArray(), charset);
      testStream.close();
      outputStream.close();
      String strToCompare =
          "####Stop Info Start####" + System.lineSeparator()
              + "ID: 0" + System.lineSeparator()
              + "Name: test stop" + System.lineSeparator()
              + "Position: 44.972392,-93.243774" + System.lineSeparator()
              + "****Passengers Info Start****" + System.lineSeparator()
              + "Num passengers waiting: 1" + System.lineSeparator()
              + "####Passenger Info Start####" + System.lineSeparator()
              + "Name: Goldy" + System.lineSeparator()
              + "Destination: 1" + System.lineSeparator()
              + "Wait at stop: 0" + System.lineSeparator()
              + "Time on vehicle: 0" + System.lineSeparator()
              + "####Passenger Info End####" + System.lineSeparator()
              + "****Passengers Info End****" + System.lineSeparator()
              + "####Stop Info End####" + System.lineSeparator();
      assertEquals(data, strToCompare);
    } catch (IOException ioe) {
      fail();
    }
  }

  /**
   * Test that the update method updates the passengers waiting at the stop.
   */
  @Test
  public void testUpdate() {
    Stop stop = new Stop(0, "test stop", new Position(-93.243774, 44.972392));
    Passenger passenger1 = new Passenger(1, "Goldy");
    Passenger passenger2 = new Passenger(2, "Gopher");
    stop.addPassengers(passenger1);
    stop.addPassengers(passenger2);

    // Call update and check that the passengers' wait times have increased.
    stop.update();
    String passenger1Report = getPassengerReport(passenger1);
    String passenger2Report = getPassengerReport(passenger2);
    assertTrue(passenger1Report.contains("Wait at stop: 1"));
    assertTrue(passenger2Report.contains("Wait at stop: 1"));

    stop.update();
    passenger1Report = getPassengerReport(passenger1);
    passenger2Report = getPassengerReport(passenger2);
    assertTrue(passenger1Report.contains("Wait at stop: 2"));
    assertTrue(passenger2Report.contains("Wait at stop: 2"));
  }
}
