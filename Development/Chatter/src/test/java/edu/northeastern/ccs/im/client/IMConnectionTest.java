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

import edu.northeastern.ccs.im.client.exceptions.InvalidListenerException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IMConnectionTest {
    @Test
    void addMessageListener() {
        IMConnection imConnection = new IMConnection("randomhost", 1234);

        Exception exception = Assertions.assertThrows(InvalidListenerException.class, () -> {
            imConnection.addMessageListener(null);
        });

        assertEquals("Cannot add (null) as a listener!", exception.getMessage());
    }

    @Test
    void connect() throws Exception {
    }

    @Test
    void connectionActive() {
    }

    @Test
    void disconnect() {
    }

    @Test
    void getKeyboardScanner() {
    }

    @Test
    void getMessageScanner() {
    }

    @Test
    void getUserName() {
    }

    @Test
    void sendMessage() {
    }

    @Test
    void fireSendMessages() {
    }

    @Test
    void fireStatusChange() {
    }

    @Test
    void loggedOut() {
    }
}