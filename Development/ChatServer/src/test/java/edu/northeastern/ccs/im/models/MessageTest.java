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

package edu.northeastern.ccs.im.models;

import edu.northeastern.ccs.im.EntityManagerHelper;
import edu.northeastern.ccs.im.services.EntityManagerResource;
import edu.northeastern.ccs.im.services.UserService;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;

class MessageTest {
    @Test
    void getterTest() {
        UserService.createUser("a", null, null, null, null);
        UserService.createUser("b", null, null, null, null);

        User sender = UserService.findUser("a");
        User receiver = UserService.findUser("b");

        MessageModel msg = new MessageModel(sender, receiver, null,"This is a message", "NTF");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        assertEquals("a", msg.getSender().getUsername());
        assertEquals("b", msg.getReceiver().getUsername());
        assertEquals("This is a message", msg.getMessageText());
        assertEquals(timestamp.getTime(), msg.getCreatedTimeStamp().getTime(), 0.1);
        Timestamp timestamp2 = new Timestamp(System.currentTimeMillis());
        msg.setSeenTimeStamp(timestamp2);
        assertNull(msg.getGroup());
        assertNull(msg.getId());
        assertEquals(timestamp2.getTime(), msg.getSeenTimeStamp().getTime(), 0.001);
        assertEquals("NTF", msg.getMessageType());

        EntityManagerHelper.truncateAll();
    }
}
