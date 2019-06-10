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

package edu.northeastern.ccs.im.server;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.fail;

import edu.northeastern.ccs.im.EntityManagerHelper;
import edu.northeastern.ccs.im.message.Message;
import edu.northeastern.ccs.im.message.MessageFactory;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledExecutorService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mock;
import org.mockito.Mockito;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PrattleTest {

  private Thread thread;

  @Mock
  ClientRunnable runnable;
  @Mock
  ServerSocketChannel ssc;
  @Mock
  ScheduledExecutorService ses;

  private int index = -1;

  private String[][] args = {{"4444"}, {"4445"}, {"4446"}, {"4447"}, {"4448"}};

  @BeforeEach
  public void init() {

    index++;
    Prattle.stopServer();
    runnable = Mockito.mock(ClientRunnable.class);
    ssc = Mockito.mock(ServerSocketChannel.class);
    ses = Mockito.mock(ScheduledExecutorService.class);
    thread = new Thread(() -> Prattle.main(args[index]));
    thread.start();
    try {
      await().until(isReady());
    } catch (Exception e) {
      fail(e.toString());
    }
  }

  private Callable<Boolean> isReady() {
    return new Callable<Boolean>() {
      public Boolean call() throws Exception {
        Field is = Prattle.class.getDeclaredField("isReady");
        is.setAccessible(true);
        return is.getBoolean(null); // The condition that must be fulfilled
      }
    };
  }

  private Callable<Boolean> isNotReady() {
    return new Callable<Boolean>() {
      public Boolean call() throws Exception {

        Field is = Prattle.class.getDeclaredField("isReady");
        is.setAccessible(true);
        return !is.getBoolean(null); // The condition that must be fulfilled
      }
    };
  }

  @AfterEach
  public void stopServer() {
    Prattle.stopServer();
    try {
      Field isReady = Prattle.class.getDeclaredField("isReady");
      isReady.setAccessible(true);
      await().until(isNotReady());
    } catch (Exception e) {
      fail(e.toString());
    }
    EntityManagerHelper.truncateUsers();
    EntityManagerHelper.truncateMessages();
  }

  @Test
  public void createClientAccessPrivateMethodAndAddClient() {
    try {
      SocketChannel channel = null;

      channel = SocketChannel.open();

      // Make this channel a non-blocking channel
      channel.configureBlocking(true);
      // Connect the channel to the remote port
      channel.connect(new InetSocketAddress(Integer.parseInt(args[index][0])));
      Message message1 = MessageFactory.makeSimpleLoginMessage("abc", "abc");
      Prattle.broadcastMessage(message1);
      channel.close();
    } catch (IOException e) {
      e.printStackTrace();
      fail(e.toString());
    }

  }

  @Test
  public void tryConnectingToPrattle() {
    try {
      SocketChannel sc = SocketChannel
          .open(new InetSocketAddress(Integer.parseInt(args[index][0])));
      sc.write(ByteBuffer
          .wrap(MessageFactory.makeSimpleLoginMessage("Vishal", "Vishal").toString().getBytes()));
      sc.close();
    } catch (IOException e) {
      fail();
    }
  }

  @Test
  public void removeClient() {
    Prattle.removeClient(runnable);
  }
}