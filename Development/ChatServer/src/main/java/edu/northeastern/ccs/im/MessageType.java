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

package edu.northeastern.ccs.im;


/**
 * Enumeration for the different types of messages.
 *
 * @author Maria Jump
 */
public enum MessageType {
  /**
   * MessageModel sent by the user attempting to login using a specified username.
   */
  HELLO("HLO"),
  /**
   * MessageFactory sent by the server acknowledging a successful log in.
   */
  ACKNOWLEDGE("ACK"),
  /**
   * MessageFactory sent by the server rejecting a login attempt.
   */
  NO_ACKNOWLEDGE("NAK"),
  /**
   * MessageFactory sent by the user to start the logging out process and sent by the server once
   * the logout process completes.
   */
  QUIT("BYE"),
  /**
   * MessageModel whose contents is broadcast to all connected users.
   */
  BROADCAST("BCT"),
  /**
   * MessageModel sent by the user whose contents are private to the specified user.
   */
  PRIVATE("PVT"),
  /**
   * MessageModel sent by the user whose contents are private to the specified group(incl.
   * participant members and groups).
   */
  GROUP("GRP"),
  /**
   * MessageModel whose contents are notifications for the specified user.
   */
  NOTIFICATION("NTF"),
  /**
   * Profile message to ask for profile.
   */
  PROFILE("PRO"),
  /**
   * Edit message to edit the user information.
   */
  EDIT("EDT"),
  /**
   * Register message to ask for user registration.
   */
  REGISTER("REG"),
  /**
   * Open message to retrieve a chat.
   */
  OPEN("OPN"),
  /**
   * Create a new group
   */
  NEW_GROUP("NGR"),
  /**
   * Delete a group
   */
  DELETE_GROUP("DGR"),
  /**
   * Message to add user to a group
   */
  ADD_PARTICIPANT_GROUP("APG"),
  /**
   * Message to delete user from a group
   */
  DELETE_PARTICIPANT_GROUP("DPG"),
  /**
   * Search Something with the system.
   */
  SEARCH("SRC"),
  /**
   * Edit the group's approval setting.
   */
  GROUP_APPROVAL_EDIT("GAE"),
  /**
   * Invite request to add participant to the group
   */
  INVITE_PARTICIPANT_GROUP("INV"),
  TAG("TAG"),
  UNTAG("UTG");
  /**
   * Store the short name of this message type.
   */
  private String abbreviation;

  /**
   * Define the message type and specify its short name.
   *
   * @param abbrev Short name of this message type, as a String.
   */
  MessageType(String abbrev) {
    abbreviation = abbrev;
  }

  /**
   * Return a representation of this MessageModel as a String.
   *
   * @return Three letter abbreviation for this type of message.
   */
  @Override
  public String toString() {
    return abbreviation;
  }
  public static MessageType enumOf(String abbr){
    for(MessageType v : values()){
      if( v.toString().equals(abbr)){
        return v;
      }
    }
    return null;
  }
}
