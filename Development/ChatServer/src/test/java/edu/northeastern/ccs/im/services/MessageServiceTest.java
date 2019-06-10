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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.northeastern.ccs.im.EntityManagerHelper;
import edu.northeastern.ccs.im.models.Group;
import edu.northeastern.ccs.im.models.MessageModel;
import edu.northeastern.ccs.im.models.User;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MessageServiceTest {

  private EntityManager em = EntityManagerResource.getEntityManager();

  private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm MM/dd/yyyy");

  private String username1 = "jdoe1";
  private String username2 = "jdoe2";
  private String username3 = "jdoe3";

  private String groupname1 = "grp1";
  private String groupname2 = "grp2";

  private String text1 = "Hello";
  private String text2 = "Hi";
  private String text3 = "Hey";

  private String messageType = "PVT";
  private String groupMessageType = "GRP";

  List<MessageModel> messages = new ArrayList<>();

  List<MessageModel> groupMessages = new ArrayList<>();

  @BeforeEach
  void setUp() {
    UserService.createUser(username1, null, null, null, null);
    UserService.createUser(username2, null, null, null, null);
    UserService.createUser(username3, null, null, null, null);

    messages.add(MessageService.createMessage(username1, username2, text1, messageType));
    messages.add(MessageService.createMessage(username2, username1, text2, messageType));
    messages.add(MessageService.createMessage(username3, username2, text3, messageType));

    User user1 = UserService.findUser(username1);
    User user2 = UserService.findUser(username2);

    GroupService.createGroup(groupname1, false, user1);

    Group group1 = GroupService.findGroup(groupname1);

    GroupService.addParticipant(group1, user2);

    groupMessages.add(MessageService.createGroupMessage(user1, group1, text1, groupMessageType));

    GroupService.createGroup(groupname2, false, user1);
  }

  @AfterEach
  void tearDown() {
    EntityManagerHelper.truncateAll();

    messages = new ArrayList<>();
    groupMessages = new ArrayList<>();
  }

  @Test
  void createMessage() {
    assertEquals(4, em.createNamedQuery("Message.findAll").getResultList().size());
  }

  @Test
  void createMessageFailure() {
    MessageModel message = MessageService.createMessage(username1, "jdoe", text1, messageType);

    assertNull(message);
    assertEquals(4, em.createNamedQuery("Message.findAll").getResultList().size());
  }

  @Test
  void getUnreadMessageEmpty() {
    assertEquals("No unread messages.", MessageService.getUnreadMessage(username3));
  }

  @Test
  void getUnreadMessage() {
    assertEquals("You have 3 unread messages.\n\t$" + username1 + " : 1\n\t$" + username3 + " : 1\n\t@" + groupname1 + " : 1",
        MessageService.getUnreadMessage(username2));
  }

  @Test
  void openChatEmpty() {
    Map<String, Object> chatObj = MessageService.openChat(username3, username1);

    assertEquals("This is the very beginning of your direct message history with $" + username1,
        chatObj.get("messageText"));
    assertTrue(((List) chatObj.get("unreadMessages")).isEmpty());
  }

  @Test
  void openChat() {
    String messageText =
        "This is the very beginning of your direct message history with $" + username1
            + "\n===== New Messages =====";

    for (MessageModel message : messages.subList(0, 2)) {
      messageText += "\n" + message.getSender().getUsername() + " (" + simpleDateFormat
          .format(message.getCreatedTimeStamp()) + ") : " + message.getMessageText();
    }

    Map<String, Object> chatObj = MessageService.openChat(username2, username1);

    assertEquals(messageText, chatObj.get("messageText"));
    assertEquals(1, ((List) chatObj.get("unreadMessages")).size());
  }

  @Test
  void openGroupChatEmpty() {
    Map<String, Object> chatObj = MessageService.openGroupChat(username1, groupname2);

    assertEquals("This is the very beginning of the group message history @" + groupname2,
        chatObj.get("messageText"));
    assertTrue(((List) chatObj.get("unreadMessages")).isEmpty());
  }

  @Test
  void openGroupChatEmptyUnread() {
    Map<String, Object> chatObj = MessageService.openGroupChat(username1, groupname1);

    String messageText = "This is the very beginning of the group message history @" + groupname1;
    for (MessageModel message : groupMessages) {
      messageText += "\n" + message.getSender().getUsername() + " (" + simpleDateFormat
          .format(message.getCreatedTimeStamp()) + ") : " + message.getMessageText();
    }

    assertEquals(messageText, chatObj.get("messageText"));
    assertTrue(((List) chatObj.get("unreadMessages")).isEmpty());
  }

  @Test
  void openGroupChat() {
    Map<String, Object> chatObj = MessageService.openGroupChat(username2, groupname1);

    String messageText = "This is the very beginning of the group message history @" + groupname1
        + "\n===== New Messages =====";
    for (MessageModel message : groupMessages) {
      messageText += "\n" + message.getSender().getUsername() + " (" + simpleDateFormat
          .format(message.getCreatedTimeStamp()) + ") : " + message.getMessageText();
    }

    assertEquals(messageText, chatObj.get("messageText"));
    assertEquals(1, ((List) chatObj.get("unreadMessages")).size());
  }
}