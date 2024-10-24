package edu.umn.cs.csci3081w.project.model;

/**
 * Represents a transit line consisting of an outbound route and an inbound route.
 */
public class Line {
  private Route outboundRoute;
  private Route inboundRoute;

  /**
   * Constructs a Line with specified outbound and inbound routes.
   *
   * @param outboundRoute the route for outbound travel
   * @param inboundRoute  the route for inbound travel
   */
  public Line(Route outboundRoute, Route inboundRoute) {
    this.outboundRoute = outboundRoute;
    this.inboundRoute = inboundRoute;
  }

  /**
   * Gets the outbound route of this line.
   *
   * @return the outbound route
   */
  public Route getOutboundRoute() {
    return outboundRoute;
  }

  /**
   * Gets the inbound route of this line.
   *
   * @return the inbound route
   */
  public Route getInboundRoute() {
    return inboundRoute;
  }
}
