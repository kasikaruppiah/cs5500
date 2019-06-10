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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ConnectionPendingException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class NetworkConnectionSendMessageExceptionTest {
  SocketChannel soc;
  Thread thread;

  public void connectToServer() {
    try {
      soc = SocketChannel.open();

      soc.configureBlocking(true);
      thread = new Thread(() -> {
        try {
          soc.connect(new InetSocketAddress(2222));
        } catch (ConnectionPendingException | IOException e) {
          fail();
        }
        System.out.println("Connected successfully");
      });
      thread.start();

    } catch (IOException e) {
      System.out.println(e);
      fail();
    }
  }


  @Test
  public void exceptionWhileSendingLargeMessage() {
    try {
      ServerSocketChannel serverSocket = ServerSocketChannel.open();

      serverSocket.socket().bind(new InetSocketAddress(2222));
      serverSocket.configureBlocking(true);
      connectToServer();
      SocketChannel sc = serverSocket.accept();
      NetworkConnection nc = new NetworkConnection(sc);
      BufferedReader br = new BufferedReader(new FileReader(new File("src/test/java/edu/northeastern/ccs/im/10mb.txt")));
      StringBuilder text = new StringBuilder();
      String read = br.readLine();
      while (read != null) {
        text.append(read);
        read = br.readLine();
      }
      Message m = MessageFactory.makeBroadcastMessage( "Vishal", text.toString());
      assertFalse(nc.sendMessage(m));
      nc.close();
      serverSocket.close();
    } catch (IOException e) {
      e.printStackTrace();
      fail();
    }
  }

  @Test
  public void basicCreationTest() {
    try {
      ServerSocketChannel serverSocket = ServerSocketChannel.open();

      serverSocket.socket().bind(new InetSocketAddress(2222));
      serverSocket.configureBlocking(true);
      connectToServer();
      SocketChannel sc = serverSocket.accept();
      while (soc.isOpen()) {
        soc.close();
      }
//      thread.
      NetworkConnection nc = new NetworkConnection(sc);
      Message m = MessageFactory.makeBroadcastMessage( "Vishal", "Hello abc");

      nc.sendMessage(m);
      serverSocket.close();
      nc.close();
      assertFalse(nc.sendMessage(m));
      nc.close();
      serverSocket.close();

    } catch (IOException e) {
      e.printStackTrace();
      fail();
    }
  }

  @AfterEach
  public void init() {

    try {
      soc.close();
    } catch (IOException e) {
      fail();
    }
  }

}
