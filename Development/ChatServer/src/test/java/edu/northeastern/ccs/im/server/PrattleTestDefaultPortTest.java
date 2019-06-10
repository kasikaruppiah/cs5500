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

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ConnectionPendingException;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

import edu.northeastern.ccs.im.EntityManagerHelper;
import edu.northeastern.ccs.im.MessageType;
import edu.northeastern.ccs.im.message.Message;
import edu.northeastern.ccs.im.message.MessageFactory;
import edu.northeastern.ccs.im.services.GroupService;
import edu.northeastern.ccs.im.services.MessageService;
import edu.northeastern.ccs.im.services.UserService;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PrattleTestDefaultPortTest {
  private Thread threadServer;

  SocketChannel soc;

  private String[] args = {};

  @BeforeAll
  void setup() {
    UserService.createUser("jake123", "jake123##", "Jake", "Dan", "jd@gmail.com");
    UserService.createUser("neha", "neha", "Neha", "Gundecha", "ng@gmail.com");
    MessageService.createMessage("neha", "jake123", "Hey this is neha", MessageType.PRIVATE.toString());
    MessageService.createMessage("jake123", "neha", "Hey this is jake", MessageType.PRIVATE.toString());
    GroupService.createGroup("grp2", false, UserService.findUser("neha"));
  }

  @AfterAll
  void truncateTable() {
    EntityManagerHelper.truncateUsers();
    EntityManagerHelper.truncateMessages();
    EntityManagerHelper.truncateGroups();
  }

  @BeforeEach
  public void init() {
    Prattle.stopServer();
    threadServer = new Thread(() -> Prattle.main(args));
    threadServer.start();
    try {
      await().until(isReady());
    } catch (Exception e) {
      fail(e.toString());
    }
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
  }

  public void connectToServer() {
    try {
      soc = SocketChannel.open();
      soc.configureBlocking(true);
      soc.connect(new InetSocketAddress(4545));
    } catch (ConnectionPendingException |
            IOException e) {
      fail();
    }

  }

  @SuppressWarnings("SleepWhileHoldingLock")
  @Test
  public void sendBroadcastMessage() {
    connectToServer();
    List<Message> list = new ArrayList<>();
    list.add(MessageFactory.makeSimpleLoginMessage("jake123", "jake123##"));
//    list.add(MessageFactory.makeProfileMessage("$jake123"));
    list.add(MessageFactory.makeProfileMessage("@grp2"));
    list.add(MessageFactory.makeProfileMessage("@grp2hg"));
    list.add(MessageFactory.makeEditMessage("firstName", "Jake boy"));
    list.add(MessageFactory.makePrivateMessage("neha", "hey"));
    list.add(MessageFactory.makeSearchMessage("user", "neha"));
    list.add(MessageFactory.makeBroadcastMessage(null, "Hey this is a broadcasst"));
    list.add(MessageFactory.makeOpenMessage("$neha"));
    list.add(MessageFactory.makeOpenMessage("$neha1234"));
    list.add(MessageFactory.makeNotificationMessage(""));
    list.add(MessageFactory.makeNewGroupMessage("jake123", "grp1"));
    list.add(MessageFactory.makeGroupMessage("grp1", "heuya"));
    list.add(MessageFactory.makeOpenMessage("@grp1"));
    list.add(MessageFactory.makeOpenMessage("@grp11"));
    list.add(MessageFactory.makeDeleteGroupMessage("neha", "grp2"));
    list.add(MessageFactory.makeAddParticipantMessage("grp1", "jdoe"));
    list.add(MessageFactory.makeMessage(MessageType.GROUP_APPROVAL_EDIT, "grp9", "heuya"));
    list.add(MessageFactory.makeMessage(MessageType.GROUP_APPROVAL_EDIT, "grp2", "heuya"));
    list.add(MessageFactory.makeMessage(MessageType.GROUP_APPROVAL_EDIT, "grp1", "heuya"));
    list.add(MessageFactory.makeAddParticipantMessage("grp2", "jake123"));
    list.add(MessageFactory.makeDeleteParticipantMessage("grp2",null));
    list.add(MessageFactory.makeDeleteParticipantMessage("grp2","jake123"));
    list.add(MessageFactory.makeAddParticipantMessage("grp2", "jdoe"));
    list.add(MessageFactory.makeGroupInviteMessage("grp2","jake123"));
    list.add(MessageFactory.makeGroupInviteMessage("grp2","jdoe"));
    list.add(MessageFactory.makeSearchMessage("user", "neha"));
    list.add(MessageFactory.makeSearchMessage("user", "sadsad"));
    list.add(MessageFactory.makeSearchMessage("group", "grp2"));
    list.add(MessageFactory.makeSearchMessage("group", "adsfadf"));
    list.add(MessageFactory.makeQuitMessage("jake123"));
    await().until(socIsConnected());
    try {
      for (Message m : list) {
        soc.write(ByteBuffer.wrap(m.toString().getBytes()));
      }
    } catch (IOException e) {
      System.out.println("cannot write in socket");
      fail();
    }
    await().atLeast(300, TimeUnit.MILLISECONDS);
    try {

      Field active = Prattle.class.getDeclaredField("active");
      active.setAccessible(true);
      ConcurrentLinkedQueue<ClientRunnable> lists = new ConcurrentLinkedQueue<>();
      while (lists.size() != 1) {
        lists = (ConcurrentLinkedQueue<ClientRunnable>) active.get(null);
      }
      final ConcurrentLinkedQueue<ClientRunnable> list2 = (ConcurrentLinkedQueue<ClientRunnable>) active.get(null);
      Method out = ClientRunnable.class.getDeclaredMethod("handleOutgoingMessages", null);
      out.setAccessible(true);

      assertEquals(1, lists.size());
      lists.peek().run();
      await().until(lists.peek()::isInitialized);
      Prattle.broadcastMessage(MessageFactory.makeBroadcastMessage("ABC", "Hey this is ABC"));
      assertTrue(lists.peek().isInitialized());
      Prattle.broadcastMessage(MessageFactory.makeBroadcastMessage("XYZ", "Hey this is XYZ"));

      Scanner reader = new Scanner(soc.socket().getInputStream());
      await().atMost(20000, TimeUnit.MILLISECONDS).until(() -> {
        StringBuilder build = new StringBuilder();
        while (true) {

          if (reader.hasNext()) {
            build.append(reader.next());
            build.append(" ");
            if (build.toString().contains("BYE 7 jake123 25 You have been logged out.")) {
              return true;
            }
          } else {
            try {
              out.invoke(list2.peek());
            } catch (Exception e) {
              continue;
            }
          }
        }
      });

      Prattle.removeClient(lists.peek());
    } catch (Exception e) {
      System.out.println();
      e.printStackTrace();
      fail();
    }
    Prattle.stopServer();
    try {
      soc.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private Callable<Boolean> socIsConnected() {
    return new Callable<Boolean>() {
      public Boolean call() throws Exception {
        return soc.isConnected(); // The condition that must be fulfilled
      }
    };
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
}
