/*
 * *
 *  * MIT License
 *  *
 *  * Copyright (c) 2019 Ruturaj Nene, Vishal Patel, Neha Gundecha, Kasi Karuppiah Alagappan
 *  *
 *  * Permission is hereby granted, free of charge, to any person obtaining a copy
 *  * of this software and associated documentation files (the "Software"), to deal
 *  * in the Software without restriction, including without limitation the rights
 *  * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  * copies of the Software, and to permit persons to whom the Software is
 *  * furnished to do so, subject to the following conditions:
 *  *
 *  * The above copyright notice and this permission notice shall be included in all
 *  * copies or substantial portions of the Software.
 *  *
 *  * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  * SOFTWARE.
 *
 */

package edu.northeastern.ccs.im.client.message;

import edu.northeastern.ccs.im.client.LoggerClass;

/**
 * Message Class
 */
public class Message {
  /**
   * The string sent when a field is null.
   */
  private static final String NULL_OUTPUT = "--";

  /**
   * The handle of the message.
   */
  private MessageType type;

  /**
   * The first argument used in the message. This will be the sender's identifier.
   */
  private String sender;

  /**
   * The second argument used in the message.
   */
  private String text;

  /**
   * Create a message with the given type
   *
   * @param type handle for the message
   */
  public Message(MessageType type) {
    this(type, null, null);
  }

  /**
   * Create a message with the given type and sender
   *
   * @param type   handle for the message
   * @param sender senders identification
   */
  public Message(MessageType type, String sender) {
    this(type, sender, null);
  }

  /**
   * Create a message with the given type, sender and text
   *
   * @param type   handle for the message
   * @param sender senders identification
   * @param text   text of the message
   */
  public Message(MessageType type, String sender, String text) {
    this.type = type;
    this.sender = sender;
    this.text = text;
  }

  /**
   * Return the message type
   *
   * @return MessageType that indicates the message type
   */
  public MessageType getType() {
    return type;
  }

  /**
   * Return the message sender identification
   *
   * @return String that indicates the message sender
   */
  public String getSender() {
    return sender;
  }

  /**
   * Return the message text
   *
   * @return String that indicates the message text
   */
  public String getText() {
    return text;
  }

  /**
   * Determine if this message is sent by a new client to log-in to the server.
   *
   * @return True if the message is an initialization message; false otherwise
   */
  public boolean isHello() {
    return (type == MessageType.HELLO);
  }

  /**
   * Determine if this message is an acknowledgement message.
   *
   * @return True if the message is an acknowledgement message; false otherwise.
   */
  public boolean isAcknowledge() {
    return (type == MessageType.ACKNOWLEDGE);
  }

  /**
   * Determine if this message is a no acknowledgement message.
   *
   * @return True if the message is a no acknowledgement message; false otherwise.
   */
  public boolean isNoAcknowledge() {
    return (type == MessageType.NO_ACKNOWLEDGE);
  }

  /**
   * Determine if this message is a message signing off from the IM server.
   *
   * @return True if the message is sent when signing off; false otherwise
   */
  public boolean isQuit() {
    return (type == MessageType.QUIT);
  }

  /**
   * Determine if this message is broadcasting text to everyone.
   *
   * @return True if the message is a broadcast message; false otherwise.
   */
  public boolean isBroadcast() {
    return (type == MessageType.BROADCAST);
  }

  /**
   * Determine if this message is private text to a user.
   *
   * @return True if the message is a private message; false otherwise.
   */
  public boolean isPrivate() {
    return (type == MessageType.PRIVATE);
  }

  /**
   * Determine if this message is notification message.
   *
   * @return True if the message is a notification message; false otherwise.
   */
  public boolean isNotification() {
    return (type == MessageType.NOTIFICATION);
  }

  /**
   * Determine if this message is profile message.
   *
   * @return True if the message is a profile message; false otherwise.
   */
  public boolean isProfile() {
    return (type == MessageType.PROFILE);
  }

  /**
   * Logs the message with sender and text
   */
  public void print() {
    LoggerClass.logInfo(getSender() + " : " + getText(), this.getClass());
  }

  /**
   * Check if the message should be published for processing
   *
   * @return boolean indicating if message should be processed
   */
  public Boolean shouldPublish() {
    return false;
  }

  /**
   * Returns the string representation of the message.
   */
  @Override
  public String toString() {
    String result = type.toString();

    if (sender != null) {
      result += " " + sender.length() + " " + sender;
    } else {
      result += " " + NULL_OUTPUT.length() + " " + NULL_OUTPUT;
    }

    if (text != null) {
      result += " " + text.length() + " " + text;
    } else {
      result += " " + NULL_OUTPUT.length() + " " + NULL_OUTPUT;
    }

    return result;
  }
}