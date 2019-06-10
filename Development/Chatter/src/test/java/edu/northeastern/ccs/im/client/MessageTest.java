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

package edu.northeastern.ccs.im.client;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import edu.northeastern.ccs.im.client.exceptions.IllegalOperationException;
import edu.northeastern.ccs.im.client.message.Message;
import edu.northeastern.ccs.im.client.message.MessageFactory;
import edu.northeastern.ccs.im.client.message.MessageType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MessageTest {
  private String srcName = "JDoe";
  private String password = "password";
  private String text = "Message Text";

  @Test
  void makeHelloMessage() {
    Message message = MessageFactory.makeHelloMessage(srcName, password);

    assertTrue(message.isHello());
    assertEquals(srcName, message.getSender());
    assertEquals(password, message.getText());
  }

  @Test
  void makeMessageQuit() {
    String handle = MessageType.QUIT.toString();

    Message message = MessageFactory.makeMessage(handle, srcName, text);

    assertTrue(message.isQuit());
    assertEquals(srcName, message.getSender());
    assertEquals(text, message.getText());
  }

  @Test
  void makeMessageHello() {
    String handle = MessageType.HELLO.toString();

    Message message = MessageFactory.makeMessage(handle, srcName, text);

    assertTrue(message.isHello());
    assertEquals(srcName, message.getSender());
    assertEquals(text, message.getText());
  }

  @Test
  void makeMessageBroadcast() {
    String handle = MessageType.BROADCAST.toString();

    Message message = MessageFactory.makeMessage(handle, srcName, text);

    assertTrue(message.isBroadcast());
    assertEquals(srcName, message.getSender());
    assertEquals(text, message.getText());
  }

  @Test
  void makeMessageAcknowledge() {
    String handle = MessageType.ACKNOWLEDGE.toString();

    Message message = MessageFactory.makeMessage(handle, srcName, text);

    assertTrue(message.isAcknowledge());
    assertEquals(srcName, message.getSender());
    assertEquals(text, message.getText());
  }

  @Test
  void makeMessageNoAcknowledge() {
    String handle = MessageType.NO_ACKNOWLEDGE.toString();

    Message message = MessageFactory.makeMessage(handle, srcName, text);

    assertEquals(MessageType.NO_ACKNOWLEDGE, message.getType());
    assertEquals(srcName, message.getSender());
    assertEquals(text, message.getText());
  }

  @Test
  void makeMessageRegister() {
    String handle = MessageType.REGISTER.toString();

    Message message = MessageFactory.makeMessage(handle, srcName, text);

    assertEquals(MessageType.REGISTER, message.getType());
    assertEquals(srcName, message.getSender());
    assertEquals(text, message.getText());
  }

  @Test
  void makeMessageElse() {
    String exceptionString = "Unexpected MessageType: 'NEW'";
    String handle = "NEW";
    try {
      Message message = MessageFactory.makeMessage(handle, srcName, text);
    }
    catch(IllegalOperationException exception){
      Assertions.assertEquals(exceptionString, exception.getMessage());
    }

//    assertNull(message);
  }

  @Test
  void isAcknowledge() {
    Message message = MessageFactory.makeNoAcknowledgeMessage();

    assertFalse(message.isAcknowledge());
  }

  @Test
  void isBroadcastMessage() {
    Message message = MessageFactory.makeHelloMessage(srcName, password);

    assertTrue(message.isHello());
  }

  @Test
  void isDisplayMessage() {
    Message message = MessageFactory.makeHelloMessage(srcName, password);

    assertFalse(message.isBroadcast());
  }

  @Test
  void isInitialization() {
    Message message = MessageFactory.makeQuitMessage();

    assertFalse(message.isHello());
  }

  @Test
  void terminate() {
    Message message = MessageFactory.makeHelloMessage(srcName, password);

    assertFalse(message.isQuit());
  }

  @Test
  void toStringNotNull() {
    Message message = MessageFactory.makeBroadcastMessage(text);

    assertEquals("BCT 2 -- 12 Message Text", message.toString());
  }

  @Test
  void toStringNull() {
    Message message = MessageFactory.makeNoAcknowledgeMessage();

    assertEquals("NAK 2 -- 2 --", message.toString());
  }
}