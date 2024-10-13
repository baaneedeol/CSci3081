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
import org.junit.jupiter.api.Test;

public class PassengerTest {

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
   * Test the Passenger constructor to ensure it initializes all fields correctly.
   */
  @Test
  public void testConstructorNormal() {
    int destinationStopId = 5;
    String name = "Goldy";
    Passenger passenger = new Passenger(destinationStopId, name);
    assertEquals(destinationStopId, passenger.getDestination());

    String passengerReport = getPassengerReport(passenger);
    assertTrue(passengerReport.contains("Name: " + name));
    assertTrue(passengerReport.contains("Destination: " + destinationStopId));
    assertTrue(passengerReport.contains("Wait at stop: 0"));
    assertTrue(passengerReport.contains("Time on vehicle: 0"));
  }

  /**
   * Test the pasUpdate method when the passenger is not on a vehicle.
   */
  @Test
  public void testPasUpdateNotOnVehicle() {
    Passenger passenger = new Passenger(1, "Goldy");

    passenger.pasUpdate();
    String passengerReport = getPassengerReport(passenger);
    assertTrue(passengerReport.contains("Wait at stop: 1"));
    assertTrue(passengerReport.contains("Time on vehicle: 0"));

    passenger.pasUpdate();
    passengerReport = getPassengerReport(passenger);
    assertTrue(passengerReport.contains("Wait at stop: 2"));
    assertTrue(passengerReport.contains("Time on vehicle: 0"));
  }

  /**
   * Test the pasUpdate method when the passenger is on a vehicle.
   */
  @Test
  public void testPasUpdateOnVehicle() {
    Passenger passenger = new Passenger(1, "Goldy");

    passenger.setOnVehicle();
    passenger.pasUpdate();
    String passengerReport = getPassengerReport(passenger);
    assertTrue(passengerReport.contains("Wait at stop: 0"));
    // setOnVehicle sets to 1, then pasUpdate increments by 1
    assertTrue(passengerReport.contains("Time on vehicle: 2"));

    passenger.pasUpdate();
    passengerReport = getPassengerReport(passenger);
    assertTrue(passengerReport.contains("Wait at stop: 0"));
    assertTrue(passengerReport.contains("Time on vehicle: 3"));
  }

  /**
   * Test the setOnVehicle method.
   */
  @Test
  public void testSetOnVehicle() {
    Passenger passenger = new Passenger(1, "Goldy");
    passenger.setOnVehicle();
    String passengerReport = getPassengerReport(passenger);
    assertTrue(passengerReport.contains("Time on vehicle: 1"));
  }

  /**
   * Test the isOnVehicle method when the passenger is not on a vehicle.
   */
  @Test
  public void testIsOnVehicleFalse() {
    Passenger passenger = new Passenger(1, "Goldy");
    assertFalse(passenger.isOnVehicle());
  }


  /**
   * Test the isOnVehicle method when the passenger is on a vehicle.
   */
  @Test
  public void testIsOnVehicleTrue() {
    Passenger passenger = new Passenger(1, "Goldy");
    passenger.setOnVehicle();
    assertTrue(passenger.isOnVehicle());
  }

  /**
   * Test the getDestination method.
   */
  @Test
  public void testGetDestination() {
    int destinationStopId = 5;
    Passenger passenger = new Passenger(destinationStopId, "Goldy");
    assertEquals(destinationStopId, passenger.getDestination());
  }


  /**
   * Test the report method for a passenger.
   */
  @Test
  public void testReport() {
    try {
      Passenger passenger = new Passenger(1, "Goldy");
      final Charset charset = StandardCharsets.UTF_8;
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      PrintStream testStream = new PrintStream(outputStream, true, charset.name());
      passenger.report(testStream);
      outputStream.flush();
      String data = outputStream.toString(charset);
      testStream.close();
      outputStream.close();
      String strToCompare =
          "####Passenger Info Start####" + System.lineSeparator()
              + "Name: Goldy" + System.lineSeparator()
              + "Destination: 1" + System.lineSeparator()
              + "Wait at stop: 0" + System.lineSeparator()
              + "Time on vehicle: 0" + System.lineSeparator()
              + "####Passenger Info End####" + System.lineSeparator();
      assertEquals(data, strToCompare);
    } catch (IOException ioe) {
      fail();
    }
  }
}