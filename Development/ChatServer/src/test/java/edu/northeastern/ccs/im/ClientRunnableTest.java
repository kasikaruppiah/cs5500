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

import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import edu.northeastern.ccs.im.message.Message;
import edu.northeastern.ccs.im.message.MessageFactory;
import edu.northeastern.ccs.im.server.ClientRunnable;
import edu.northeastern.ccs.im.services.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ClientRunnableTest {


  @Mock
  ClientRunnable clientRunnable;
  private ClientRunnable clientRunnable1;
  NetworkConnection networkConnection;

  @BeforeEach
  public void setUp() throws IllegalAccessException, InvocationTargetException, InstantiationException {
    clientRunnable = Mockito.mock(ClientRunnable.class);
    networkConnection = Mockito.mock(NetworkConnection.class);
    Constructor[] constructor = ClientRunnable.class.getDeclaredConstructors();
    constructor[0].setAccessible(true);
    Constructor<ClientRunnable> con = constructor[0];
    con.setAccessible(true);
    clientRunnable1 = con.newInstance(networkConnection);
  }

  @Test
  public void testSetNameGetName() {
    clientRunnable1.setName("Client Runnable 1");
    assertEquals("Client Runnable 1", clientRunnable1.getName());
  }


  @Test
  public void testSetUsernameNull() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
    Method setUserNameMethod = ClientRunnable.class.getDeclaredMethod("setUserName", String.class);
    setUserNameMethod.setAccessible(true);
    String username = null;
    assertEquals(setUserNameMethod.invoke(clientRunnable1, username), false);

  }

  @Test
  public void testSetUsername() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
    Method setUserNameMethod = ClientRunnable.class.getDeclaredMethod("setUserName", String.class);
    setUserNameMethod.setAccessible(true);
    assertEquals(setUserNameMethod.invoke(clientRunnable1, "abc"), true);

  }

  @Test
  public void testIsInitialized() {
    assertFalse(clientRunnable1.isInitialized());
  }

  @Test
  public void testMessageChecksNull() throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {
    Message message = MessageFactory.makeSimpleLoginMessage(null, "Hello there!");
    Method messageChecksMethod = ClientRunnable.class.getDeclaredMethod("messageChecks", Message.class);
    messageChecksMethod.setAccessible(true);
    clientRunnable1.setName(null);
    assertEquals(messageChecksMethod.invoke(clientRunnable1, message), false);

  }

  @Test
  public void testMessageChecks() throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {
    Message message = MessageFactory.makeSimpleLoginMessage("a", "Hello there!");
    Method messageChecksMethod = ClientRunnable.class.getDeclaredMethod("messageChecks", Message.class);
    messageChecksMethod.setAccessible(true);
    clientRunnable1.setName("a");
    assertEquals(messageChecksMethod.invoke(clientRunnable1, message), true);

  }

  @Test
  public void testSendMessage() throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {
    Message message = MessageFactory.makeSimpleLoginMessage("a", "Hello there!");
    Method sendMessageMethod = ClientRunnable.class.getDeclaredMethod("sendMessage", Message.class);
    sendMessageMethod.setAccessible(true);
    assertEquals(sendMessageMethod.invoke(clientRunnable1, message), false);
  }

  @Test
  public void testEnqueueMessage() throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException, NoSuchFieldException {
    Message message = MessageFactory.makeSimpleLoginMessage("a", "Hello there!");
    Method enqueueMessageMethod = ClientRunnable.class.getDeclaredMethod("enqueueMessage", Message.class);
    enqueueMessageMethod.setAccessible(true);
    enqueueMessageMethod.invoke(clientRunnable1, message);
    Message message2 = MessageFactory.makeSimpleLoginMessage("b", "Hello, I am b.");
    Method enqueueMessageMethod2 = ClientRunnable.class.getDeclaredMethod("enqueueMessage", Message.class);
    enqueueMessageMethod2.setAccessible(true);
    enqueueMessageMethod2.invoke(clientRunnable1, message2);
    System.out.println("okay");

  }

  @Test
  public void testOutgoingMessages() {
    Message message = null;
    Method enqueueMessageMethod = null;
    Message message2 = null;
    Method enqueueMessageMethod2 = null;
    Method handleOutgoingMessagesMethod = null;

    try {
      message = MessageFactory.makeSimpleLoginMessage("a", "Hello there!");
      enqueueMessageMethod = ClientRunnable.class.getDeclaredMethod("enqueueMessage", Message.class);
      enqueueMessageMethod.setAccessible(true);
      message2 = MessageFactory.makeSimpleLoginMessage("b", "Hello, I am b.");
      enqueueMessageMethod2 = ClientRunnable.class.getDeclaredMethod("enqueueMessage", Message.class);
      enqueueMessageMethod2.setAccessible(true);
      enqueueMessageMethod.invoke(clientRunnable1, message);
      enqueueMessageMethod2.invoke(clientRunnable1, message2);
      handleOutgoingMessagesMethod = ClientRunnable.class.getDeclaredMethod("handleOutgoingMessages");
      handleOutgoingMessagesMethod.setAccessible(true);
      handleOutgoingMessagesMethod.invoke(clientRunnable1);
    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
      fail();
    }
  }

  @AfterAll
  public void truncateTable() {
    EntityManagerHelper.truncateUsers();
    EntityManagerHelper.truncateGroups();
    EntityManagerHelper.truncateMessages();
  }
}