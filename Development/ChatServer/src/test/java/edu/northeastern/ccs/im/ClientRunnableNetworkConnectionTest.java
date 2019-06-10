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

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ConnectionPendingException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import edu.northeastern.ccs.im.message.Message;
import edu.northeastern.ccs.im.message.MessageFactory;
import edu.northeastern.ccs.im.server.ClientRunnable;
import edu.northeastern.ccs.im.server.ClientTimer;
import edu.northeastern.ccs.im.server.Prattle;
import edu.northeastern.ccs.im.services.UserService;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ClientRunnableNetworkConnectionTest {
    SocketChannel soc;
    Message m1;
    Message m2;
    Message m3;

    public void connectToServer(boolean sendMessage, Message m, int port) {
        new Thread(() -> {
            try {
                soc = SocketChannel.open();
                soc.configureBlocking(true);
                try {
                    soc.connect(new InetSocketAddress(port));
                } catch (ConnectionPendingException | IOException e) {
                    e.printStackTrace();
                    fail();
                }

                if (sendMessage) {
                    if (m == null) {

                        m3 = MessageFactory.makeSimpleLoginMessage("neha", "neha");
                        m1 = MessageFactory.makeBroadcastMessage("Vishal", "Hey this is me");
                        m2 = MessageFactory.makeQuitMessage("Neha");
                        try {
                            soc.write(ByteBuffer.wrap(m1.toString().getBytes()));
                            soc.write(ByteBuffer.wrap(m2.toString().getBytes()));
                            soc.write(ByteBuffer.wrap(m3.toString().getBytes()));
                        } catch (IOException e) {
                            fail();
                        }
                    } else {
                        try {
                            soc.write(ByteBuffer.wrap(m.toString().getBytes()));
                        } catch (IOException e) {
                            fail();
                        }
                    }
                } else {
                    //write code if you expect to recieve message whatever you need to do here
                }
                System.out.println("Connected successfully");
            } catch (
                    IOException e) {
                fail();
            }
        }).start();
    }

    @Test
    public void clientRunnableNetworkConnectionTest() {
//    if you want to recieve message flag is true else false

        try {
            ServerSocketChannel serverSocket = ServerSocketChannel.open();

            serverSocket.configureBlocking(true);
            serverSocket.socket().bind(new InetSocketAddress(6666));
            await().until(server(serverSocket));
            connectToServer(true, null, 6666);
            await().until(serverSocket::isOpen);

            NetworkConnection nc = new NetworkConnection(serverSocket.accept());
            ClientRunnable cr = new ClientRunnable(nc);
            cr.run();
            Field terminateField = cr.getClass().getDeclaredField("terminate");
            terminateField.setAccessible(true);
            terminateField.set(cr, true);

            Field scheduledFutureField = cr.getClass().getDeclaredField("runnableMe");
            scheduledFutureField.setAccessible(true);
            cr.setFuture(Mockito.mock(ScheduledFuture.class));
            cr.terminateClient();
            //if you send message from here
//      ensure flag above is false and in the else there are some asserts.
            nc.close();
            serverSocket.close();
        } catch (IOException | NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void messageHasNoName() {
        //    if you want to recieve message flag is true else false

        try {
            ServerSocketChannel serverSocket = ServerSocketChannel.open();

            serverSocket.configureBlocking(true);
            serverSocket.socket().bind(new InetSocketAddress(5235));

            await().until(serverSocket::isOpen);
            connectToServer(true, MessageFactory.makeSimpleLoginMessage("", ""), 5235);
            NetworkConnection nc = new NetworkConnection(serverSocket.accept());
            ClientRunnable cr = new ClientRunnable(nc);
            assertFalse(cr.isInitialized());
            assertTrue(Math.abs(cr.getUserId()) >= 0);
            cr.setFuture(Mockito.mock(ScheduledFuture.class));
            cr.terminateClient();
            //if you send message from here
//      ensure flag above is false and in the else there are some asserts.
            nc.close();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void callRunEnqueueDequeueMessage() {

        String[] args = {"6666"};
        try {
            ServerSocketChannel serverSocket = ServerSocketChannel.open();
            serverSocket.socket().bind(new InetSocketAddress(5237));
            serverSocket.configureBlocking(true);
            await().until(serverSocket::isOpen);
            connectToServer(true, null, 5237);
            NetworkConnection nc = new NetworkConnection(serverSocket.accept());
            ClientRunnable cr = new ClientRunnable(nc);


            cr.enqueueMessage(MessageFactory.makeSimpleLoginMessage("Broadcast", "hello hello"));
            cr.enqueueMessage(MessageFactory.makeBroadcastMessage("Broadcast", "hello"));
            cr.enqueueMessage(MessageFactory.makeQuitMessage("Broadcast"));

            ScheduledFuture<?> clientFuture = Executors.newScheduledThreadPool(10).scheduleAtFixedRate(cr, 100, 100
                    , TimeUnit.MILLISECONDS);

            cr.setFuture(clientFuture);
            cr.run();

            GregorianCalendar cal = new GregorianCalendar();
            cal.set(Calendar.YEAR, 2015);
            System.out.println(cal.getTime());
            ClientTimer timer = new ClientTimer();
            Field calf = ClientTimer.class.getDeclaredField("calendar");
            calf.setAccessible(true);
            calf.set(timer, cal);
            Field timerf = ClientRunnable.class.getDeclaredField("timer");
            timerf.setAccessible(true);
            timerf.set(cr, timer);

            cr.run();
            cr.terminateClient();
            nc.close();
            serverSocket.close();
            Prattle.stopServer();
        } catch (IOException | NoSuchFieldException | IllegalAccessException e) {
            fail();
        }
    }

    @BeforeAll
    public void setup() {
        UserService.createUser("jake123", "jake123##", "Jake", "Dan", "jd@gmail.com");
        UserService.createUser("neha", "neha", "Neha", "Gundecha", "ng@gmail.com");
    }

    @AfterAll
    public void truncateTable() {
        EntityManagerHelper.truncateUsers();
        EntityManagerHelper.truncateGroups();
        EntityManagerHelper.truncateMessages();
    }

    @AfterEach
    public void init() {
        try {
            soc.close();
            await().until(socIsConnected());
            soc = null;
        } catch (IOException e) {
            fail();
        }
    }

    private Callable<Boolean> socIsConnected() {
        return new Callable<Boolean>() {
            public Boolean call() throws Exception {
                return !soc.isConnected(); // The condition that must be fulfilled
            }
        };
    }

    private Callable<Boolean> server(ServerSocketChannel ss) {
        return new Callable<Boolean>() {
            public Boolean call() throws Exception {
                return ss.isOpen(); // The condition that must be fulfilled
            }
        };
    }
}
