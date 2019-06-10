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

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import edu.northeastern.ccs.im.message.Message;
import edu.northeastern.ccs.im.message.MessageFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MessageTest {

  private String fieldName = "msgType";

  @Test
  public void basicTest() {

    Message message1 = MessageFactory.makeSimpleLoginMessage("john", "def");
    assertEquals("def", message1.getText());

  }

  @Test
  public void testMessageGetter() {

    Message message1 = MessageFactory.makeMessage(MessageType.enumOf("BYE"), "bob", "def");
    assertEquals("bob", message1.getName());

  }


  @Test
  public void testMakeMessage1() {
    Message broadcast = MessageFactory.makeMessage(MessageType.enumOf("BCT"), "bob", "def");
    assertEquals("BCT", broadcast.getHandle().toString());

  }

  @Test
  public void testMakeMessage2() {
    Message hello = MessageFactory.makeMessage(MessageType.enumOf("HLO"), "bob", "def");
    assertEquals("HLO", hello.getHandle().toString());

  }

  @Test
  public void testMakeMessage3() {
    Message bye = MessageFactory.makeMessage(MessageType.enumOf("BYE"), "bob", "def");
    assertEquals("BYE", bye.getHandle().toString());
  }

  @Test
  public void testMakeMessagePVT() {
    Message pvt = MessageFactory.makeMessage(MessageType.enumOf("PVT"), "ron", "hello");
    assertEquals("PVT", pvt.getHandle().toString());
  }

  @Test
  public void testMakeMessageGRP() {
    Message grp = MessageFactory.makeMessage(MessageType.enumOf("GRP"), "group1", "a new group");
    assertEquals("GRP", grp.getHandle().toString());
  }

  @Test
  public void testMakeMessageNTF() {
    Message ntf = MessageFactory.makeMessage(MessageType.enumOf("NTF"), "john", "you have 1 new notification");
    assertEquals("NTF", ntf.getHandle().toString());
  }

  @Test
  public void testMakeMessageOPN() {
    Message message = MessageFactory.makeMessage(MessageType.enumOf("OPN"), "jdoe", null);
    assertEquals("OPN", message.getHandle().toString());
  }

  @Test
  public void testMessageTypeMethod() {
    Message broadcast = MessageFactory.makeBroadcastMessage("bob", "broadcast");
    assertEquals("BCT", broadcast.getHandle().toString());
  }


  @Test
  public void testToString1() {
    Message broadcast = MessageFactory.makeMessage(MessageType.enumOf("BCT"), "bob", "def");
    assertEquals("BCT 3 bob 3 def", broadcast.toString());
  }

  @Test
  public void testToString2() {
    Message bye = MessageFactory.makeMessage(MessageType.enumOf("BYE"), "bob", "def");
    assertEquals("BYE 3 bob 25 You have been logged out.", bye.toString());
  }

  @Test
  public void testToString3() {
    Message broadcast = MessageFactory.makeMessage(MessageType.enumOf("BCT"), null, "def");
    assertEquals("BCT 2 -- 3 def", broadcast.toString());
  }

  @Test
  public void testTerminate1() {
    Message bye = MessageFactory.makeMessage(MessageType.enumOf("BYE"), "bob", "def");
    assertTrue(bye.terminate());
  }

  @Test
  public void testTerminate2() {
    Message broadcast = MessageFactory.makeMessage(MessageType.enumOf("BCT"), "bob", "def");
    assertFalse(broadcast.terminate());
  }

  @Test
  public void testInitialization1() {
    Message bye = MessageFactory.makeMessage(MessageType.enumOf("BYE"), "bob", "def");
    assertFalse(bye.isInitialization());
  }

  @Test
  public void testInitialization2() {
    Message login = MessageFactory.makeMessage(MessageType.enumOf("HLO"), "bob", "def");
    assertTrue(login.isInitialization());
  }

  @Test
  public void testGetHandle1() {
    Message privateMsg = MessageFactory.makeMessage(MessageType.enumOf("PVT"), "jess", "private text");
    assertEquals(MessageType.PRIVATE, privateMsg.getHandle());
  }


  @Test
  public void testGetHandle2() {
    Message group = MessageFactory.makeMessage(MessageType.enumOf("GRP"), "robert", "hello");
    assertEquals(MessageType.GROUP, group.getHandle());
  }

  @Test
  public void testGetHandle3() {
    Message notif = MessageFactory.makeMessage(MessageType.enumOf("NTF"), "julia", "new notifications");
    assertEquals(MessageType.NOTIFICATION, notif.getHandle());
  }

  @Test
  public void invalidArgInMakeMessage() {
    assertThrows(IllegalArgumentException.class, () -> MessageFactory.makeMessage(MessageType.enumOf("adsf"), "", ""));
  }

  @Test
  public void makeEditMessage() {
    Message edit = MessageFactory.makeMessage(MessageType.EDIT, "", "");
    assertEquals(MessageType.EDIT, edit.getHandle());
  }

  @Test
  public void makeProfileMessage() {
    Message edit = MessageFactory.makeMessage(MessageType.PROFILE, "", "");
    assertEquals(MessageType.PROFILE, edit.getHandle());
  }

  @Test
  public void makeSearchMessage() {
    Message search = MessageFactory.makeMessage(MessageType.SEARCH, "username", "c");
    assertEquals(MessageType.SEARCH, search.getHandle());
  }
}