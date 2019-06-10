package edu.northeastern.ccs.im.client.message;

public class TagMessage extends Message {

  /**
   * Create a message with the given type
   *
   * @param username username of the user
   */
  public TagMessage(String username) {
    super(MessageType.TAG, username);
  }

  /**
   * Create a message with the given type, sender and text
   *
   * @param type handle for the message
   * @param sender senders identification
   * @param text text of the message
   */
  public TagMessage(MessageType type, String sender, String text) {
    super(type, sender, text);
  }
}
