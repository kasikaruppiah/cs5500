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

import static edu.northeastern.ccs.im.client.message.MessageType.ACKNOWLEDGE;
import static edu.northeastern.ccs.im.client.message.MessageType.ADD_PARTICIPANT_GROUP;
import static edu.northeastern.ccs.im.client.message.MessageType.BROADCAST;
import static edu.northeastern.ccs.im.client.message.MessageType.DELETE_GROUP;
import static edu.northeastern.ccs.im.client.message.MessageType.DELETE_PARTICIPANT_GROUP;
import static edu.northeastern.ccs.im.client.message.MessageType.EDIT;
import static edu.northeastern.ccs.im.client.message.MessageType.GROUP;
import static edu.northeastern.ccs.im.client.message.MessageType.GROUP_PROFILE_APPROVAL;
import static edu.northeastern.ccs.im.client.message.MessageType.HELLO;
import static edu.northeastern.ccs.im.client.message.MessageType.NEW_GROUP;
import static edu.northeastern.ccs.im.client.message.MessageType.NOTIFICATION;
import static edu.northeastern.ccs.im.client.message.MessageType.NO_ACKNOWLEDGE;
import static edu.northeastern.ccs.im.client.message.MessageType.OPEN;
import static edu.northeastern.ccs.im.client.message.MessageType.PRIVATE;
import static edu.northeastern.ccs.im.client.message.MessageType.PROFILE;
import static edu.northeastern.ccs.im.client.message.MessageType.QUIT;
import static edu.northeastern.ccs.im.client.message.MessageType.REGISTER;
import static edu.northeastern.ccs.im.client.message.MessageType.SEARCH;
import static edu.northeastern.ccs.im.client.message.MessageType.TAG;
import static edu.northeastern.ccs.im.client.message.MessageType.UNTAG;

import edu.northeastern.ccs.im.client.exceptions.IllegalOperationException;

/**
 * Message Factory
 */
public class MessageFactory {

  private MessageFactory() {
  }

  /**
   * Create a hello message with the given type, username and password
   *
   * @param username senders identification
   * @param password password of the sender
   * @return the message
   */
  public static Message makeHelloMessage(String username, String password) {
    return new HelloMessage(username, password);
  }

  /**
   * Create a acknowledge message
   *
   * @return the message
   */
  public static Message makeAcknowledgeMessage() {
    return new AcknowledgeMessage();
  }

  /**
   * Create a no acknowledgement message
   *
   * @return the message
   */
  public static Message makeNoAcknowledgeMessage() {
    return new NoAcknowledgeMessage();
  }

  /**
   * Create a quit message
   *
   * @return the message
   */
  public static Message makeQuitMessage() {
    return new QuitMessage();
  }

  /**
   * Create a broadcast message with the given text
   *
   * @param text text of the message
   * @return the message
   */
  public static Message makeBroadcastMessage(String text) {
    return new BroadcastMessage(text);
  }

  /**
   * Create a private message with the given receiver and text
   *
   * @param receiver senders identification
   * @param text text of the message
   * @return the message
   */
  public static Message makePrivateMessage(String receiver, String text) {
    return new PrivateMessage(receiver, text);
  }

  /**
   * Create a notification message
   *
   * @return the message
   */
  public static Message makeNotificationMessage() {
    return new NotificationMessage();
  }

  /**
   * Create a profile message with the given type and sender
   *
   * @param username senders identification
   * @return the message
   */
  public static Message makeProfileMessage(String username) {
    return new ProfileMessage(username);
  }

  /**
   * Create a profile message with the given type and sender
   *
   * @param username senders identification
   * @param password the password
   * @return the message
   */
  public static Message makeRegisterMessage(String username, String password) {
    return new RegisterMessage(username, password);
  }

  /**
   * Create a Open Message with the given username
   *
   * @param username chat receiverid
   * @return the message
   */
  public static Message makeOpenMessage(String username) {
    return new OpenMessage(username);
  }

  /**
   * Create a new edit message
   *
   * @param parameter the parameter to edit
   * @param newValue the new value of the parameter
   * @return the message
   */
  public static Message makeEditMessage(String parameter, String newValue) {
    return new EditMessage(EDIT, parameter, newValue);
  }

  /**
   * Create a new group
   *
   * @param username username of group creator
   * @param groupname name of the group
   * @return the message
   */
  public static Message makeCreateNewGroup(String username, String groupname) {
    return new NewGroupMessage(NEW_GROUP, username, groupname);
  }

  /**
   * Delete a new group
   *
   * @param username username of group creator
   * @param groupname name of the group
   * @return the message
   */
  public static Message makeDeleteNewGroupMessage(String username, String groupname) {
    return new DeleteGroupMessage(DELETE_GROUP, username, groupname);
  }

  /**
   * Add participant to a group
   *
   * @param groupname name of the group
   * @param username username of user to add to the group
   * @return the message
   */
  public static Message makeAddParticipantGroupMessage(String groupname, String username) {
    return new AddParticipantGroupMessage(ADD_PARTICIPANT_GROUP, groupname, username);
  }

  /**
   * Delete participant from a group
   *
   * @param groupname name of the group
   * @param username username of user to delete from the group
   * @return the message
   */
  public static Message makeDeleteParticipantGroupMessage(String groupname, String username) {
    return new DeleteParticipantGroupMessage(DELETE_PARTICIPANT_GROUP, groupname, username);
  }

  /**
   * Create a new search message
   *
   * @param parameter the parameter to searhc
   * @param query search query
   * @return the message
   */
  public static Message makeSearchMessage(String parameter, String query) {
    return new SearchMessage(SEARCH, parameter, query);
  }


  /**
   * Make group message message.
   *
   * @param receiver the receiver
   * @param text the text
   * @return the message
   */
  public static Message makeGroupMessage(String receiver, String text) {
    return new GroupMessage(receiver, text);
  }


  /**
   * Make group message message.
   *
   * @param receiver the receiver
   * @return the message
   */
  public static Message makeEditGroupApprovalMessage(String receiver) {
    return new EditGroupApprovalMessage(GROUP_PROFILE_APPROVAL, receiver);
  }

  /**
   * Create a Group Invite message
   *
   * @param groupname the name for the invited group
   * @param username the name of the user to be invited to the group
   * @return a new group invite message
   */
  public static Message makeGroupInviteMessage(String groupname, String username) {
    return new GroupInviteMessage(groupname, username);
  }

  public static Message makeTagMessage(String username) {
    return new TagMessage(username);
  }

  public static Message makeUnTagMessage(String username) {
    return new UnTagMessage(username);
  }

  /**
   * Create a message with the given type, sender and text
   *
   * @param type handle for the message
   * @param sender senders identification
   * @param text text of the message
   * @return the message
   */
  public static Message makeMessage(String type, String sender, String text) {
    if (type.equals(HELLO.toString())) {
      return new HelloMessage(HELLO, sender, text);
    } else if (type.equals(ACKNOWLEDGE.toString())) {
      return new AcknowledgeMessage(ACKNOWLEDGE, sender, text);
    } else if (type.equals(NO_ACKNOWLEDGE.toString())) {
      return new NoAcknowledgeMessage(NO_ACKNOWLEDGE, sender, text);
    } else if (type.equals(QUIT.toString())) {
      return new QuitMessage(QUIT, sender, text);
    } else if (type.equals(BROADCAST.toString())) {
      return new BroadcastMessage(BROADCAST, sender, text);
    } else if (type.equals(PRIVATE.toString())) {
      return new PrivateMessage(PRIVATE, sender, text);
    } else if (type.equals(NOTIFICATION.toString())) {
      return new NotificationMessage(NOTIFICATION, sender, text);
    } else if (type.equals(PROFILE.toString())) {
      return new ProfileMessage(PROFILE, sender, text);
    } else if (type.equals(REGISTER.toString())) {
      return new RegisterMessage(REGISTER, sender, text);
    } else if (type.equals(OPEN.toString())) {
      return new OpenMessage(OPEN, sender, text);
    } else if (type.equals(MessageType.EDIT.toString())) {
      return new EditMessage(REGISTER, sender, text);
    } else if (type.equals(MessageType.NEW_GROUP.toString())) {
      return new NewGroupMessage(NEW_GROUP, sender, text);
    } else if (type.equals(MessageType.DELETE_GROUP.toString())) {
      return new DeleteGroupMessage(DELETE_GROUP, sender, text);
    } else if (type.equals(MessageType.ADD_PARTICIPANT_GROUP.toString())) {
      return new AddParticipantGroupMessage(ADD_PARTICIPANT_GROUP, sender, text);
    } else if (type.equals(MessageType.DELETE_PARTICIPANT_GROUP.toString())) {
      return new DeleteParticipantGroupMessage(DELETE_PARTICIPANT_GROUP, sender, text);
    } else if (type.equals(GROUP.toString())) {
      return new GroupMessage(GROUP, sender, text);
    } else if (type.equals(TAG.toString())) {
      return new TagMessage(TAG, sender, text);
    } else if (type.equals(UNTAG.toString())) {
      return new TagMessage(UNTAG, sender, text);
    } else {
      throw new IllegalOperationException("Unexpected MessageType: '" + type + "'");
    }
  }
}
