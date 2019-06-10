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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GroupTest {

    @Test
    void getterTest() {
        User user = new User("ab", "#123", "a", "b", "ab@gmail.com");
        Group group = new Group("msd",false,user);
        assertEquals("msd", group.getGroupname());
        assertFalse( group.getApproval());
        assertEquals(1, group.getModerators().size());
        group.addParticipants(user);
        assertEquals(1, group.getParticipants().size());
        assertNotNull(group.getCreatedTimeStamp());
    }

    @Test
    void removeParticipantTest(){
        User user = new User("admin", "admin", "a", "b", "ab@gmail.com");
        Group group = new Group("msd",false,user);
        User participant= new User("user1","user1","tom","wade","tw@gmail.com");
        group.addParticipants(user);
        group.addParticipants(participant);
        assertEquals(2,group.getParticipants().size());
        group.removeParticipants(participant);
        assertEquals(1,group.getParticipants().size());
    }

    @Test
    void removeModeratorTest(){
        User user = new User("admin", "12345", "raj", "n", "rn@gmail.com");
        Group group = new Group("neu",false,user);
        group.removeModerator(user);
        assertEquals(0,group.getModerators().size());
    }

    @Test
    void groupHasUserTest(){
        User user = new User("admin", "12345", "raj", "n", "rn@gmail.com");
        Group group = new Group("neu",false,user);
        group.addParticipants(user);
        assertTrue(group.hasUser(user));
    }

    @Test
    void groupHasNoUserTest(){
        User user = new User("admin", "12345", "raj", "n", "rn@gmail.com");
        Group group = new Group("neu",false,user);
        assertFalse(group.hasUser(user));
    }

    @Test
    void groupParticipantsTest(){
        User user = new User("admin", "12345", "raj", "n", "rn@gmail.com");
        Group group = new Group("neu",false,user);
        group.addParticipants(user);
        assertEquals(group.getGroupParticipantsUsernames(), "admin");
    }

    @Test
    void groupIsModeratorTest(){
        User user = new User("admin", "12345", "raj", "n", "rn@gmail.com");
        Group group = new Group("neu",false,user);
        group.addParticipants(user);
        assertTrue(group.isModerator(user));
    }

    @Test
    void groupIsModeratorFalseTest(){
        User user = new User("admin", "12345", "raj", "n", "rn@gmail.com");
        Group group = new Group("neu",false,user);
        User user2 = new User("neha", "12345", "raj", "n", "rn@gmail.com");
        assertFalse(group.isModerator(user2));
    }

}
