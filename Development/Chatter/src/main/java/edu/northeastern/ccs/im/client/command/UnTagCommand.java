package edu.northeastern.ccs.im.client.command;

import edu.northeastern.ccs.im.client.IMConnection;
import edu.northeastern.ccs.im.client.message.MessageFactory;

public class UnTagCommand implements CommandInterface {

  private String username;

  public UnTagCommand(String[] tokens) {
    this.username = tokens[1];
  }

  /**
   * This method is the caller that performs the action.
   */
  @Override
  public void executeCommand(IMConnection connection) {
    connection.sendMessage(MessageFactory.makeUnTagMessage(username));
  }
}
