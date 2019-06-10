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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.GregorianCalendar;
import java.util.concurrent.ScheduledFuture;

import edu.northeastern.ccs.im.server.ClientRunnable;
import edu.northeastern.ccs.im.server.ClientTimer;

import static org.junit.jupiter.api.Assertions.fail;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ClientRunnableTimerBehindTest {
  SocketChannel soc;

  public void connectToServer() {
    try {
      soc = SocketChannel.open();
      soc.configureBlocking(true);

      new Thread(() -> {
        try {
          soc.connect(new InetSocketAddress(3234));
        } catch (IOException e) {
          fail();
        }
        System.out.println("Connected successfully");
      }).start();

    } catch (IOException e) {
      fail();
    }
  }

  @Test
  public void createClientExpireTimer() {

    try {
      ServerSocketChannel serverSocket = ServerSocketChannel.open();

      serverSocket.configureBlocking(true);
      serverSocket.socket().bind(new InetSocketAddress(3234));
      connectToServer();
      NetworkConnection nc = new NetworkConnection(serverSocket.accept());
      ClientRunnable cr = new ClientRunnable(nc);
      cr.run();
      Field timer = cr.getClass().getDeclaredField("timer");
      timer.setAccessible(true);

      ClientTimer timerToSet = new ClientTimer();
      Field cal = ClientTimer.class.getDeclaredField("calendar");
      cal.setAccessible(true);
      GregorianCalendar caltoSet = new GregorianCalendar();
      caltoSet.set(GregorianCalendar.YEAR, 2011);
      cal.set(timerToSet, caltoSet);
//      System.out.println(timerToSet.isBehind());
      timer.set(cr, timerToSet);

      Field scheduledFutureField = cr.getClass().getDeclaredField("runnableMe");
      scheduledFutureField.setAccessible(true);
      cr.setFuture(Mockito.mock(ScheduledFuture.class));
      cr.run();
      //if you send message from here
//      ensure flag above is false and in the else there are some asserts.
      nc.close();
      serverSocket.close();
    } catch (IOException | NoSuchFieldException | IllegalAccessException e) {
      e.printStackTrace();
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
