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

package edu.northeastern.ccs.im.services;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import edu.northeastern.ccs.im.ChatLogger;
import edu.northeastern.ccs.im.models.Group;
import edu.northeastern.ccs.im.models.GroupParticipation;
import edu.northeastern.ccs.im.models.GroupUnseenTemporary;
import edu.northeastern.ccs.im.models.MessageModel;
import edu.northeastern.ccs.im.models.User;

/**
 * This class represents all the operations that can be performed on the message class.
 */
public class MessageService {

  private static EntityManager em = EntityManagerResource.getEntityManager();
  private static final String PRIVATE = "private";
  private static final String GROUP = "group";

  /**
   * Private constructor to prohibit creating objects of this class.
   */
  private MessageService() {
  }

  /**
   * Create an a private message into the database.
   *
   * @param senderUsername   username of the sender
   * @param receiverUsername username of the receiver
   * @param text             the text of the message
   * @param messageType      the type of the message
   * @return the created message object.
   */
  public static MessageModel createMessage(String senderUsername, String receiverUsername,
                                           String text, String messageType) {
    User sender = UserService.findUser(senderUsername);
    User receiver = UserService.findUser(receiverUsername);

    if (receiver != null) {
      MessageModel message = new MessageModel(sender, receiver, null, text, messageType);

      EntityManagerResource.persist(message);

      return message;
    } else {
      return null;
    }
  }

  /**
   * Create and insert a group message into the database.
   *
   * @param sender      the sender of the message
   * @param receiver    the name of the group
   * @param text        the text of the message
   * @param messageType the type of the message
   * @return the created message object.
   */
  public static MessageModel createGroupMessage(User sender, Group receiver, String text,
                                                String messageType) {

    MessageModel message = new MessageModel(sender, null, receiver, text, messageType);

    EntityManagerResource.persist(message);

    for (GroupParticipation participant : receiver.getParticipants()) {
      if (!participant.getUser().equals(sender)) {
        GroupUnseenTemporaryService
                .createUnseenGroupMessage(receiver.getId(), participant.getUser().getId(),
                        message.getId());
      }
    }

    return message;
  }

  /**
   * Returns the list of all the unread messages for the user with username username.
   *
   * @param username the username of the user
   * @return the unread messages of the user.
   */
  public static String getUnreadMessage(String username) {
    Map<String, List<Object[]>> results = new HashMap<>();

    String unreadMessageText = "";

    Query query = em.createQuery(
            "SELECT message.sender.username, COUNT(message) FROM MessageModel message WHERE message.receiver.username = :receiverUsername AND message.seenTimeStamp IS NULL GROUP BY message.sender.username ORDER BY COUNT(message) DESC, message.sender.username");
    query.setParameter("receiverUsername", username);

    results.put(PRIVATE, query.getResultList());

    query = em.createQuery(
            "SELECT grp.groupname, COUNT(*) FROM GroupUnseenTemporary unseen, Group grp, User user WHERE unseen.groupId = grp.id AND unseen.receiverId = user.id AND user.username = :username GROUP BY grp.groupname ORDER BY COUNT(*) DESC, grp.groupname");
    query.setParameter("username", username);

    results.put(GROUP, query.getResultList());

    if (results.get(PRIVATE).isEmpty() && results.get(GROUP).isEmpty()) {
      unreadMessageText = "No unread messages.";
    } else {
      BigInteger unreadCount = BigInteger.valueOf(0);

      List<String> senderUnreadTexts = new ArrayList<>();

      for (Object[] result : results.get(PRIVATE)) {
        BigInteger senderUnreadCount = BigInteger.valueOf((Long) result[1]);

        senderUnreadTexts.add("\t$" + result[0] + " : " + senderUnreadCount);

        unreadCount = unreadCount.add(senderUnreadCount);
      }

      for (Object[] result : results.get(GROUP)) {
        BigInteger senderUnreadCount = BigInteger.valueOf((Long) result[1]);

        senderUnreadTexts.add("\t@" + result[0] + " : " + senderUnreadCount);

        unreadCount = unreadCount.add(senderUnreadCount);
      }

      unreadMessageText =
              "You have " + unreadCount + " unread messages.\n" + String.join("\n", senderUnreadTexts);
    }

    return unreadMessageText;
  }

  /**
   * Returns the chat between two users.
   *
   * @param senderUsername   the sender of the message
   * @param receiverUsername the receiver of the message.
   * @return the chat between the two users.
   */
  public static Map<String, Object> openChat(String senderUsername, String receiverUsername) {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm MM/dd/yyyy");

    String messageText =
            "This is the very beginning of your direct message history with $" + receiverUsername;
    List<MessageModel> unreadMessages = new ArrayList<>();

    try {
      Query query = em.createQuery(
              "SELECT message FROM MessageModel message WHERE message.messageType = 'PVT' AND (message.sender.username = :senderUsername AND message.receiver.username = :receiverUsername) OR (message.sender.username = :receiverUsername AND message.receiver.username = :senderUsername) ORDER BY message.id");
      query.setParameter("senderUsername", senderUsername);
      query.setParameter("receiverUsername", receiverUsername);

      List<MessageModel> messages = query.getResultList();

      if (!messages.isEmpty()) {
        List<String> messageTexts = new ArrayList<>();
        Boolean flag = true;

        for (MessageModel message : messages) {
          if (message.getSeenTimeStamp() == null && message.getReceiver().getUsername()
                  .equals(senderUsername)) {
            if (flag) {
              messageTexts.add("===== New Messages =====");
              flag = false;
            }
            unreadMessages.add(message);
          }

          messageTexts.add(message.getSender().getUsername() + " (" + simpleDateFormat
                  .format(message.getCreatedTimeStamp()) + ") : " + message.getMessageText());
        }

        messageText += "\n" + String.join("\n", messageTexts);
      }
    } catch (Exception e) {
      ChatLogger.error(e.getMessage());
    }

    Map<String, Object> returnObj = new HashMap<>();
    returnObj.put("messageText", messageText);
    returnObj.put("unreadMessages", unreadMessages);

    return returnObj;
  }

  /**
   * Find and return all the chat of the user with username name on groupname gorupname.
   *
   * @param name      the name of the user
   * @param groupname the name of the group
   * @return the string chat of the user on the groupname
   */
  public static Map<String, Object> openGroupChat(String name, String groupname) {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm MM/dd/yyyy");

    Date date = new Date();

    String messageText = "This is the very beginning of the group message history @" + groupname;
    List<GroupUnseenTemporary> unreadMessages = new ArrayList<>();

    try {
      Query query = em.createQuery(
              "SELECT unseen FROM GroupUnseenTemporary unseen, User user, MessageModel message WHERE unseen.receiverId = user.id AND unseen.messageId = message.id AND user.username = :username AND message.createdTimeStamp < :date ORDER BY unseen.id");
      query.setParameter("username", name);
      query.setParameter("date", date);

      unreadMessages = query.getResultList();

      Long unseenId = null;
      if (!unreadMessages.isEmpty()) {
        unseenId = unreadMessages.get(0).getMessageId();
      }

      query = em.createQuery(
              "SELECT message FROM MessageModel message WHERE message.messageType = 'GRP' AND message.group.groupname = :groupname AND message.createdTimeStamp < :date ORDER BY message.id");
      query.setParameter("groupname", groupname);
      query.setParameter("date", date);

      List<MessageModel> messages = query.getResultList();

      if (!messages.isEmpty()) {
        List<String> messageTexts = new ArrayList<>();

        for (MessageModel message : messages) {
          if (unseenId != null && message.getId() == unseenId) {
            messageTexts.add("===== New Messages =====");
          }

          messageTexts.add(message.getSender().getUsername() + " (" + simpleDateFormat
                  .format(message.getCreatedTimeStamp()) + ") : " + message.getMessageText());
        }

        messageText += "\n" + String.join("\n", messageTexts);
      }
    } catch (Exception e) {
      ChatLogger.error(e.getMessage());
    }

    Map<String, Object> returnObj = new HashMap<>();
    returnObj.put("messageText", messageText);
    returnObj.put("unreadMessages", unreadMessages);

    return returnObj;
  }

  /**
   * Creates a message entry tto the database
   *
   * @param sender      the sender of the message
   * @param receiver    the receiver of the message
   * @param text        the text of the message
   * @param messageType the message type (private or group)
   * @return true if operation successful
   */
  public static boolean createMessage(User sender, User receiver, String text, String messageType) {
    MessageModel message = new MessageModel(sender, receiver, null, text, messageType);
    try {
      EntityManagerResource.persist(message);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * This method is for deleting all the messages for a group from the database.
   *
   * @param group group
   * @return true if successfully deleted, false otherwise
   */
  public static boolean deleteMessagesForGroup(Group group) {
    Query query = em.createQuery("SELECT message FROM MessageModel message WHERE group.id = :group");
    query.setParameter(GROUP, group.getId());
    List<MessageModel> messages = query.getResultList();
    EntityManagerResource.removeAll(messages);
    return true;
  }
}