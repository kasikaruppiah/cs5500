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

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class GroupParticipationTest {

    @Test
    public void getterTest(){
        User user = new User("ab", "#123", "a", "b", "ab@gmail.com");
        Group group = new Group("msd",false,user);
        GroupParticipation groupParticipation= new GroupParticipation(user,group,new Date());
        groupParticipation.setLastActiveTimeStamp(new Date());
        assertEquals(user,groupParticipation.getUser());
        assertEquals(group,groupParticipation.getGroup());
        assertNotNull(groupParticipation.getId());
        assertNotNull(groupParticipation.getLastActiveTimeStamp());
    }

    @Test
    public void equalsTest(){
        User user = new User("raj", "raj", "raj", "n", "rn@gmail.com");
        Group group = new Group("neu",false,user);
        User user1 = new User("v", "v", "v", "p", "vp@gmail.com");
        GroupParticipation groupParticipation= new GroupParticipation(user,group,new Date());
        GroupParticipation groupParticipation1= new GroupParticipation(user1,group,new Date());
        assertNotEquals(groupParticipation, groupParticipation1);
    }
}
