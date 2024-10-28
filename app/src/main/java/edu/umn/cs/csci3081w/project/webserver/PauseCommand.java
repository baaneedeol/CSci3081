package edu.umn.cs.csci3081w.project.webserver;

import com.google.gson.JsonObject;

public class PauseCommand extends SimulatorCommand {

  private VisualTransitSimulator simulator;

  public PauseCommand(VisualTransitSimulator simulator) {
    this.simulator = simulator;
  }

  /**
   * Pauses and resumes the simulation.
   *
   * @param session current simulation session
   * @param command the pause simulation command content
   */
  @Override
  public void execute(WebServerSession session, JsonObject command) {
    simulator.togglePause();
  }

}
