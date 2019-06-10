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

import edu.northeastern.ccs.im.EntityManagerHelper;
import edu.northeastern.ccs.im.MessageType;
import edu.northeastern.ccs.im.message.Message;
import edu.northeastern.ccs.im.message.MessageFactory;
import edu.northeastern.ccs.im.services.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PrattlePrivateMessageTest {
    private Thread thread;

    SocketChannel soc;

    private String[] args = {"4646"};

    @BeforeEach
    public void init() {
        UserService.createUser("jake123", "jake123##", "Jake", "Dan", "jd@gmail.com");
        UserService.createUser("neha", "neha", "Neha", "Gundecha", "ng@gmail.com");

        Prattle.stopServer();
        thread = new Thread(() -> Prattle.main(args));
        thread.start();
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
        EntityManagerHelper.truncateMessages();
    }

    public void connectToServer() {
        try {
            soc = SocketChannel.open();
            soc.configureBlocking(true);
            soc.connect(new InetSocketAddress(4646));
        } catch (IOException e) {
            fail();
        }
    }


    @Test
    public void sendPrivateMessage() {
        try {
            connectToServer();
            Message m1 = MessageFactory.makeSimpleLoginMessage("neha", "neha");
            soc.write(ByteBuffer.wrap(m1.toString().getBytes()));
            await().atMost(300, TimeUnit.MILLISECONDS);
            Message m2 = MessageFactory.makeMessage(MessageType.EDIT, "email", "Ahabsdf@gmail.com");
            soc.write(ByteBuffer.wrap(m2.toString().getBytes()));

            try {
                ConcurrentLinkedQueue<ClientRunnable> list = new ConcurrentLinkedQueue<>();
                while (list.size() != 1) {
                    Field active = Prattle.class.getDeclaredField("active");
                    active.setAccessible(true);
                    list = (ConcurrentLinkedQueue<ClientRunnable>) active.get(null);
                }
                assertEquals(1, list.size());
                list.peek().run();
                Prattle.privateMessage(MessageFactory.makePrivateMessage("ABC", "Hey this is ABC"),
                        "neha");
                ClientRunnable vishal = list.stream().filter(cR -> cR.getName().compareTo("neha") == 0).findFirst().get();
                Field waitingList = vishal.getClass().getDeclaredField("waitingList");
                waitingList.setAccessible(true);
                Queue<Message> wtList = (Queue<Message>) waitingList.get(vishal);
                assertTrue(wtList.size() != 0);
                vishal.handleIncomingMessages();
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
            soc.close();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
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
}
