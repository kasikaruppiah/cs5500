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
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * Each instance of this class represents a single transmission by our IM clients.
 * <p>
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International
 * License. To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/. It
 * is based on work originally written by Matthew Hertz and has been adapted for use in a class
 * assignment at Northeastern University.
 *
 * @version 1.3
 */
public class MessageFactory {

  /**
   * HashMap that stores the string value of the message type and returns the message object.
   */
  private static Map<MessageType, BiFunction<String, String, Message>> messageMap;

  static {
    messageMap = new HashMap<>();
    messageMap.put(MessageType.ACKNOWLEDGE,
        (String srcName, String text) -> makeAcknowledgeMessage(text));
    messageMap.put(MessageType.QUIT, (String srcName, String text) -> makeQuitMessage(srcName));
    messageMap.put(MessageType.HELLO, MessageFactory::makeSimpleLoginMessage);
    messageMap.put(MessageType.BROADCAST, MessageFactory::makeBroadcastMessage);
    messageMap.put(MessageType.PRIVATE, MessageFactory::makePrivateMessage);
    messageMap.put(MessageType.GROUP, MessageFactory::makeGroupMessage);
    messageMap.put(MessageType.NOTIFICATION,
        (String srcName, String text) -> makeNotificationMessage(text));
    messageMap.put(MessageType.NO_ACKNOWLEDGE,
        (String srcName, String text) -> makeNoAcknowledgeMessage(text));
    messageMap
        .put(MessageType.PROFILE, (String srcName, String text) -> makeProfileMessage(srcName));
    messageMap.put(MessageType.REGISTER, MessageFactory::makeRegisterMessage);
    messageMap.put(MessageType.EDIT, MessageFactory::makeEditMessage);
    messageMap.put(MessageType.OPEN, (String srcName, String text) -> makeOpenMessage(srcName));
    messageMap.put(MessageType.NEW_GROUP, MessageFactory::makeNewGroupMessage);
    messageMap.put(MessageType.DELETE_GROUP, MessageFactory::makeDeleteGroupMessage);
    messageMap.put(MessageType.ADD_PARTICIPANT_GROUP, MessageFactory::makeAddParticipantMessage);
    messageMap
        .put(MessageType.DELETE_PARTICIPANT_GROUP, MessageFactory::makeDeleteParticipantMessage);
    messageMap.put(MessageType.SEARCH, MessageFactory::makeSearchMessage);
    messageMap.put(MessageType.GROUP_APPROVAL_EDIT,
        (String srcName, String text) -> makeEditGroupApprovalMessage(srcName));
    messageMap.put(MessageType.INVITE_PARTICIPANT_GROUP, MessageFactory::makeGroupInviteMessage);
    messageMap.put(MessageType.TAG, (String username, String text) -> makeTagMessage(username));
    messageMap.put(MessageType.UNTAG, (String username, String text) -> makeUnTagMessage(username));
  }

  /**
   * private default constructor for MessageFactory
   */
  private MessageFactory() {

  }

  /**
   * Create a new message to continue the logout process.
   *
   * @param myName The name of the client that sent the quit message.
   * @return Instance of Message that specifies the process is logging out.
   */
  public static Message makeQuitMessage(String myName) {
    return new QuitMessage(myName);
  }

  /**
   * Create a new message broadcasting an announcement to the world.
   *
   * @param myName Name of the sender of this very important missive.
   * @param text Text of the message that will be sent to all users
   * @return Instance of Message that transmits text to all logged in users.
   */
  public static Message makeBroadcastMessage(String myName, String text) {
    return new BroadcastMessage(myName, text);
  }

  /**
   * Create a new private message .
   *
   * @param srcName Name of the intended receiver of the message if incoming message, else the name
   * of the sender of the message if outgoing message
   * @param text Text of the message that will be sent to the specified user
   * @return Instance of Message that transmits text to the intended user.
   */
  public static Message makePrivateMessage(String srcName, String text) {
    return new PrivateMessage(srcName, text);
  }

  /**
   * Create a new group message .
   *
   * @param srcName Name of the intended receiver(group) of the message if incoming message, else
   * the name of the sender@group of the message if outgoing message
   * @param text Text of the message that will be sent to the specified user
   * @return Instance of Message that transmits text to the intended user.
   */
  public static Message makeGroupMessage(String srcName, String text) {
    return new GroupMessage(srcName, text);
  }

  /**
   * Creates a new Notification message, which notifies the user about an action/process with a
   * Message
   *
   * @param text Text of the message that contains notification details
   * @return Instance of Message that transmits notification to the intended user.
   */
  public static Message makeNotificationMessage(String text) {
    return new NotificationMessage(text);
  }

  /**
   * Create a new message to notify a user of an incorrect attempt to perform a particular action
   *
   * @param messageText text to display for no acknowledge message
   * @return Instance of Message that rejects the incorrect action attempt.
   */
  public static Message makeNoAcknowledgeMessage(String messageText) {
    return new NoAcknowledgeMessage(messageText);
  }

  /**
   * Create a new message to acknowledge that the user successfully performed certain action
   *
   * @param messageText Name the user was able to use to log in.
   * @return Instance of Message that acknowledges the successful login.
   */
  public static Message makeAcknowledgeMessage(String messageText) {
    return new AcknowledgeMessage(messageText);
  }

  /**
   * Create a new message when the user wants to login to the system using a username and password.
   *
   * @param myName Name of the user who wants to login.
   * @param text text of the login message in this case the password
   * @return Instance of Message specifying a new friend has just logged in.
   */
  public static Message makeSimpleLoginMessage(String myName, String text) {
    return new LoginMessage(myName, text);
  }

  /**
   * Create a new Profile Message.
   *
   * @param srcName src of the profile message
   * @return a new profile message
   */
  public static Message makeProfileMessage(String srcName) {
    return new ProfileMessage(srcName);
  }

  /**
   * Create a new Register Message.
   *
   * @param username username of user to be registered
   * @param password password of user to be registered
   * @return a new register message
   */
  public static Message makeRegisterMessage(String username, String password) {
    return new RegisterMessage(username, password);
  }

  /**
   * Create a new Edit Profile message
   *
   * @param property the property to change
   * @param toChange the value to change to
   * @return the message
   */
  public static Message makeEditMessage(String property, String toChange) {
    return new EditProfileMessage(property, toChange);
  }

  /**
   * Make open message message.
   *
   * @param username username of the user whose chat is to be opened
   * @return a new open message
   */
  public static Message makeOpenMessage(String username) {
    return new OpenMessage(username);
  }

  /**
   * Create a new group.
   *
   * @param username username of the user
   * @param groupname name of the group
   * @return a new group message
   */
  public static Message makeNewGroupMessage(String username, String groupname) {
    return new NewGroupMessage(username, groupname);
  }

  /**
   * Delete a group.
   *
   * @param username username of the user
   * @param groupname name of the group to be deleted
   * @return a new group message
   */
  public static Message makeDeleteGroupMessage(String username, String groupname) {
    return new DeleteGroupMessage(username, groupname);
  }

  /**
   * Make add participant message.
   *
   * @param groupname the groupname of the group
   * @param username the username of the user to be added
   * @return the instance of the add participant message
   */
  public static Message makeAddParticipantMessage(String groupname, String username) {
    return new AddParticipantGroupMessage(groupname, username);
  }

  /**
   * Delete participant from group.
   *
   * @param groupname name of group
   * @param username username of group
   * @return a delete participant from group message
   */
  public static Message makeDeleteParticipantMessage(String groupname, String username) {
    return new DeleteParticipantGroupMessage(groupname, username);
  }

  /**
   * Creates a search message with parameter and query
   *
   * @param search the search type
   * @param paramater of search
   * @return a new search message
   */
  public static Message makeSearchMessage(String search, String paramater) {
    return new SearchMessage(search, paramater);
  }


  /**
   * Create a edit group approval message
   *
   * @param groupname the group name to change approval
   * @return a new Edit group approval message
   */
  private static Message makeEditGroupApprovalMessage(String groupname) {
    return new EditGroupApprovalMessage(groupname);
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

  /**
   * Given a handle, name and text, return the appropriate message instance or an instance from a
   * subclass of message.
   *
   * @param handle Handle of the message to be generated.
   * @param srcName Name of the originator of the message (may be null)
   * @param text Text sent in this message (may be null)
   * @return Instance of MessageModel (or its subclasses) representing the handle, name, & text.
   */
  public static Message makeMessage(MessageType handle, String srcName, String text) {
    BiFunction<String, String, Message> func = (messageMap.getOrDefault(handle, null));
    if (func == null) {
      throw new IllegalArgumentException("Message type not supported by the system");
    }
    return func.apply(srcName, text);
  }

  public static Message makeTagMessage(String username) {
    return new TagMessage(username);
  }

  public static Message makeUnTagMessage(String username) {
    return new UnTagMessage(username);
  }
}
