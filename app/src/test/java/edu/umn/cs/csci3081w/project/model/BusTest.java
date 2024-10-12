package edu.umn.cs.csci3081w.project.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BusTest {

  private List<Stop> stops;

  /**
   * Setup operations before each test runs. Create three stops for the bus so that the routes
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
   * Create the out test route for the bus. The route has three stops, and the distance
   * from each stop is added to the list of distances. The probabilities of passengers
   * getting on the bus at each stop are added to the list of probabilities.
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
    return new Route(10, "testLine", "BUS", "testRouteOut",
        stopsOut, distancesOut, generatorOut);
  }

  /**
   * Create the in test route for the bus.
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
    return new Route(11, "testLine", "BUS", "testRouteIn",
        stopsIn, distancesIn, generatorIn);
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
   * Test the Bus constructor to ensure it initializes all fields correctly.
   */
  @Test
  public void testConstructorNormal() {
    Route testRouteOut = createTestOutRoute();
    Route testRouteIn = createTestInRoute();
    Bus bus = new Bus(0, testRouteOut, testRouteIn, 5, 1);
    assertEquals(0, bus.getId());
    assertEquals(5, bus.getCapacity());
    assertEquals(1, bus.getSpeed());
    assertEquals("testRouteOut0", bus.getName());
    assertEquals(-93.243774, bus.getPosition().getLongitude());
    assertEquals(44.972392, bus.getPosition().getLatitude());
    assertEquals(testRouteOut.getDestinationStop(), bus.getNextStop());
  }

  /**
   * Test the report method to ensure it prints the bus information correctly.
   */
  @Test
  public void testReport() {
    // In try catch block to handle Exception with PrintStream.
    try {
      Route testRouteOut = createTestOutRoute();
      Route testRouteIn = createTestInRoute();
      Bus bus = new Bus(0, testRouteOut, testRouteIn, 5, 1);
      // Add a passenger to the bus as it's info should be reported.
      Passenger passenger = new Passenger(1, "Goldy");
      bus.loadPassenger(passenger);
      // Stream to capture the output of the report method.
      final Charset charset = StandardCharsets.UTF_8;
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      PrintStream testStream = new PrintStream(outputStream, true, charset.name());
      bus.report(testStream);
      String output = outputStream.toString(charset);
      testStream.close();
      outputStream.close();
      String expectedOutput = "####Bus Info Start####" + System.lineSeparator()
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
          + "####Bus Info End####" + System.lineSeparator();
      assertEquals(expectedOutput, output);
    } catch (IOException ioe) {
      fail();
    }
  }

  /**
   * Test the isTripComplete method to ensure it returns true when the bus has reached the end of
   * both routes.
   */
  @Test
  public void testIsTripComplete() {
    Route testRouteOut = createTestOutRoute();
    Route testRouteIn = createTestInRoute();
    Bus bus = new Bus(0, testRouteOut, testRouteIn, 5, 1);
    // Bus currently at start of outbound (stop 0)
    assertFalse(bus.isTripComplete());
    bus.move();
    // Bus currently at outbound test stop 1
    assertFalse(bus.isTripComplete());
    bus.move();
    // Bus currently at outbound test stop 2
    assertFalse(bus.isTripComplete());
    bus.move();
    // Bus currently at end of outbound route (test stop 3)
    assertFalse(bus.isTripComplete());
    bus.move();
    // Bus currently at start of inbound route (test stop 3)
    assertFalse(bus.isTripComplete());
    bus.move();
    // Bus currently at inbound test stop 2
    assertFalse(bus.isTripComplete());
    bus.move();
    // Bus currently at inbound test stop 1 and trip is done
    assertTrue(bus.isTripComplete());
  }

  /**
   * Test the loadPassenger method to ensure it loads passengers onto the bus correctly.
   */
  @Test
  public void testLoadPassenger() {
    Route testRouteOut = createTestOutRoute();
    Route testRouteIn = createTestInRoute();
    Bus bus = new Bus(0, testRouteOut, testRouteIn, 5, 1);
    Passenger passenger1 = new Passenger(1, "Goldy1");
    Passenger passenger2 = new Passenger(1, "Goldy2");
    Passenger passenger3 = new Passenger(1, "Goldy3");
    Passenger passenger4 = new Passenger(1, "Goldy4");
    Passenger passenger5 = new Passenger(1, "Goldy5");
    Passenger passenger6 = new Passenger(1, "Goldy6");
    int result1 = bus.loadPassenger(passenger1);
    assertEquals(1, result1);
    assertEquals(1, bus.getPassengers().size());

    int result2 = bus.loadPassenger(passenger2);
    assertEquals(1, result2);
    assertEquals(2, bus.getPassengers().size());

    int result3 = bus.loadPassenger(passenger3);
    assertEquals(1, result3);
    assertEquals(3, bus.getPassengers().size());

    int result4 = bus.loadPassenger(passenger4);
    assertEquals(1, result4);
    assertEquals(4, bus.getPassengers().size());

    int result5 = bus.loadPassenger(passenger5);
    assertEquals(1, result5);
    assertEquals(5, bus.getPassengers().size());

    // Bus is full, so no more passengers can be loaded.
    int result6 = bus.loadPassenger(passenger6);
    assertEquals(0, result6);
    assertEquals(5, bus.getPassengers().size());
  }

  /**
   * Test move method to ensure the bus moves to the next stop.
   */
  @Test
  public void testMove() {
    Route testRouteOut = createTestOutRoute();
    Route testRouteIn = createTestInRoute();
    Bus bus = new Bus(0, testRouteOut, testRouteIn, 5, 1);
    // Bus currently at start of outbound (stop 0)
    // NOTE: When the bus is created, the bus position is set to the first stop,
    // but the bus is not actually at the first stop since the first stop in the route is
    // also set as the next stop.
    assertEquals(-93.243774, bus.getPosition().getLongitude());
    assertEquals(44.972392, bus.getPosition().getLatitude());
    // Test the next stop is the first stop in the route.
    assertEquals(testRouteOut.getStops().getFirst(), bus.getNextStop());
    bus.move();
    // Bus currently at outbound test stop 1
    assertEquals(-93.243774, bus.getPosition().getLongitude());
    assertEquals(44.972392, bus.getPosition().getLatitude());
    // Test the next stop is the second stop in the route.
    assertEquals(testRouteOut.getStops().get(1), bus.getNextStop());
    bus.move();
    // Bus currently at outbound test stop 2
    assertEquals(-93.235071, bus.getPosition().getLongitude());
    assertEquals(44.973580, bus.getPosition().getLatitude());
    // Test the next stop is the third stop in the route.
    assertEquals(testRouteOut.getStops().get(2), bus.getNextStop());
  }

  /**
   * Test the move method with people on the bus to ensure they are updated correctly.
   * The bus should get more passengers at each stop, but the bus should not be able to
   * load more passengers if it is full.
   */
  @Test
  public void testMoveWithPassengers() {
    Route testRouteOut = createTestOutRoute();
    Route testRouteIn = createTestInRoute();
    List<Passenger> passengers = new ArrayList<>();
    for (int i = 1; i <= 6; i++) {
      passengers.add(new Passenger(0, "Goldy" + i));
    }
    // Add passengers to stops so they can be loaded onto the bus later.
    testRouteOut.getStops().get(0).addPassengers(passengers.get(0));
    testRouteOut.getStops().get(1).addPassengers(passengers.get(1));
    testRouteOut.getStops().get(1).addPassengers(passengers.get(2));
    testRouteOut.getStops().get(2).addPassengers(passengers.get(3));
    testRouteOut.getStops().get(2).addPassengers(passengers.get(4));
    testRouteOut.getStops().get(2).addPassengers(passengers.get(5));

    Bus bus = new Bus(0, testRouteOut, testRouteIn, 5, 1);
    // Bus currently at start of outbound (stop 0)
    // NOTE: When the bus is created, the bus position is set to the first stop,
    // but the bus is not actually at the first stop since the first stop in the route is
    // also set as the next stop.
    // Test the next stop is the first stop in the route.
    bus.move();
    // Bus currently at outbound test stop 1.
    assertEquals(1, bus.getPassengers().size());
    assertEquals(0, testRouteOut.getStops().get(0).getPassengers().size());
    bus.move();
    // Bus currently at outbound test stop 2.
    assertEquals(3, bus.getPassengers().size());
    assertEquals(0, testRouteOut.getStops().get(1).getPassengers().size());
    bus.move();
    // Bus currently at outbound test stop 3 and passengers are loaded onto the bus
    // and the stop should have one passenger left.
    assertEquals(5, bus.getPassengers().size());
    assertEquals(1, testRouteOut.getStops().get(2).getPassengers().size());
    // Move the bus and check that passengers get off the bus at stop 1.
    bus.move();
    // Bus currently at start of inbound (stop 3). Nothing should happen at this stop.
    assertEquals(5, bus.getPassengers().size());
    assertEquals(1, testRouteIn.getStops().get(0).getPassengers().size());
    bus.move();
    // Bus currently at inbound test stop 2. Nothing should happen at this stop.
    assertEquals(5, bus.getPassengers().size());
    assertEquals(0, testRouteIn.getStops().get(1).getPassengers().size());
    bus.move();
    // Bus currently at inbound test stop 1. Passengers should get off the bus.
    assertEquals(0, bus.getPassengers().size());
    assertEquals(0, testRouteIn.getStops().get(2).getPassengers().size());
    // Make sure all the passengers were updated by comparing reports.
    List<String> passengerNames = Arrays.asList("Goldy1", "Goldy2", "Goldy3",
        "Goldy4", "Goldy5", "Goldy6");
    // Passengers will have no wait stop since stop is not updated in this test.
    List<Integer> passengerWaitAtStop = Arrays.asList(0, 0, 0, 0, 0, 0);
    List<Integer> passengerTimeOnVehicle = Arrays.asList(6, 5, 5, 4, 4, 0);
    for (int i = 0; i < passengers.size(); i++) {
      Passenger passenger = passengers.get(i);
      String passengerReport = getPassengerReport(passenger);
      assertTrue(passengerReport.contains(passengerNames.get(i)));
      assertTrue(passengerReport.contains("Wait at stop: " + passengerWaitAtStop.get(i)));
      assertTrue(passengerReport.contains("Time on vehicle: " + passengerTimeOnVehicle.get(i)));
    }
  }

  /**
   * Test the update method to ensure it updates the bus and passengers correctly.
   * Because the update method calls the move method, the move method is tested here as well.
   */
  @Test
  public void testUpdate() {
    testMove();
  }

  /**
   * Test the update method with passengers on the bus to ensure they are updated correctly.
   * Because the update method calls the move method, the move method is tested here as well.
   */
  @Test
  public void testUpdateWithPassengers() {
    testMoveWithPassengers();
  }

}
