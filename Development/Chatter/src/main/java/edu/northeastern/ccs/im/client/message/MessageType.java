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

/**
 * The enum Message type.
 */
public enum MessageType {
  /**
   * Message sent by the user attempting to login using a specified username.
   */
  HELLO("HLO"),

  /**
   * Message sent by the server acknowledging a successful log in.
   */
  ACKNOWLEDGE("ACK"),

  /**
   * Message sent by the server rejecting a login attempt.
   */
  NO_ACKNOWLEDGE("NAK"),

  /**
   * Message sent by the user to start the logging out process and sent by the server once the
   * logout process completes.
   */
  QUIT("BYE"),

  /**
   * Message whose contents is broadcast to all connected users.
   */
  BROADCAST("BCT"),

  /**
   * Message whose contents is sent to only a particular user
   */
  PRIVATE("PVT"),

  /**
   * Message whose contents are sent to the users part of the given group.
   */
  GROUP("GRP"),

  /**
   * Message to get unread message count
   */
  NOTIFICATION("NTF"),

  /**
   * Message to get the profile of the user
   */
  PROFILE("PRO"),
  /**
   * Message for editing user profile
   */
  EDIT("EDT"),
  /**
   * Message for registering user
   */
  REGISTER("REG"),
  /**
   * Message to get retrieve a private message
   */
  OPEN("OPN"),
  /**
   * Message to create a new group
   */
  NEW_GROUP("NGR"),
  /**
   * Message to delete a group
   */
  DELETE_GROUP("DGR"),
  /**
   * Message to add user to a group
   */
  ADD_PARTICIPANT_GROUP("APG"),
  /**
   * Message to delete a participant from the group
   */
  DELETE_PARTICIPANT_GROUP("DPG"),
  /**
   * Message to search a user of a group
   */
  SEARCH("SRC"),
  /**
   * Edit the group approval
   */
  GROUP_PROFILE_APPROVAL("GAE"),
  /**
   * Invite request to add participant to the group
   */
  INVITE_PARTICIPANT_GROUP("INV"),
  /**
   * Follow a user
   */
  TAG("TAG"),
  /**
   * Unfollow a user
   */
  UNTAG("UTG");

  /**
   * Store the short name of this message type.
   */
  private String type;

  /**
   * Define the message type and specify its short name.
   *
   * @param type Short name of this message type, as a String.
   */
  MessageType(String type) {
    this.type = type;
  }

  /**
   * Return a representation of this Message as a String.
   *
   * @return Three letter abbreviation for this type of message.
   */
  @Override
  public String toString() {
    return type;
  }
}