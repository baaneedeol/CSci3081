package edu.umn.cs.csci3081w.project.webserver;

import com.google.gson.JsonObject;

/**
 * Abstract class representing a command in the simulator.
 */
public abstract class SimulatorCommand {

  /**
   * Default constructor for SimulatorCommand.
   */
  public SimulatorCommand() {
    // Default constructor
  }

  /**
   * Executes the command with the given session and command data.
   *
   * @param session the current web server session
   * @param command the command data in JSON format
   */
  public abstract void execute(WebServerSession session, JsonObject command);
}
