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
 * Enumeration for strings to display on the user interface.
 */
public enum MessageStrings {

  /**
   * Name of the bot that sends the acknowledgement notification.
   */
  CHAT_BOT("PrattleBot"),

  /**
   * Acknowledge message for adding a user to a group.
   */
  ADD_USER_ACK("User was succesfully added to the group"),

  /**
   * No acknowledge message for adding a user to a group.
   */
  ADD_USER_NOACK("Cannot add user to the group. Please send invite request to the Moderator"),

  /**
   * Acknowledge message for deleting a user from a group.
   */
  DELETE_PARTICIPANT_ACK("User was successfully deleted from the group"),

  /**
   * No acknowledge message for deleting a user from a group when not in the group.
   */
  DELETE_PARTICIPANT_NOACK("Cannot delete user from the group. User doesn't exist in the group."),

  /**
   * Acknowledge message for deleting a group.
   */
  DELETE_GROUP_ACK("Group deleted successfully"),

  /**
   * No acknowledge message for invalid command for deleting a group.
   */
  DELETE_GROUP_INVALID_COMMAND("There is some problem in deleting the group. Please try again."),

  /**
   * No acknowledge message when the current user is not the moderator of the group.
   */
  GROUP_MODERATOR_ACTION_NOACK("You are not the group moderator. Please ask the group moderator to process your request."),

  /**
   * Acknowledge message for creating a new group.
   */
  CREATE_GROUP_ACK("Group created successfully"),

  /**
   * No acknowledge message for creating a new group.
   */
  CREATE_GROUP_NOACK("Group name already taken. Please try again with another group name."),

  /**
   * Acknowledge message when user successfully logs out of the system.
   */
  QUIT_ACK("You have been logged out."),

  /**
   * No acknowledge message for sending group messages.
   */
  GROUP_MESSAGE_NOACK("Please check if you are a part of the group"),

  /**
   * No acknowledge message for register user.
   */
  REGISTER_USER_NOACK("Unable to register. Please try again."),
  /**
   * No acknowledge message authentication of user.
   */
  CHECK_AUTHENTICATION_NOACK("Invalid username or password. Please try again."),

  /**
   * No acknowledge message as group doesn't exist.
   */
  NO_GROUP_EXIST("This group doesn't exist. Please try again."),

  /**
   * No acknowledge message as user doesn't exist.
   */
  NO_USER_EXIST("This user doesn't exist. Please try again."),

  /**
   * Acknowledge message for changing group's approval settings.
   */
  EDIT_GROUP_APPROVAL_ACK("Changed the groups approval."),

  /**
   * No acknowledge for changing group's approval settings.
   */
  EDIT_GROUP_APPROVAL_NOACK("Some problem Changing the groups approval settings. Please try again."),

  /**
   * No ack message when the sender of the message doesn't exist in the system.
   */
  NO_MESSAGE_SENDER("No sender with the given username. Please try again."),

  /**
   * No ack message when the receiver of the message doesn't exist in the system.
   */
  NO_MESSAGE_RECEIVER("No reciever with the given username. Please try again."),

  /**
   * No ack message when user enters an invalid query parameter.
   */
  INVALID_QUERY_PARAM("Cannot recognize query parameter. Please try again."),

  /**
   * No ack message when an invalid command is entered by the user.
   */
  INVALID_COMMAND_NOACK("Command not formed properly. Please take help of the documentation for a correct command."),

  /**
   * No ack message when the User already exists in group.
   */
  USER_EXIST_IN_GROUP("The user already exists in the group");


  private String messageString;

  /**
   * Define the message string and specify its string to display on the UI.
   *
   * @param message message to display on the UI.
   */
  MessageStrings(String message) {
    this.messageString = message;
  }

  /**
   * Return a representation of this message string.
   *
   * @return message to display on the UI.
   */
  @Override
  public String toString() {
    return messageString;
  }
}
