package edu.umn.cs.csci3081w.project.webserver;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the state of a web server session, including available commands.
 */
public class WebServerSessionState {

  private Map<String, SimulatorCommand> commands;

  /**
   * Constructor for WebServerSessionState. Initializes the commands map.
   */
  public WebServerSessionState() {
    this.commands = new HashMap<String, SimulatorCommand>();
  }

  /**
   * Get the commands available in the session.
   *
   * @return a map of command names to command objects
   */
  public Map<String, SimulatorCommand> getCommands() {
    return commands;
  }
}
