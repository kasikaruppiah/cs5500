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
import java.nio.channels.ConnectionPendingException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class NetworkConnectionTest {
  SocketChannel soc;
  Thread thread;

  public void connectToServer() {
    try {
      soc = SocketChannel.open();
      soc.configureBlocking(true);
      thread = new Thread(() -> {
        try {
          soc.connect(new InetSocketAddress(2234));
          System.out.println("Connected");
        } catch (ConnectionPendingException | IOException e) {
          fail();
        }
      });
      thread.start();
    } catch (IOException e) {
      fail();
    }
  }


  @Test
  public void basicCreationTest() {
    try {
      ServerSocketChannel serverSocket = ServerSocketChannel.open();
      serverSocket.configureBlocking(true);
      serverSocket.socket().bind(new InetSocketAddress(2234));

      connectToServer();
      SocketChannel sc = serverSocket.accept();
      NetworkConnection nc = new NetworkConnection(sc);
      Message m = MessageFactory.makeBroadcastMessage( "Vishal", "Hey this is me");
      nc.sendMessage(m);
      ByteBuffer buf = ByteBuffer.allocate(m.toString().getBytes().length);
      soc.read(buf);
      assertEquals(buf.compact(), ByteBuffer.wrap(m.toString().getBytes()));
      sc.close();
      serverSocket.close();
    } catch (IOException e) {
      fail();
    }
  }

  @Test
  public void IOExceptionInConstructor() {
    try {
      ServerSocketChannel serverSocket = ServerSocketChannel.open();
      serverSocket.configureBlocking(true);
      serverSocket.socket().bind(new InetSocketAddress(2234));

      connectToServer();
      SocketChannel sc = serverSocket.accept();
      sc.configureBlocking(false);
      Selector selector = Selector.open();
      sc.register(selector, SelectionKey.OP_READ);
      sc.close();
      assertThrows(AssertionError.class, () -> new NetworkConnection(sc));

      sc.close();
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