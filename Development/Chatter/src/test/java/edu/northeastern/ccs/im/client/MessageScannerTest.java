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

import edu.northeastern.ccs.im.client.message.Message;
import edu.northeastern.ccs.im.client.message.MessageFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;

class MessageScannerTest {
    private MessageScanner messageScanner;
    private Field instance;
    private Queue<Message> messages;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        messageScanner = MessageScanner.getInstance();

        instance = MessageScanner.class.getDeclaredField("instance");
        instance.setAccessible(true);

        Field messagesField = MessageScanner.class.getDeclaredField("messages");
        messagesField.setAccessible(true);

        messages = (Queue<Message>) messagesField.get(messageScanner);
    }

    @AfterEach
    void tearDown() throws IllegalAccessException {
        instance.set(null, null);
    }

    @Test
    void getInstance() {
        MessageScanner newMessageScanner = MessageScanner.getInstance();

        assertEquals(messageScanner, newMessageScanner);
    }

    @Test
    void messagesReceived() {
        assertEquals(0, messages.size());
        assertFalse(messageScanner.hasNext());

        List<Message> messageList = Arrays.asList(MessageFactory.makeHelloMessage("JDoe", "password"));

        messageScanner.messagesReceived(messageList.iterator());

        assertEquals(1, messages.size());
        assertTrue(messageScanner.hasNext());
    }

    @Test
    void next() {
        Message loginMessage = MessageFactory.makeHelloMessage("JDoe", "password");
        List<Message> messageList = Arrays.asList(loginMessage);

        messageScanner.messagesReceived(messageList.iterator());

        assertEquals(loginMessage, messageScanner.next());
        assertEquals(0, messages.size());
        assertFalse(messageScanner.hasNext());
    }

    @Test
    void nextEmpty() {
        Exception exception = assertThrows(NoSuchElementException.class, () -> {
            messageScanner.next();
        });

        assertEquals("There was no next Message to be returned!", exception.getMessage());
    }

    @Test
    void remove() {
        Exception exception = assertThrows(UnsupportedOperationException.class, () -> {
            messageScanner.remove();
        });

        assertEquals("Cannot remove a Message from IMScanner.", exception.getMessage());
    }
}