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

import edu.northeastern.ccs.im.message.Message;
import edu.northeastern.ccs.im.message.MessageFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class NetworkConnectionIteratorTest {

  SocketChannel soc;
  Message m1;
  Message m2;
  Message m3;
  Thread thread;

  public void connectToServer() {
    try {
      soc = SocketChannel.open();
      soc.configureBlocking(true);

      thread = new Thread(() -> {
        try {
          soc.connect(new InetSocketAddress(1235));
        } catch (IOException e) {
          fail();
        }

        System.out.println("Connected");
      });
      thread.start();

    } catch (IOException e) {
      fail();
    }
  }


  @Test
  public void basicCreationTestClientSendsMessages() {
    try {
      ServerSocketChannel serverSocket = ServerSocketChannel.open();

      serverSocket.configureBlocking(true);
      serverSocket.socket().bind(new InetSocketAddress(1235));
      connectToServer();
      SocketChannel sc = serverSocket.accept();

      System.out.println("after accepting connection");

      m1 = MessageFactory.makeBroadcastMessage( "Vishal", "Hey this is me");
      m2 = MessageFactory.makeBroadcastMessage( "Vishal2", "Hey this is me");
      m3 = MessageFactory.makeBroadcastMessage( "Vishal3", "Hey this is me\n");
      soc.write(ByteBuffer.wrap(m1.toString().getBytes()));
      soc.write(ByteBuffer.wrap(m2.toString().getBytes()));
      soc.write(ByteBuffer.wrap(m3.toString().getBytes()));

      NetworkConnection nc = new NetworkConnection(sc);
      Iterator<Message> it = nc.iterator();
      int count = 1;
      while (!it.hasNext()) {
        nc = new NetworkConnection(sc);
        it = nc.iterator();
        count++;
        if (count == 10) {
          fail();
        }
      }
      assertEquals(m1.toString(), it.next().toString());
      while (!it.hasNext()) {
        continue;
      }
      assertEquals(m2.toString(), it.next().toString());
      while (!it.hasNext()) {
        continue;
      }
      assertEquals(m3.toString(), it.next().toString());
      nc.close();
      serverSocket.close();
    } catch (IOException e) {
      e.printStackTrace();
      fail();
    }

  }

  @Test
  public void basicCreationTestNoMessages() {

    try {
      ServerSocketChannel serverSocket = ServerSocketChannel.open();

      serverSocket.configureBlocking(true);
      serverSocket.socket().bind(new InetSocketAddress(1235));
      connectToServer();
      SocketChannel sc = serverSocket.accept();


      NetworkConnection nc = new NetworkConnection(sc);
      Iterator<Message> it = nc.iterator();
      assertThrows(NoSuchElementException.class, it::next);
      assertFalse(it::hasNext);
      nc.close();
      serverSocket.close();
    } catch (IOException e) {
      fail();
    }

  }

  @Test
  public void sendNonFormatedMessage() {
    try {
      ServerSocketChannel serverSocket = ServerSocketChannel.open();

      serverSocket.configureBlocking(true);
      serverSocket.socket().bind(new InetSocketAddress(1235));
      connectToServer();
      SocketChannel sc = serverSocket.accept();

      System.out.println("after accepting connection");

      soc.write(ByteBuffer.wrap("Hey this is me".getBytes()));

      NetworkConnection nc = new NetworkConnection(sc);
      Iterator<Message> it = nc.iterator();
      assertThrows(AssertionError.class, it::hasNext);

      nc.close();
      serverSocket.close();
    } catch (IOException e) {
      fail();
    }
  }

  @Test
  public void zeroLengthFirst() {
    try {
      ServerSocketChannel serverSocket = ServerSocketChannel.open();

      serverSocket.configureBlocking(true);
      serverSocket.socket().bind(new InetSocketAddress(1235));
      connectToServer();
      SocketChannel sc = serverSocket.accept();

      System.out.println("after accepting connection");

      soc.write(ByteBuffer.wrap("0  30 Hey".getBytes()));

      NetworkConnection nc = new NetworkConnection(sc);
      Iterator<Message> it = nc.iterator();
      assertThrows(AssertionError.class, it::hasNext);

      nc.close();
      serverSocket.close();
    } catch (IOException e) {
      fail();
    }
  }

  @AfterEach
  public void init() {
    try {
      soc.close();
      thread.stop();
    } catch (IOException e) {
      fail();
    }
  }

}
