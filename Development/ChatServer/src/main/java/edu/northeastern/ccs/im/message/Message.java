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

package edu.northeastern.ccs.im.message;

import edu.northeastern.ccs.im.MessageType;

/**
 * Abstract class for messages. Contains all the common functionality extracted up to a common
 * parent.
 */
public abstract class Message implements MessageInterface {

  /**
   * The string sent when a field is null.
   */
  private static final String NULL_OUTPUT = "--";
  /**
   * The handle of the message.
   */
  private MessageType msgType;

  /**
   * The first argument used in the message. This will be the sender's identifier.
   */
  private String msgSender;

  /**
   * The second argument used in the message.
   */
  private String msgText;

  /**
   * Create a new message that contains actual IM text. The type of distribution is defined by the
   * handle and we must also set the name of the message sender, message recipient, and the text to
   * send.
   *
   * @param messageType the message type
   * @param srcName     Name of the individual sending this message
   * @param text        Text of the instant message
   */
  Message(MessageType messageType, String srcName, String text) {
    msgType = messageType;
    // Save the properly formatted identifier for the user sending the
    // message.
    msgSender = srcName;
    // Save the text of the message.
    msgText = text;
  }

  /**
   * Return the name of the sender of this message.
   *
   * @return String specifying the name of the message originator.
   */
  @Override
  public String getName() {
    return msgSender;
  }

  /**
   * Return the text of this message.
   *
   * @return String equal to the text sent by this message.
   */
  @Override
  public String getText() {
    return msgText;
  }

  /**
   * Get handle of this message i.e MessageType.
   *
   * @return the message type
   */
  @Override
  public MessageType getHandle() {
    return msgType;
  }

  @Override
  public String toString() {
    String result = msgType.toString();
    if (msgSender != null) {
      result += " " + msgSender.length() + " " + msgSender;
    } else {
      result += " " + NULL_OUTPUT.length() + " " + NULL_OUTPUT;
    }
    if (msgText != null) {
      result += " " + msgText.length() + " " + msgText;
    } else {
      result += " " + NULL_OUTPUT.length() + " " + NULL_OUTPUT;
    }
    return result;
  }

  @Override
  public boolean isInitialization() {
    return false;
  }

  @Override
  public boolean terminate() {
    return false;
  }

}
