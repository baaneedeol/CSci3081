package edu.umn.cs.csci3081w.project.model;

/**
 * Represents a geographical position with longitude and latitude.
 */
public class Position {

  private double longitude;
  private double latitude;

  /**
   * Constructor for Position.
   *
   * @param longitude the longitude of the position.
   * @param latitude the latitude of the position.
   */
  public Position(double longitude, double latitude) {
    this.longitude = longitude;
    this.latitude = latitude;
  }

  /**
   * Get the longitude of the position.
   *
   * @return the longitude.
   */
  public double getLongitude() {
    return longitude;
  }

  /**
   * Get the latitude of the position.
   *
   * @return the latitude.
   */
  public double getLatitude() {
    return latitude;
  }

}
