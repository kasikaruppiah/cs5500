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

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import edu.northeastern.ccs.im.EntityManagerHelper;
import edu.northeastern.ccs.im.MessageType;
import edu.northeastern.ccs.im.models.Group;
import edu.northeastern.ccs.im.models.User;
import edu.northeastern.ccs.im.server.ClientRunnable;
import edu.northeastern.ccs.im.services.GroupService;
import edu.northeastern.ccs.im.services.MessageService;
import edu.northeastern.ccs.im.services.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OpenMessageTest {

  private String username1 = "jdoe1";
  private String username2 = "jdoe2";
  private String username3 = "jdoe3";
  private String username4 = "jdoe4";

  private String groupname1 = "grp1";
  private String groupname2 = "grp2";

  private String text1 = "Hello";
  private String text2 = "Hi";

  private String messageType = "PVT";
  private String groupMessageType = "GRP";

  @BeforeEach
  void setUp() {
    UserService.createUser(username1, null, null, null, null);
    UserService.createUser(username2, null, null, null, null);
    UserService.createUser(username3, null, null, null, null);

    MessageService.createMessage(username1, username2, text1, messageType);
    MessageService.createMessage(username2, username1, text2, messageType);

    User user1 = UserService.findUser(username1);
    User user2 = UserService.findUser(username2);

    GroupService.createGroup(groupname1, false, user1);

    Group group1 = GroupService.findGroup(groupname1);

    GroupService.addParticipant(group1, user2);

    MessageService.createGroupMessage(user1, group1, text1, groupMessageType);
  }

  @AfterEach
  void tearDown() {
    EntityManagerHelper.truncateAll();
  }

  @Test
  void dispatchPrivateValid() {
    ClientRunnable clientRunnable = mock(ClientRunnable.class);
    when(clientRunnable.getName()).thenReturn(username1);

    assertTrue(MessageFactory.makeMessage(MessageType.OPEN, "$" + username2, null)
        .dispatch(clientRunnable));
  }

  @Test
  void dispatchPrivateInValid() {
    ClientRunnable clientRunnable = mock(ClientRunnable.class);
    when(clientRunnable.getName()).thenReturn(username1);

    assertTrue(MessageFactory.makeMessage(MessageType.OPEN, "$" + username4, null)
        .dispatch(clientRunnable));
  }

  @Test
  void dispatchGroupValid() {
    ClientRunnable clientRunnable = mock(ClientRunnable.class);
    when(clientRunnable.getName()).thenReturn(username2);

    assertTrue(MessageFactory.makeMessage(MessageType.OPEN, "@" + groupname1, null)
        .dispatch(clientRunnable));
  }

  @Test
  void dispatchGroupInValidGroup() {
    ClientRunnable clientRunnable = mock(ClientRunnable.class);
    when(clientRunnable.getName()).thenReturn(username2);

    assertTrue(MessageFactory.makeMessage(MessageType.OPEN, "@" + groupname2, null)
        .dispatch(clientRunnable));
  }

  @Test
  void dispatchGroupInValid() {
    ClientRunnable clientRunnable = mock(ClientRunnable.class);
    when(clientRunnable.getName()).thenReturn(username3);

    assertTrue(MessageFactory.makeMessage(MessageType.OPEN, "@" + groupname1, null)
        .dispatch(clientRunnable));
  }
}